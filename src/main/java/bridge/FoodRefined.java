package bridge;

import model.Dish;

/**
 * Bridge pattern (abstraction side).
 * A food has two independent dimensions: what it is (staple / drink)
 * and how it is served (dine-in / delivery). Both can grow separately.
 */
public abstract class FoodRefined {

    protected final Dish dish;
    protected final DiningMode mode;

    protected FoodRefined(Dish dish, DiningMode mode) {
        this.dish = dish;
        this.mode = mode;
    }

    /** Staple or drink - decided by the subclass. */
    public abstract String getFoodType();

    /** Something the waiter has to bring with this kind of food. */
    protected abstract String getServiceNote();

    /** The serving work is delegated to the dining mode. */
    public String serve() {
        return "[" + mode.getName() + "] " + mode.serve(dish.getName())
                + " (" + getServiceNote() + ")";
    }

    /** Food price + the fee of the dining mode. */
    public double getPrice() {
        return dish.getPrice() + mode.extraFee();
    }

    public Dish getDish() {
        return dish;
    }

    public DiningMode getMode() {
        return mode;
    }
}
