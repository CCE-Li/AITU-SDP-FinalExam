package facade;

import model.Order;

/** Subsystem of the facade: the kitchen. */
public class Kitchen {

    public void cook(Order order) {
        System.out.println("  [Kitchen] cooking order #" + order.getId()
                + " (" + order.size() + " item(s))");
    }
}
