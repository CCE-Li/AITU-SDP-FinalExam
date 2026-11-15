package bridge;

import model.Dish;

/** A drink, for example coke or soy milk. */
public class DrinkItem extends FoodRefined {

    public DrinkItem(Dish dish, DiningMode mode) {
        super(dish, mode);
    }

    @Override
    public String getFoodType() {
        return "Drink";
    }

    @Override
    protected String getServiceNote() {
        return "straw included";
    }
}
