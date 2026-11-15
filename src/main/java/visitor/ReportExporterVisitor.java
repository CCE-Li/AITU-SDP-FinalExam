package visitor;

import model.Dish;
import model.Order;

/**
 * Visitor pattern: export the order as a simple text receipt.
 * It also reuses the calorie visitor, so the two visitors work together.
 */
public class ReportExporterVisitor implements OrderVisitor {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visit(Dish dish) {
        report.append(String.format("  %-22s $%6.2f  %4d kcal\n",
                dish.getName(), dish.getPrice(), dish.getCalories()));
    }

    @Override
    public void visit(Order order) {
        report.setLength(0);
        report.append("----- ORDER RECEIPT -----\n");
        report.append("Order no: ").append(order.getId()).append("\n");
        for (Dish dish : order.getDishes()) {
            dish.accept(this);
        }

        CalorieCalculatorVisitor calories = new CalorieCalculatorVisitor();
        order.accept(calories);

        report.append("-------------------------\n");
        report.append(String.format("Items: %d   Subtotal: $%.2f   Calories: %d\n",
                order.size(), order.getTotalPrice(), calories.getTotalCalories()));
    }

    public String getReport() {
        return report.toString();
    }
}
