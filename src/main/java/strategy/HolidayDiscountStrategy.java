package strategy;

/**
 * Holiday promotion (full reduction):
 * spend 50 or more, save 10; spend 30 or more, save 5.
 */
public class HolidayDiscountStrategy implements PricingStrategy {

    @Override
    public double calculate(double total) {
        double result = total;
        if (total >= 50) {
            result = total - 10;
        } else if (total >= 30) {
            result = total - 5;
        }
        return Math.max(result, 0);
    }

    @Override
    public String getName() {
        return "Holiday full reduction";
    }
}
