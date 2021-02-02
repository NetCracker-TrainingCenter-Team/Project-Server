package controller.itercollect;

import model.Dish;

import java.util.Iterator;
import java.util.List;

public class ItDishes implements Iterator<Dish> {
    private List<Dish> dishes;
    private int index;
    public ItDishes(List<Dish> dishes){
        this.dishes=dishes;
    }
    @Override
    public boolean hasNext() {
        if(index<dishes.size()){
            return true;
        }
        return false;
    }

    @Override
    public Dish next() {
        return dishes.get(index++);
    }

    @Override
    public void remove() {

    }
}
