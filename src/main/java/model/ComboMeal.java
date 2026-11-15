package model;

import java.util.List;

/**
 * A combo meal is built from several dishes, but from the order's point of view
 * it is still one dish (one line on the receipt).
 * The builder pattern fills the parts, the price is a little cheaper than the sum.
 */
public class ComboMeal extends Dish {

    private final List<Dish> parts;

    public ComboMeal(String name, List<Dish> parts, double comboPrice) {
        super(name, comboPrice, sumCalories(parts));
        this.parts = parts;
    }

    public List<Dish> getParts() {
        return parts;
    }

    private static int sumCalories(List<Dish> parts) {
        int total = 0;
        for (Dish dish : parts) {
            total += dish.getCalories();
        }
        return total;
    }
}
