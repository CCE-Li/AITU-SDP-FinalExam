package model;

import visitor.OrderVisitor;

/**
 * Base class of every food in the restaurant.
 * A dish only stores the basic data: name, price and calories.
 */
public class Dish {

    private final String name;
    private final double price;
    private final int calories;

    public Dish(String name, double price, int calories) {
        this.name = name;
        this.price = price;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    /** Visitor pattern: let a visitor do its own job on this dish. */
    public void accept(OrderVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%-22s $%5.2f  %4d kcal", name, price, calories);
    }
}
