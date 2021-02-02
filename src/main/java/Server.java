import controller.Controller;
import controller.Serialize;
import model.Category;
import model.Dish;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    /**
     * интерфейс управления потоками
     */
    static ExecutorService executeIt = Executors.newCachedThreadPool();

    private static class ServerRun implements Runnable {
        /**
         * поле для сокета клиента
         */
        private Socket clientSocket;
        /**
         * поле для файла блюд
         */
        private File file;
        /**
         * поле для списка блюд
         */
        public List<Dish> dishes;
        /**
         * поле для списка категорий
         */
        public List<Category> categories;

        /**
         * конструктор - создание потока
         * @param client - сокет клиента
         */
        public ServerRun(Socket client) {
            this.clientSocket = client;
        }

        /**
         * Метод создания данных
         * @param in - поток для входных данных
         * @param out - поток для выходных данных
         * @param dataModifFile - значение последнего изменения файла
         * @param testEmpty - проверка на существование данных
         */
        public void addData (DataInputStream in,DataOutputStream out,long dataModifFile,boolean testEmpty) {
            try {
                dataModifFile = 0;
                testEmpty=false;
                String nameFile = in.readUTF();
                categories = new ArrayList<>();
                dishes = new ArrayList<>();
                if (!nameFile.equals("newDish")) {
                    file = new File(nameFile);
                    dataModifFile = file.lastModified();
                    if (file.length() > 0) {
                        out.writeUTF("yes");
                        out.flush();
                        String clientReaction = in.readUTF();
                        if (clientReaction.equals("newData")) {
                            Serialize.deserializeFile(nameFile, in);
                            if (file.length() > 0) {
                                dishes = Serialize.deserialize(file);
                                categories.add(dishes.get(0).getCategory());
                                Controller.addCategoryByDish(dishes, categories);
                                testEmpty = true;
                                out.writeUTF("not");
                                out.flush();
                            } else {
                                out.writeUTF("empty");
                                out.flush();
                            }
                        } else {
                            dishes = Serialize.deserialize(file);
                            categories.add(dishes.get(0).getCategory());
                            Controller.addCategoryByDish(dishes, categories);
                            testEmpty = true;
                        }
                    } else {
                        out.writeUTF("not");
                        out.flush();
                        Serialize.deserializeFile(nameFile, in);
                        if (file.length() > 0) {
                            dishes = Serialize.deserialize(file);
                            categories.add(dishes.get(0).getCategory());
                            Controller.addCategoryByDish(dishes, categories);
                            testEmpty = true;
                            out.writeUTF("not");
                            out.flush();
                        } else {
                            out.writeUTF("empty");
                            out.flush();
                        }
                    }
                } else {
                    out.writeUTF("empty");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * метод запуска потока
         */
        @Override
        public void run() {
            try {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                long dataModifFile = 0;
                boolean testEmpty=false;
                addData(in,out,dataModifFile,testEmpty);
                while (!clientSocket.isClosed()) {
                    if(dataModifFile != 0){
                        if(dataModifFile != file.lastModified()){
                            out.writeUTF("Yes");
                            out.flush();
                        }else{
                            out.writeUTF("No");
                            out.flush();
                        }
                    }else{
                        out.writeUTF("No");
                        out.flush();
                    }
                    String method = in.readUTF();
                    if(method.equals("Stop")){
                        out.flush();
                        break;
                    }
                    int numDish;
                    Dish dish;
                    String name;
                    String nameCategory;
                    double price;
                    switch (method) {
                        case "setDataByNumber":
                            numDish = in.readInt();
                            dish = Serialize.deserializeDish(in);
                            Controller.setDataByNumber(numDish,dish,dishes);
                            break;
                        case "setDataByName":
                            name = in.readUTF();
                            dish = Serialize.deserializeDish(in);
                            Controller.setDataByName(name,dish,dishes);
                            break;
                        case "setCategoryByName":
                            name = in.readUTF();
                            nameCategory=in.readUTF();
                            Controller.setCategoryByName(name,new Category(nameCategory),dishes);
                            break;
                        case "setNameByName":
                            name = in.readUTF();
                            String newName=in.readUTF();
                            Controller.setNameByName(name,newName,dishes);
                            break;
                        case "setPriceByName":
                            name = in.readUTF();
                            price=in.readDouble();
                            Controller.setPriceByName(name,price,dishes);
                            break;
                        case "addData":
                            dish = Serialize.deserializeDish(in);
                            if(testEmpty) {
                                if (Controller.addData(dish, dishes, categories)) {
                                    out.writeUTF("Yes");
                                    out.flush();
                                } else {
                                    out.writeUTF("No");
                                    out.flush();
                                }
                            }
                            else{
                                dishes.add(dish);
                                categories.add(dish.getCategory());
                                testEmpty=true;
                                out.writeUTF("Yes");
                                out.flush();
                            }
                            break;
                        case "deleteData":
                            name = in.readUTF();
                            Controller.deleteData(name,dishes);
                            break;
                        case "printDish":
                            out.writeUTF(Controller.printDish(dishes).toString());
                            out.flush();
                            break;
                        case "searchFile":
                            File directory = new File((this.getClass().getProtectionDomain().getCodeSource().getLocation()).getPath().replace("/target/classes",""));
                            StringBuffer fileList=new StringBuffer();
                            for ( File newfile : directory.listFiles() ){
                                if ( newfile.isFile() && newfile.getName().contains(".json")&& file.getName()!=newfile.getName())
                                    fileList.append(newfile.getName()+"*");
                            }
                            out.writeUTF(fileList.toString());
                            out.flush();
                            break;
                        case "addFileServer":
                            name = in.readUTF();
                            File otherFile=new File(name);
                            if(otherFile.length()>0){
                                List<Dish> otherDishes=Serialize.deserialize(otherFile);
                                if(Controller.addFile(dishes,otherDishes,categories)){
                                    out.writeUTF("Yes");
                                    out.flush();
                                }else{
                                    out.writeUTF("No");
                                    out.flush();
                                }

                            }else{
                                out.writeUTF("No");
                                out.flush();
                            }
                            break;
                        case "addFile":
                            name = in.readUTF();
                            Serialize.deserializeFile(name,in);
                            otherFile=new File(name);
                            if(otherFile.length()>0){
                                List<Dish> otherDishes=Serialize.deserialize(otherFile);
                                if(Controller.addFile(dishes,otherDishes,categories)){
                                    out.writeUTF("Yes");
                                    out.flush();
                                }else{
                                    out.writeUTF("No");
                                    out.flush();
                                }
                            }else{
                                out.writeUTF("No");
                                out.flush();
                            }
                            break;
                        case "addCategory":
                            nameCategory = in.readUTF();
                            if(Controller.addCategory(new Category(nameCategory),categories)){
                                out.writeUTF("Yes");
                                out.flush();
                            }else{
                                out.writeUTF("No");
                                out.flush();
                            }
                            break;
                        case "printDishByCategory":
                            nameCategory = in.readUTF();
                            out.writeUTF(Controller.printDishByCategory(nameCategory,dishes).toString());
                            out.flush();
                            break;
                        case "printCategory":
                            out.writeUTF(Controller.printCategory(categories).toString());
                            out.flush();
                            break;
                        case "getDataByName":
                            name = in.readUTF();
                            out.writeUTF(Controller.getDataByName(name,dishes).toString());
                            out.flush();
                            break;
                        case "getDataByCategory":
                            nameCategory = in.readUTF();
                            out.writeUTF(Controller.getDataByCategory(nameCategory,dishes).toString());
                            out.flush();
                            break;
                        case "saveFile":
                            Serialize.serialize(dishes,file);
                            dataModifFile=file.lastModified();
                            break;
                        case "saveNewFile":
                            name = in.readUTF();
                            File newFile= new File(name);
                            Serialize.serialize(dishes,newFile);
                            dataModifFile=newFile.lastModified();
                            break;
                        case "openNewFile":
                            addData(in,out,dataModifFile,testEmpty);
                            break;
                        case "downloadFile":
                            Serialize.serializeFile(file.getName(), out);
                            break;
                    }
                }
                in.close();
                out.close();
                System.out.println("Завершение работы");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket(8000);
            Socket serverClient = server.accept();
            executeIt.execute(new ServerRun(serverClient));
            server.close();
            executeIt.shutdown();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


