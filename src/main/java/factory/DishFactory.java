package factory;

import model.Dish;

/**
 * Factory Method pattern.
 * The client only knows this interface, every subclass decides
 * which concrete dish it creates.
 */
public interface DishFactory {

    Dish create(String name);

    String getCategory();
}
