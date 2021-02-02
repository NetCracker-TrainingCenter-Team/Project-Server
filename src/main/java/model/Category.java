package model;

import java.io.Serializable;

/** Класс модель - категорий блюд со свойством <b>name</b>.
 */
public class Category implements Serializable {
    /** Поле название категории */
    private String nameCategory;

    /**
     * Конструктор - создание нового объекта категории с определенными значениями
     * @param nameCategory - имя категории
     */
    public Category(String nameCategory) {
        this.nameCategory = nameCategory;
    }
    /**
     * Конструктор - создание нового объекта категории без передаваемых значений
     */
    public Category() { }
    /**
     * Функция получения значения поля имя категории
     * @return возвращает название категории
     */
    public String getNameCategory() {
        return nameCategory;
    }
    /**
     * Процедура определения категории
     * @param nameCategory - название категории
     */
    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    /**
     * Метод для получения строкового объект
     * @return строку описывающую категорию
     */
    @Override
    public String toString() {
        return "Model.Category{" + "nameCategory: " + nameCategory + '}';
    }


}