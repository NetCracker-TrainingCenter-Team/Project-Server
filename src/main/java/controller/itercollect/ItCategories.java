package controller.itercollect;

import model.Category;

import java.util.Iterator;
import java.util.List;

public class ItCategories implements Iterator<Category> {
    private List<Category> categories;
    private int index;
    public ItCategories(List<Category> categories){
        this.categories=categories;
    }
    @Override
    public boolean hasNext() {
        if(index<categories.size()){
            return true;
        }
        return false;
    }

    @Override
    public Category next() {
        return categories.get(index++);
    }

    @Override
    public void remove() {

    }
}
