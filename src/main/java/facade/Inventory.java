package facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ComboMeal;
import model.Dish;

/**
 * Subsystem of the facade: the stock of the restaurant.
 * A combo meal is counted by its parts, because the kitchen uses the parts.
 */
public class Inventory {

    private final Map<String, Integer> stock = new HashMap<>();

    public void addStock(String dishName, int amount) {
        stock.merge(dishName, amount, Integer::sum);
    }

    public int getStock(String dishName) {
        return stock.getOrDefault(dishName, 0);
    }

    /**
     * Take the dishes out of the stock.
     * Check every dish first, so a failed order never takes half of the stock.
     */
    public boolean reserve(List<Dish> dishes) {
        List<Dish> items = expand(dishes);
        for (Dish dish : items) {
            if (getStock(dish.getName()) <= 0) {
                return false;
            }
        }
        for (Dish dish : items) {
            stock.put(dish.getName(), getStock(dish.getName()) - 1);
        }
        return true;
    }

    /** Rollback: put the reserved dishes back into the stock. */
    public void release(List<Dish> dishes) {
        for (Dish dish : expand(dishes)) {
            stock.merge(dish.getName(), 1, Integer::sum);
        }
    }

    public void printStock() {
        System.out.println("  [Stock] " + stock);
    }

    private List<Dish> expand(List<Dish> dishes) {
        List<Dish> items = new ArrayList<>();
        for (Dish dish : dishes) {
            if (dish instanceof ComboMeal combo) {
                items.addAll(combo.getParts());
            } else {
                items.add(dish);
            }
        }
        return items;
    }
}
