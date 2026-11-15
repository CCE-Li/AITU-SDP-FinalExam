package visitor;

import model.Dish;
import model.Order;

/**
 * Visitor interface.
 * Every new report (calories, receipt, statistics...) is a new visitor class,
 * so the model classes never have to change.
 */
public interface OrderVisitor {

    void visit(Dish dish);

    void visit(Order order);
}
