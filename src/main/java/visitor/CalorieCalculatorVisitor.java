package visitor;

import model.Dish;
import model.Order;

/**
 * Visitor pattern: count the calories of a whole order.
 * The order and the dishes do not know anything about calories reports.
 */
public class CalorieCalculatorVisitor implements OrderVisitor {

    private int totalCalories;

    @Override
    public void visit(Dish dish) {
        totalCalories += dish.getCalories();
    }

    @Override
    public void visit(Order order) {
        totalCalories = 0;
        for (Dish dish : order.getDishes()) {
            dish.accept(this);
        }
    }

    public int getTotalCalories() {
        return totalCalories;
    }
}
