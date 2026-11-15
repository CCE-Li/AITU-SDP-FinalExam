package abstractfactory;

import model.Dish;

/**
 * Abstract Factory pattern.
 * One factory creates a whole family of dishes of the same cuisine,
 * so the client never mixes a Chinese main dish with a Western drink.
 */
public interface MealAbstractFactory {

    Dish createMainDish();

    Dish createDrink();

    String getCuisine();
}
