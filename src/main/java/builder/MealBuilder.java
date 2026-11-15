package builder;

import java.util.ArrayList;
import java.util.List;

import model.ComboMeal;
import model.Dish;

/**
 * Builder pattern (abstract builder).
 * It collects the parts of a combo meal step by step,
 * the concrete builder decides how the final meal is built.
 */
public abstract class MealBuilder {

    protected String mealName = "Custom Combo";
    protected final List<Dish> parts = new ArrayList<>();

    public void reset() {
        parts.clear();
        mealName = "Custom Combo";
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void addMain(Dish dish) {
        parts.add(dish);
    }

    public void addSide(Dish dish) {
        parts.add(dish);
    }

    public void addDrink(Dish dish) {
        parts.add(dish);
    }

    public abstract ComboMeal getMeal();
}
