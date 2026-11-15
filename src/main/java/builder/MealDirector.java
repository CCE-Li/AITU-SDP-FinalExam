package builder;

import factory.BurgerFactory;
import factory.DishFactory;
import factory.DrinkFactory;
import model.ComboMeal;
import model.Dish;

/**
 * Director: it knows the order of the build steps and the recipes,
 * but it does not know how the meal object is created.
 */
public class MealDirector {

    private final MealBuilder builder;

    public MealDirector(MealBuilder builder) {
        this.builder = builder;
    }

    /** Classic recipe: beef burger + fries + coke. */
    public ComboMeal buildClassicCombo() {
        DishFactory burgers = new BurgerFactory();
        DishFactory drinks = new DrinkFactory();

        builder.reset();
        builder.setMealName("Classic Combo");
        builder.addMain(burgers.create("Beef Burger"));
        builder.addSide(new Dish("Fries", 2.00, 320));
        builder.addDrink(drinks.create("Coke"));
        return builder.getMeal();
    }

    /** Light recipe: veggie burger + salad + water. */
    public ComboMeal buildLightCombo() {
        DishFactory burgers = new BurgerFactory();
        DishFactory drinks = new DrinkFactory();

        builder.reset();
        builder.setMealName("Light Combo");
        builder.addMain(burgers.create("Veggie Burger"));
        builder.addSide(new Dish("Salad", 2.20, 90));
        builder.addDrink(drinks.create("Water"));
        return builder.getMeal();
    }

    /** The customer chooses every part by himself. */
    public ComboMeal buildCustomCombo(String name, Dish main, Dish side, Dish drink) {
        builder.reset();
        builder.setMealName(name);
        builder.addMain(main);
        builder.addSide(side);
        builder.addDrink(drink);
        return builder.getMeal();
    }
}
