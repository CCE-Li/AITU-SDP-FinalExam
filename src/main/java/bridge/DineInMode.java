package bridge;

/** Eat in the restaurant, no extra fee. */
public class DineInMode implements DiningMode {

    @Override
    public String getName() {
        return "Dine-in";
    }

    @Override
    public String serve(String foodName) {
        return foodName + " is served on table 12";
    }

    @Override
    public double extraFee() {
        return 0.0;
    }
}
