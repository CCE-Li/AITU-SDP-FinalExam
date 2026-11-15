package strategy;

/** Normal customer: no discount. */
public class NormalPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(double total) {
        return total;
    }

    @Override
    public String getName() {
        return "Normal price";
    }
}
