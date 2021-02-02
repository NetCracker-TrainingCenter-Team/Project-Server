package model;

import java.io.Serializable;

/**
 * Класс - модель блюд со свойствами <b>name</b>, <b>category</b>,</b> и <b>price</b>.
 */
public class Dish implements Serializable {
    /** Поле название блюда */
    private String name;
    /** Поле категория блюда {@link Category} */
    private Category category;
    /** Поле цена блюда */
    private double price;

    /**
     * Конструктор - создание нового объекта блюда с определенными значениями
     * @param name - имя блюда
     * @param category - категория
     * @param price - цена
     */
    public Dish(String name, Category category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
    /**
     * Конструктор - создание нового объекта категории без передаваемых значений
     */
    public Dish() {}
    /**
     * Метод получения значения поля name
     * @return возвращает название категории
     */
    public String getName() {
        return name;
    }
    /**
     * Метод определения названия блюда
     * @param name - название блюда
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Метод получения значения поля category
     * @return возвращает категорию {@link Category}
     */
    public Category getCategory() {
        return category;
    }
    /**
     * Метод определения категории
     * @param category - категория блюда
     */
    public void setCategory(Category category) {
        this.category = category;
    }
    /**
     * Метод получения значения поля price
     * @return возвращает цену
     */
    public double getPrice() {
        return price;
    }
    /**
     * Метод определения цены
     * @param price - цена блюда
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * Метод для получения строкового объект
     * @return строку описывающую блюдо
     */
    @Override
    public String toString() {
        return "Model.Dish{ " + "name: " + name  + ", category: " + category + ", price: " + price + '}';
    }

}