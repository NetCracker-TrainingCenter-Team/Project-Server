package controller;

import controller.itercollect.ItCategories;
import controller.itercollect.ItDishes;
import model.Category;
import model.Dish;

import java.io.IOException;
import java.util.List;

/**
 * Класс - контроллер
 */
public class Controller {
    /**
     * Метод изменения блюда по его номеру
     * @param numDish  - номер блюда по его положению в списке
     * @param dish - блюдо
     * @param dishes - список блюд
     * @throws IOException
     */
    public static void setDataByNumber(int numDish, Dish dish, List<Dish> dishes) {
        dishes.set(numDish, dish);
    }

    /**
     * Метод изменения блюда по его названию
     * @param name - название блюда
     * @param dish - блюдо
     * @param dishes - список блюд
     * @throws IOException
     */
    public static void setDataByName(String name, Dish dish, List<Dish> dishes) {
        int numDish = 0;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)) {
                dishes.set(numDish, dish);
                break;
            }
            numDish++;
        }
    }

    /**
     * Метод изменения категории блюда по названию
     * @param name - название блюда
     * @param category - категория блюда
     * @param dishes - список блюд
     * @throws IOException
     */
    public static void setCategoryByName(String name, Category category, List<Dish> dishes) {
        int numDish = 0;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)) {
                dishes.get(numDish).setCategory(category);
                break;
            }
            numDish++;
        }
    }

    /**
     * Метод изменения названия блюда по названию
     * @param name - название блюда
     * @param newName- новое название блюда
     * @param dishes - список блюд
     * @throws IOException
     */
    public static void setNameByName(String name, String newName, List<Dish> dishes) {
        int numDish = 0;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)) {
                dishes.get(numDish).setName(newName);
                break;
            }
            numDish++;
        }
    }
    /**
     * Метод изменения цены блюда по названию
     * @param name  - название блюда
     * @param price - цена блюда
     * @param dishes - список блюд
     * @throws IOException
     */
    public static void setPriceByName(String name, Double price, List<Dish> dishes) {
        int numDish = 0;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)) {
                dishes.get(numDish).setPrice(price);
                break;
            }
            numDish++;
        }
    }

    /**
     * Метод добавления блюда
     * @param dish  - блюдо
     * @param dishes - список блюд
     * @param categories - список категорий
     * @return значение истина или ложь
     * @throws IOException
     */
    public static boolean addData(Dish dish, List<Dish> dishes, List<Category> categories) {
        if (compareDishes(dishes, dish)) {
            dishes.add(dish);
            addCategory(dish.getCategory(), categories);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод добавления категорий из файла
     * @param dishes - список блюд
     * @param categories - список категорий
     * @return значение истина или ложь
     * @throws IOException
     */
    public static void addCategoryByDish(List<Dish> dishes, List<Category> categories) {
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            addCategory(iterator.next().getCategory(), categories);
        }
    }

    /**
     * Метод удаления блюда по его названию
     * @param name - название блюда
     * @param dishes - список блюд
     * @throws IOException
     */
    public static void deleteData(String name, List<Dish> dishes) {
        int numDish = 0;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(name)) {
                dishes.remove(numDish);
                break;
            }
            numDish++;
        }
    }

    /**
     * Метод просмотра данных
     * @param dishes - список блюд
     * @return возвращает строку содержащую список блюд
     * @throws IOException
     */
    public static StringBuffer printDish (List<Dish> dishes) {
        StringBuffer resultString = new StringBuffer();
        int numDish = 1;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            Dish dish= iterator.next();
            resultString.append(numDish + "*" + dish.getName() + "*"
                    + dish.getCategory().getNameCategory() + "*"
                    + dish.getPrice() + "*");
            numDish++;
        }
        return resultString;
    }

    /**
     * Метод проверки данных
     * @param dish - блюдо одного из файлов
     * @param otherDish - блюдо из другого файла
     * @return возвращает значение истина или ложь
     */
    private static boolean compareDish(Dish dish, Dish otherDish) {
        if (dish.getName().equals(otherDish.getName())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Метод проверки данных из другого файла
     * @param dishes - меню одного из файлов
     * @param dish - блюдо из другого файла
     * @return возвращает значение истина или ложь
     */
    private static boolean compareDishes(List<Dish> dishes, Dish dish) {
        boolean bool = true;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext() && bool) {
            bool = compareDish(iterator.next(), dish);
        }
        return bool;
    }

    /**
     * Метод добавления данных из другого файла
     * @param dishes - список блюд
     * @param otherDishes - список блюд из другого файла
     * @param categories - список категорий из исходного файла
     * @return возвращает значение истина или ложь
     * @throws IOException
     */
    public static boolean addFile(List<Dish> dishes, List<Dish> otherDishes, List<Category> categories) {
        if (dishes.size() != 0 && otherDishes.size() != 0) {
            boolean changeTest = false;
            ItDishes iterator=new ItDishes(otherDishes);
            while (iterator.hasNext()) {
                if (addData(iterator.next(),dishes,categories)) {
                    changeTest = true;
                }
            }
            return changeTest;
        } else {
            if (otherDishes.size() != 0) {
                dishes = otherDishes;
                return true;
            }
            return false;
        }
    }

    /**
     * Метод проверки данных
     * @param category - категория
     * @param otherCategory - категория
     * @return возвращает значение истина или ложь
     */
    private static boolean compareCategory(Category category, Category otherCategory) {
        if (category.getNameCategory().equals(otherCategory.getNameCategory())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Метод проверки данных из другого файла
     * @param categories - лист категорий
     * @param category - категория
     * @return возвращает значение истина или ложь
     */
    public static boolean compareCategories(List<Category> categories, Category category) {
        boolean changeTest = true;
        ItCategories iterator=new ItCategories(categories);
        while (iterator.hasNext() && changeTest) {
            changeTest = compareCategory(iterator.next(), category);
        }
        return changeTest;
    }

    /**
     * Метод добавления категории
     * @param category - категория
     * @param categories - список категорий
     * @return возвращает значение истина или ложь
     * @throws IOException
     */
    public static boolean addCategory(Category category, List<Category> categories) {
        if (compareCategories(categories, category)) {
            categories.add(category);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод просмотра данных по категории
     * @param category - название категории
     * @param dishes - список блюд
     * @return возвращает строку содержащую список блюд
     * @throws IOException
     */
    public static StringBuffer printDishByCategory(String category, List<Dish> dishes) {
        StringBuffer resultString = new StringBuffer();
        int numDish = 1;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            Dish dish= iterator.next();
            if (dish.getCategory().getNameCategory().equals(category)) {
                resultString.append(numDish + "*" + dish.getName() + "*"
                        + dish.getCategory().getNameCategory() + "*"
                        + dish.getPrice() + "*");
                numDish++;
            }
        }
        return resultString;
    }

    /**
     * Метод просмотра категорий
     * @param categories - список категорий
     * @return возвращает строку содержащую список категорий
     * @throws IOException
     */
    public static StringBuffer printCategory(List<Category> categories) {
        StringBuffer resultString = new StringBuffer();
        ItCategories iterator=new ItCategories(categories);
        while (iterator.hasNext()) {
            resultString.append(iterator.next().getNameCategory() + "*");
        }
        return resultString;
    }

    /**
     * Метод поиска данных по названию
     * @param name - название блюда
     * @param dishes - список блюд
     * @return возвращает строку, содержащую все блюда, подходящие под шаблон названия
     * @throws IOException
     */
    public static StringBuffer getDataByName(String name, List<Dish> dishes) {
        StringBuffer resultString = new StringBuffer();
        char[] arrayName = name.toCharArray();
        int numDish = 1;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            int numArrayName = 0, numDishName = 0;
            boolean checkEqual = false;
            Dish dish= iterator.next();
            while (numArrayName < arrayName.length) {
                if (arrayName[numArrayName] == '*' || arrayName[numArrayName] == '?') {
                    numArrayName++;
                } else {
                    checkEqual = false;
                    while (numDishName < dish.getName().length() && !checkEqual) {
                        char[] d = dish.getName().toCharArray();
                        if (arrayName[numArrayName] == d[numDishName]) {
                            checkEqual = true;
                        }
                        numDishName++;
                    }
                    numArrayName++;
                }
            }
            if (checkEqual) {
                resultString.append(numDish + "*" + dish.getName() + "*"
                        + dish.getCategory().getNameCategory() + "*"
                        + dish.getPrice() + '*');
                numDish++;
            }
        }
        return resultString;
    }

    /**
     * Метод поиска данных по категории
     * @param category - название категории
     * @param dishes - список блюд
     * @return возвращает строку, содержащую все блюда, подходящие под шаблон категории
     * @throws IOException
     */
    public static StringBuffer getDataByCategory(String category, List<Dish> dishes) {
        StringBuffer resultString = new StringBuffer();
        char[] arrayCategoryName = category.toCharArray();
        int numDish = 1;
        ItDishes iterator=new ItDishes(dishes);
        while (iterator.hasNext()) {
            int numArrayCategoryName = 0, numDishCategory = 0;
            boolean bool = false;
            Dish dish=iterator.next();
            while (numArrayCategoryName < arrayCategoryName.length) {
                if (arrayCategoryName[numArrayCategoryName] == '*' || arrayCategoryName[numArrayCategoryName] == '?') {
                    numArrayCategoryName++;
                } else {
                    bool = false;
                    while (numDishCategory < dish.getCategory().getNameCategory().length() && !bool) {
                        char[] d = dish.getCategory().getNameCategory().toCharArray();
                        if (arrayCategoryName[numArrayCategoryName] == d[numDishCategory]) {
                            bool = true;
                        }
                        numDishCategory++;
                    }
                    numArrayCategoryName++;
                }
            }
            if (bool) {
                resultString.append(numDish + "*" + dish.getName() + "*"
                        + dish.getCategory().getNameCategory() + "*"
                        + dish.getPrice() + '*');
                numDish++;
            }
        }
        return resultString;
    }

}

