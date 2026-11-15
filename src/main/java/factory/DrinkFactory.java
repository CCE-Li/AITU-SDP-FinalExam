package factory;

import model.Dish;

/** Creates drinks. */
public class DrinkFactory implements DishFactory {

    @Override
    public Dish create(String name) {
        switch (name) {
            case "Coke":
                return new Dish("Coke", 2.00, 150);
            case "Soy Milk":
                return new Dish("Soy Milk", 2.50, 120);
            case "Orange Juice":
                return new Dish("Orange Juice", 3.00, 110);
            case "Water":
                return new Dish("Water", 1.00, 0);
            default:
                throw new IllegalArgumentException("Unknown drink: " + name);
        }
    }

    @Override
    public String getCategory() {
        return "Drink";
    }
}
