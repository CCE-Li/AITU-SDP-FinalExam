package builder;

import java.util.ArrayList;

import model.ComboMeal;
import model.Dish;

/**
 * Concrete builder: a combo meal costs 10% less than the sum of its parts.
 */
public class CustomMealBuilder extends MealBuilder {

    @Override
    public ComboMeal getMeal() {
        double sum = 0;
        for (Dish dish : parts) {
            sum += dish.getPrice();
        }
        double comboPrice = sum * 0.9;
        // copy the list so the next build does not change this meal
        return new ComboMeal(mealName, new ArrayList<>(parts), comboPrice);
    }
}
