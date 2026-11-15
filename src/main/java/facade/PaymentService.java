package facade;

/**
 * Subsystem of the facade: a very simple payment gateway.
 * The limit can be changed to show what happens when the payment fails.
 */
public class PaymentService {

    private double limit = 100.00;

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public boolean pay(int orderId, double amount) {
        if (amount > limit) {
            System.out.println("  [Payment] order #" + orderId + " refused: $"
                    + String.format("%.2f", amount) + " is over the card limit");
            return false;
        }
        System.out.println("  [Payment] order #" + orderId + " paid $"
                + String.format("%.2f", amount));
        return true;
    }
}
