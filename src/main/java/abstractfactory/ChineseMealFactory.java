package abstractfactory;

import model.Dish;

/** Chinese family: noodles + soy milk. */
public class ChineseMealFactory implements MealAbstractFactory {

    @Override
    public Dish createMainDish() {
        return new Dish("Beef Noodles", 4.50, 600);
    }

    @Override
    public Dish createDrink() {
        return new Dish("Soy Milk", 2.50, 120);
    }

    @Override
    public String getCuisine() {
        return "Chinese";
    }
}
