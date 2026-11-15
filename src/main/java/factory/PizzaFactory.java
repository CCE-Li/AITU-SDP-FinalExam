package factory;

import model.Dish;

/** Creates pizza dishes. */
public class PizzaFactory implements DishFactory {

    @Override
    public Dish create(String name) {
        switch (name) {
            case "Pepperoni Pizza":
                return new Dish("Pepperoni Pizza", 8.90, 820);
            case "Cheese Pizza":
                return new Dish("Cheese Pizza", 7.50, 700);
            case "Seafood Pizza":
                return new Dish("Seafood Pizza", 10.50, 760);
            default:
                throw new IllegalArgumentException("Unknown pizza: " + name);
        }
    }

    @Override
    public String getCategory() {
        return "Pizza";
    }
}
