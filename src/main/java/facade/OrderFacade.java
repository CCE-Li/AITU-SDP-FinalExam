package facade;

import bridge.DiningMode;
import model.Order;
import strategy.PricingStrategy;

/**
 * Facade pattern.
 * The front desk only calls placeOrder(), it does not need to know
 * about the stock, the payment gateway, the kitchen or the notifier.
 * It also handles the rollback when one step fails.
 */
public class OrderFacade {

    private final Inventory inventory = new Inventory();
    private final PaymentService payment = new PaymentService();
    private final Kitchen kitchen = new Kitchen();
    private final Notifier notifier = new Notifier();

    public void addStock(String dishName, int amount) {
        inventory.addStock(dishName, amount);
    }

    public void printStock() {
        inventory.printStock();
    }

    public void setPaymentLimit(double limit) {
        payment.setLimit(limit);
    }

    /**
     * One call does the whole work: stock -> payment -> kitchen -> notify.
     * If the payment fails, the reserved stock is given back (rollback).
     *
     * @return true when the order is accepted
     */
    public boolean placeOrder(Order order, PricingStrategy strategy, DiningMode mode) {
        System.out.println("--- order #" + order.getId() + " | " + strategy.getName()
                + " | " + mode.getName() + " ---");

        // 1. check and reserve the stock
        if (!inventory.reserve(order.getDishes())) {
            notifier.notifyCustomer("Sorry, some dishes are sold out, order #"
                    + order.getId() + " is cancelled.");
            return false;
        }

        // 2. pay (the discount is calculated by the strategy, delivery fee is added once)
        double subtotal = order.getTotalPrice();
        double finalPrice = strategy.calculate(subtotal) + mode.extraFee();
        System.out.println("  [Cashier] subtotal $" + String.format("%.2f", subtotal)
                + " -> to pay $" + String.format("%.2f", finalPrice));

        if (!payment.pay(order.getId(), finalPrice)) {
            inventory.release(order.getDishes());   // rollback
            notifier.notifyCustomer("Payment failed, the stock of order #"
                    + order.getId() + " has been released.");
            return false;
        }

        // 3. the kitchen starts cooking
        kitchen.cook(order);

        // 4. tell the customer
        notifier.notifyCustomer("Order #" + order.getId() + " confirmed, total $"
                + String.format("%.2f", finalPrice) + " (" + mode.getName() + ").");
        return true;
    }
}
