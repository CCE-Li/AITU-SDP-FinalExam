package model;

import java.util.ArrayList;
import java.util.List;

import visitor.OrderVisitor;

/**
 * One customer order. It only keeps a list of dishes,
 * the money and the calories are calculated by other classes.
 */
public class Order {

    private final int id;
    private final List<Dish> dishes = new ArrayList<>();

    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public int size() {
        return dishes.size();
    }

    /** Sum of all dish prices, before any discount. */
    public double getTotalPrice() {
        double total = 0;
        for (Dish dish : dishes) {
            total += dish.getPrice();
        }
        return total;
    }

    /** Visitor pattern: the order also accepts visitors. */
    public void accept(OrderVisitor visitor) {
        visitor.visit(this);
    }
}
