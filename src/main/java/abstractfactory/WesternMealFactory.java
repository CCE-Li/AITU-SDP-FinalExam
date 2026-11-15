package abstractfactory;

import model.Dish;

/** Western family: beef burger + coke. */
public class WesternMealFactory implements MealAbstractFactory {

    @Override
    public Dish createMainDish() {
        return new Dish("Beef Burger", 6.50, 550);
    }

    @Override
    public Dish createDrink() {
        return new Dish("Coke", 2.00, 150);
    }

    @Override
    public String getCuisine() {
        return "Western";
    }
}
