package strategy;

/** VIP member: 20% off the whole order. */
public class VipDiscountStrategy implements PricingStrategy {

    private static final double DISCOUNT = 0.8;

    @Override
    public double calculate(double total) {
        return total * DISCOUNT;
    }

    @Override
    public String getName() {
        return "VIP 20% off";
    }
}
