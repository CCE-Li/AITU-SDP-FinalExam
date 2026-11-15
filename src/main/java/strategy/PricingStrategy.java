package strategy;

/**
 * Strategy pattern.
 * Every discount rule is a strategy, the cashier can switch it at runtime
 * without changing the order or the facade code.
 */
public interface PricingStrategy {

    /** Turn the raw order total into the price the customer has to pay. */
    double calculate(double total);

    String getName();
}
