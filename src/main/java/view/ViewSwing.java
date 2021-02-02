import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;

public class ViewSwing extends JFrame {

    /** Поле для окна */
    private static JFrame frame;
    /** Поле для файла блюд */
    private static File file;
    /** Поле для файла клиента*/
    private static Client client;

    /**
     * Конструктор - создает клиент и запускает программу
     */
    ViewSwing() throws IOException {
        client = new Client(8000);
        begin();
    }

    /**
     * Метод окна с кнопками открыть и загрузить
     */
    private void begin(){
        frame = new JFrame("Меню");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550, 350));

        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));


        JLabel name = new JLabel("Редактор меню ресторана.");
        name.setFont(new Font("Italic", Font.PLAIN, 20));
        plain.add(name);
        plain.add(new JLabel(" Загрузите файл с меню или создайте новый:"));
        JButton open = new JButton("Создать");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    client.file("newFile");
                    newFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(open);

        JButton download = new JButton("Загрузить");
        download.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    download(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(download);

        frame.add(plain);
        frame.pack();
        frame.setVisible(true);
    }

    private void newFile(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        plain.add(new JLabel(" У вас пустое меню, чтобы продолжить работу нужно добавить блюда."));
        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addDish();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(add);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод получения файла
     */
    private void download(int k) throws IOException {
        JFileChooser dialog = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "json file", "json");
        dialog.setFileFilter(filter);
        int result = dialog.showOpenDialog(this);
        file = dialog.getSelectedFile();
        String s = "";
        if (result == JFileChooser.APPROVE_OPTION ){
            if (k == 0) {
                s = client.file(file.getName());
                if (s.equals("yes")){
                    JPanel panel = new JPanel();

                    panel.add(new JLabel("Такой файл уже есть на сервере. Вы хотите его перезаписать?"));

                    JButton yes = new JButton("Да");
                    yes.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                String s = client.file("newData");
                                if (s.equals("empty")){
                                    newFile();
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    JButton no = new JButton("Нет");
                    no.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                String s = client.file("");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    panel.add(yes);
                    panel.add(no);
                    JOptionPane.showConfirmDialog(null, panel, null, JOptionPane.PLAIN_MESSAGE);
                }
                else if (s.equals("not")){
                    client.serialize(file.getName());
                    s = client.file();
                    if (s.equals("empty")){
                        newFile();
                    }
                }
            } else if (k == 1){
                client.save("saveNewFile",file.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Сохранение прошло успешно!");
            } else{
                s  = client.addFile("addFile",file.getAbsolutePath());
                if (s.equals("No")){
                    JOptionPane.showMessageDialog(null, "Что-то пошло не так!");
                } else{
                    JOptionPane.showMessageDialog(null, "Добавление прошло успешно!");
                }
            }
            if (!s.equals("empty")){
                menu();
            }
        }
    }

    /**
     * Метод с меню
     */
    private void menu(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        JButton view = new JButton("Посмотреть меню");
        view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view();
            }
        });
        plain.add(view);

        JButton add = new JButton("Добавить данные");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        plain.add(add);

        JButton edit = new JButton("Редактировать меню");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    edit();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(edit);

        JButton search = new JButton("Поиск по меню");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        plain.add(search);

        JButton save = new JButton("Сохранить");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        plain.add(save);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод с меню для просмотра
     */
    private void view(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        JButton viewFull = new JButton("Все меню");
        viewFull.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewFullMenu();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(viewFull);

        JButton viewMC = new JButton("Меню по категориям");
        viewMC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewDishByCategory();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(viewMC);

        JButton viewCat = new JButton("Категории");
        viewCat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    viewCategory();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(viewCat);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод для просмотра всего меню
     */
    private void viewFullMenu() throws IOException {
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория", "Цена"};
        if (client.updateCheck()) update();
        String[] dish_arr = client.print("printDish").split("\\*");
        Object[][] array = new String[dish_arr.length/4][4];
        for (int i = 0; i < dish_arr.length/4;i++){
            for (int k = 0; k < 4; k ++){
                array[i][k] = dish_arr[i*4+k];
            }
        }
        Comparator comparator = new ComparatorMenu();
        sort(comparator,array);
        for (int i = 0; i < dish_arr.length/4;i++){
            array[i][0] = Integer.toString(i);
        }


        // Таблица с настройками
        JTable table = new JTable(array,columnsHeader){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,200));

        JButton edit = new JButton("Редактировать");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    edit();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view();
            }
        });
        plain.add(table_scroll);
        plain.add(edit);
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна для просмотра блюд по категориям
     */
    private void viewDishByCategory() throws IOException {
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        if (client.updateCheck()) update();
        String[] category = client.print("printCategory").split("\\*");

        JComboBox comboBox = new JComboBox(category);
        comboBox.setEditable(false);
        plain.add(comboBox);


        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория",
                "Цена"};
        Object[][] array = new String[0][0];
        DefaultTableModel model = new DefaultTableModel(array, columnsHeader);
        // Таблица с настройками
        JTable table = new JTable(model){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,200));
        plain.add(table_scroll);

        JButton ok = new JButton("Показать");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (client.updateCheck()) update();
                    String[] dish_arr = client.print("printDishByCategory",comboBox.getSelectedItem().toString()).split("\\*");
                    Object[][] arrayNew = new String[dish_arr.length/4][4];
                    for (int i = 0; i < dish_arr.length/4;i++){
                        for (int k = 0; k < 4; k ++){
                            arrayNew[i][k] = dish_arr[i*4+k];
                        }
                    }
                    Comparator comparator = new ComparatorMenu();
                    sort(comparator,arrayNew);
                    for (int i = 0; i < dish_arr.length/4;i++){
                        arrayNew[i][0] = Integer.toString(i);
                    }
                    model.setRowCount(0);
                    DefaultTableModel modelNew = (DefaultTableModel)table.getModel();
                    for (Object[] row : arrayNew) {
                        modelNew.addRow(row);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(ok);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               view();
            }
        });
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна для просмотра категорий
     */
    private void viewCategory() throws IOException {
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        Object[] columnsHeader = new String[] {"№","Категории"};
        if (client.updateCheck()) update();
        String[] category = client.print("printCategory").split("\\*");
        Object[][] array = new String[category.length][2];
        for (int i = 0; i < category.length; i++){
            array[i][0] = Integer.toString(i+1);
            array[i][1] = category[i];
        }
        Comparator comparator = new ComparatorMenu();
        sort(comparator,array);
        for (int i = 0; i < category.length/4;i++){
            array[i][0] = Integer.toString(i);
        }
        // Таблица с настройками
        JTable table = new JTable(array,columnsHeader){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,200));

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view();
            }
        });
        plain.add(table_scroll);
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна с меню добавления
     */
    private void add(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        JButton addDish = new JButton("Добавить блюдо");
        addDish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addDish();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(addDish);

        JButton addCategory = new JButton("Добавить категорию");
        addCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();

            }
        });
        plain.add(addCategory);

        JButton addFile = new JButton("Добавить данные из файла");
        addFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Выберите файл с данными");
                try {
                    download(2);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        plain.add(addFile);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна с добавлением блюд
     */
    private void addDish() throws IOException {
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));
        plain.setLayout(new BoxLayout(plain, BoxLayout.Y_AXIS));

        JTextField name = new JTextField(30);
        name.setFont(new Font("Dialog", Font.PLAIN, 20));
        name.setHorizontalAlignment(JTextField.LEFT);

        if (client.updateCheck()) update();
        String[] category = client.print("printCategory").split("\\*");

        JComboBox comboBox = new JComboBox(category);
        comboBox.setEditable(true);
        plain.add(comboBox);

        NumberFormat price =  new DecimalFormat("##0.###");
        JFormattedTextField numberField = new JFormattedTextField(new NumberFormatter(price));
        numberField.setFont(new Font("Dialog", Font.PLAIN, 20));
        numberField.setColumns(10);

        plain.add(new JLabel("Название категории :"));
        plain.add(name);
        plain.add(new JLabel("Категория :"));
        plain.add(comboBox);
        plain.add(new JLabel("Цена :"));
        plain.add(numberField);

        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               Dish dish = new Dish(name.getText(),
                       new Category(comboBox.getSelectedItem().toString()),
                       new Double(numberField.getValue().toString()));
                try {
                    if (client.updateCheck()) update();
                    String res = client.addData("addData",dish);
                    if (res.equals("Yes")){
                        JOptionPane.showMessageDialog(null, "Добавление прошло успешно");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Такое блюдо уже есть!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(add);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна с добавлением категорий
     */
    private void addCategory(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        JTextField name = new JTextField(25);
        name.setFont(new Font("Dialog", Font.PLAIN, 14));
        name.setHorizontalAlignment(JTextField.LEFT);


        plain.add(new JLabel("Название категории :"));
        plain.add(name);

        JButton add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (client.updateCheck()) update();
                    String res = client.addData("addCategory",name.getText());
                    if (res.equals("Yes")){
                        JOptionPane.showMessageDialog(null, "Добавление прошло успешно");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Такая категория уже есть!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(add);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна с поиском
     */
    private void search(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        plain.add(new Label("Введите запрос для поиска(Например, тор?ик*)"));
        JTextField search = new JTextField(25);
        search.setFont(new Font("Dialog", Font.PLAIN, 14));
        search.setHorizontalAlignment(JTextField.LEFT);
        plain.add(search);

        plain.add(new Label("Как искать: "));

        JComboBox comboBox = new JComboBox(new String[]{"по категории","по блюду"});
        comboBox.setEditable(false);
        plain.add(comboBox);


        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория",
                "Цена"};
        Object[][] array = new String[0][0];
        DefaultTableModel model = new DefaultTableModel(array, columnsHeader);
        // Таблица с настройками
        JTable table = new JTable(model){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,200));
        plain.add(table_scroll);

        JButton searchB = new JButton("Найти");
        searchB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (client.updateCheck()) update();
                    String method = "getDataByCategory";
                    if (comboBox.getSelectedIndex() == 1){
                        method = "getDataByName";
                    }
                    String[] dish_arr = client.print(method,search.getText()).split("\\*");
                    Object[][] arrayNew = new String[dish_arr.length/4][4];
                    for (int i = 0; i < dish_arr.length/4;i++){
                        for (int k = 0; k < 4; k ++){
                            arrayNew[i][k] = dish_arr[i*4+k];
                        }
                    }
                    Comparator comparator = new ComparatorMenu();
                    sort(comparator,arrayNew);
                    for (int i = 0; i < dish_arr.length/4;i++){
                        arrayNew[i][0] = Integer.toString(i);
                    }
                    model.setRowCount(0);
                    DefaultTableModel modelNew = (DefaultTableModel)table.getModel();
                    for (Object[] row : arrayNew) {
                        modelNew.addRow(row);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(searchB);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна с сохранением
     */
    private void save(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        JButton save = new JButton("сохранить");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (client.updateCheck()) update();
                    client.save("saveFile");
                    JOptionPane.showMessageDialog(null, "Сохранение прошло успешно!");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(save);

        JButton addCategory = new JButton("сохранить как копию");
        addCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Выберите файл для сохранения");
                try {
                    download(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(addCategory);

        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });

        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    /**
     * Метод окна с редактированием
     */
    private void edit() throws IOException {
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));

        Object[] columnsHeader = new String[] {"№","Блюдо", "Категория", "Цена"};
        if (client.updateCheck()) update();
        String ff =  client.print("printDish");
        String[] dish_arr =ff.split("\\*");
        Object[][] array = new String[dish_arr.length/4][4];
        Object[][] arrayCopy = new String[dish_arr.length/4][4];
        for (int i = 0; i < dish_arr.length/4;i++){
            for (int k = 0; k < 4; k ++){
                array[i][k] = dish_arr[i*4+k];
                arrayCopy[i][k] = dish_arr[i*4+k];
            }
        }
        Comparator comparator = new ComparatorMenu();
        sort(comparator,array);
        sort(comparator,arrayCopy);
        for (int i = 0; i < dish_arr.length/4;i++){
            array[i][0] = Integer.toString(i);
        }
        DefaultTableModel model = new DefaultTableModel(array, columnsHeader);
        // Таблица с настройками
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane table_scroll = new JScrollPane(table);
        table_scroll.setPreferredSize(new Dimension(500,200));
        plain.add(table_scroll);

        JButton edit = new JButton("Сохранить изменения");
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < dish_arr.length/4; i ++){
                        for (int k = 1; k < 4; k++){
                            if (!table.getValueAt(i,k).equals(arrayCopy[i][k])){
                                if (client.updateCheck()) update();
                                if (k == 1) {
                                    client.setData("setNameByName",arrayCopy[i][1].toString(), table.getValueAt(i,k).toString());
                                } else if (k == 2) {
                                    client.setData("setCategoryByName",arrayCopy[i][1].toString(), table.getValueAt(i,k).toString());
                                } else if (k == 3) {
                                    client.setData("setPriceByName",arrayCopy[i][1].toString(),Double.parseDouble(table.getValueAt(i,k).toString()));
                                }
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Изменение прошло успешно!");
                    frame.getContentPane().removeAll();
                    frame.getContentPane().invalidate();
                    edit();
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(edit);

        JButton discard = new JButton("Сбросить");
        discard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    edit();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(discard);

        JButton delete = new JButton("Удалить выделенную строку");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int idx = table.getSelectedRow();
                    model.removeRow(idx);
                    if (client.updateCheck()) update();
                    client.setData("deleteData",array[idx][1].toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        plain.add(delete);



        JButton cancel = new JButton("Назад");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu();
            }
        });
        plain.add(cancel);

        frame.getContentPane().removeAll();
        frame.getContentPane().invalidate();
        frame.getContentPane().add(plain);
        frame.getContentPane().revalidate();
    }

    public static void main (String [] args) throws IOException {
        ViewSwing windowApplication = new ViewSwing();
        client.stop();
    }

    /**
     * Метод сортировки для таблицы с блюдами или категориями по алфавиту
     * @param comparator - компаратор
     * @param o - объект, который будут сортировать
     */
    private static <T> void sort(Comparator<T> comparator, T... o){
        for (int i = 0; i < o.length; i++){
            for (int j = i+1; j < o.length; j++){
                if (comparator.compare(o[i],o[j]) < 0){
                    T temp = o[i];
                    o[i] = o[j];
                    o[j] = temp;
                }
            }
        }
    }

    public void update(){
        JPanel plain = new JPanel();
        plain.setBackground(new Color(176, 224, 230));
        plain.setLayout(new BoxLayout(plain, BoxLayout.PAGE_AXIS));
        plain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        plain.add(new Label("Файл был обновлен другим пользователем!" +
                "\nВы хотите продолжить работу с обноленным файлом?"));

        JButton yes = new JButton("Да");
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    client.newFile("openNewFile");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        JButton yesCopy = new JButton("Да, но сохранить мои изменения как копию");
        yesCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Выберите файл для сохранения");
                try {
                    download(1);
                    client.newFile("openNewFile");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton no = new JButton("Нет");
        no.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
            }
        });

        plain.add(yes);
        plain.add(yesCopy);
        plain.add(no);

        JOptionPane.showConfirmDialog(null, plain, null, JOptionPane.PLAIN_MESSAGE);
    }


}
