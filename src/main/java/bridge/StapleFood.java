package bridge;

import model.Dish;

/** A staple food, for example a burger or a pizza. */
public class StapleFood extends FoodRefined {

    public StapleFood(Dish dish, DiningMode mode) {
        super(dish, mode);
    }

    @Override
    public String getFoodType() {
        return "Staple";
    }

    @Override
    protected String getServiceNote() {
        return "cutlery included";
    }
}
