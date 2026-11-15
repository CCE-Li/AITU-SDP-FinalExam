package factory;

import model.Dish;

/** Creates burger dishes. */
public class BurgerFactory implements DishFactory {

    @Override
    public Dish create(String name) {
        switch (name) {
            case "Beef Burger":
                return new Dish("Beef Burger", 6.50, 550);
            case "Chicken Burger":
                return new Dish("Chicken Burger", 5.50, 480);
            case "Veggie Burger":
                return new Dish("Veggie Burger", 4.80, 390);
            default:
                throw new IllegalArgumentException("Unknown burger: " + name);
        }
    }

    @Override
    public String getCategory() {
        return "Burger";
    }
}
