package controller;

import model.Category;
import model.Dish;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/** Класс предназначенный для сериализации и десериализации
 */
public class Serialize {
    /**
     * Метод серилизации в JSON
     * @param dishes - список блюд
     * @param file - файл
     * @throws IOException
     */
    public static void serialize(List<Dish> dishes,File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, dishes);
    }
    /**
     * Метод серилизации в JSON категорий
     * @param categories - список категорий
     * @param file - файл
     * @throws IOException
     */
    public static void serializeCategory(List<Category> categories, File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, categories);
    }
    /**
     * Метод десерилизации из JSON
     * @param file - файл
     * @return возвращает список блюд
     * @throws IOException
     */
    public static List<Dish> deserialize(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Dish> dishes =mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Dish.class));
        return dishes;
    }
    /**
     * Метод десерилизации из JSON категорий
     * @param file - файл
     * @return возвращает список категорий
     * @throws IOException
     */
    public static List<Category> deserializeCategory(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories =mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Category.class));
        return categories;
    }

    /**
     * Метод сериализации блюда в байтовый поток
     * @param dish - блюдо
     * @param out - поток
     */
    public static void serializeDish (Dish dish, OutputStream out){
        try {
            ObjectOutputStream stream = new ObjectOutputStream(out);
            stream.writeObject(dish);
            stream.flush();
        }
        catch (IOException e){
            System.out.println("Some error occurred!");
        }
    }

    /**
     * Метод десериализации блюда из байтового потока
     * @param in - поток
     * @return
     */
    public static Dish deserializeDish (InputStream in){
        try {
            ObjectInputStream stream = new ObjectInputStream(in);
            Dish dish = (Dish) stream.readObject();
            return dish;
        }
        catch (IOException e){
            System.out.println("Some error occurred!");
            return null;
        }
        catch(ClassNotFoundException e) {
            System.out.println("Wrong object type");
            return null;
        }
    }

    /**
     * Метод сериализации файла в байтовый поток
     * @param nameFile - имя файла
     * @param out - поток
     */
    public static void serializeFile(String nameFile,OutputStream out){
        try {
            FileInputStream fileInputStream = new FileInputStream(nameFile);
            byte[] buffer = new byte[16*1024];
            int len;
            if((len=fileInputStream.read(buffer)) >0){
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод десериализации файла из байтового потока
     * @param nameFile - имя файла
     * @param in - поток
     */
    public  static void deserializeFile(String nameFile,InputStream in){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(nameFile);
            byte[] buffer = new byte[16*1024];
            int len= in.read(buffer);
            fileOutputStream.write(buffer, 0, len);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
