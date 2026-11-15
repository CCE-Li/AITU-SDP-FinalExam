import abstractfactory.ChineseMealFactory;
import abstractfactory.MealAbstractFactory;
import abstractfactory.WesternMealFactory;
import bridge.DeliveryMode;
import bridge.DineInMode;
import bridge.DiningMode;
import bridge.DrinkItem;
import bridge.FoodRefined;
import bridge.StapleFood;
import builder.CustomMealBuilder;
import builder.MealDirector;
import facade.OrderFacade;
import factory.BurgerFactory;
import factory.DishFactory;
import factory.DrinkFactory;
import factory.PizzaFactory;
import model.ComboMeal;
import model.Dish;
import model.Order;
import strategy.HolidayDiscountStrategy;
import strategy.NormalPricingStrategy;
import strategy.PricingStrategy;
import strategy.VipDiscountStrategy;
import visitor.CalorieCalculatorVisitor;
import visitor.ReportExporterVisitor;

/**
 * Client of the restaurant system.
 * It goes through the 7 design patterns one by one,
 * at the end everything is used together to place a real order.
 */
public class Main {

    public static void main(String[] args) {

        // ---------- 1. Factory Method ----------
        title("1. Factory Method: create dishes");

        DishFactory burgerFactory = new BurgerFactory();
        DishFactory pizzaFactory = new PizzaFactory();
        DishFactory drinkFactory = new DrinkFactory();

        Dish beefBurger = burgerFactory.create("Beef Burger");
        Dish veggieBurger = burgerFactory.create("Veggie Burger");
        Dish pepperoni = pizzaFactory.create("Pepperoni Pizza");
        Dish coke = drinkFactory.create("Coke");
        Dish water = drinkFactory.create("Water");

        System.out.println(beefBurger);
        System.out.println(veggieBurger);
        System.out.println(pepperoni);
        System.out.println(coke);
        System.out.println(water);

        // ---------- 2. Abstract Factory ----------
        title("2. Abstract Factory: Chinese / Western meal family");

        MealAbstractFactory chinese = new ChineseMealFactory();
        MealAbstractFactory western = new WesternMealFactory();

        System.out.println(chinese.getCuisine() + " main: " + chinese.createMainDish());
        System.out.println(chinese.getCuisine() + " drink: " + chinese.createDrink());
        System.out.println(western.getCuisine() + " main: " + western.createMainDish());
        System.out.println(western.getCuisine() + " drink: " + western.createDrink());

        // ---------- 3. Builder ----------
        title("3. Builder: build combo meals");

        MealDirector director = new MealDirector(new CustomMealBuilder());

        ComboMeal classic = director.buildClassicCombo();
        ComboMeal light = director.buildLightCombo();
        // the abstract factory gives the parts, the builder puts them into a meal
        ComboMeal chineseSet = director.buildCustomCombo("Chinese Set",
                chinese.createMainDish(), new Dish("Pickles", 1.00, 30), chinese.createDrink());

        System.out.println(classic);
        System.out.println(light);
        System.out.println(chineseSet);

        // ---------- 4. Bridge ----------
        title("4. Bridge: food type x dining mode");

        DiningMode dineIn = new DineInMode();
        DiningMode delivery = new DeliveryMode();

        FoodRefined[] foods = {
                new StapleFood(beefBurger, dineIn),
                new StapleFood(beefBurger, delivery),
                new DrinkItem(coke, dineIn),
                new DrinkItem(coke, delivery)
        };
        for (FoodRefined food : foods) {
            System.out.println(food.getFoodType() + " | " + food.serve()
                    + " -> $" + String.format("%.2f", food.getPrice()));
        }

        // ---------- 5. Visitor ----------
        title("5. Visitor: receipt and calories");

        Order order = new Order(1001);
        order.addDish(classic);
        order.addDish(pepperoni);
        order.addDish(coke);

        ReportExporterVisitor report = new ReportExporterVisitor();
        order.accept(report);
        System.out.print(report.getReport());

        CalorieCalculatorVisitor calories = new CalorieCalculatorVisitor();
        order.accept(calories);
        System.out.println("Total calories of order #1001: " + calories.getTotalCalories());

        // ---------- 6. Strategy ----------
        title("6. Strategy: price for different customers");

        PricingStrategy[] strategies = {
                new NormalPricingStrategy(),
                new VipDiscountStrategy(),
                new HolidayDiscountStrategy()
        };
        for (PricingStrategy strategy : strategies) {
            System.out.println(String.format("%-26s $%.2f",
                    strategy.getName(), strategy.calculate(order.getTotalPrice())));
        }

        // ---------- 7. Facade ----------
        title("7. Facade: place the order");

        OrderFacade frontDesk = new OrderFacade();
        frontDesk.addStock("Beef Burger", 5);
        frontDesk.addStock("Fries", 5);
        frontDesk.addStock("Coke", 5);
        frontDesk.addStock("Pepperoni Pizza", 2);
        frontDesk.addStock("Veggie Burger", 3);
        frontDesk.addStock("Salad", 3);
        frontDesk.addStock("Water", 5);
        frontDesk.addStock("Beef Noodles", 3);
        frontDesk.addStock("Soy Milk", 5);
        frontDesk.addStock("Pickles", 5);

        frontDesk.printStock();
        boolean placed = frontDesk.placeOrder(order, new VipDiscountStrategy(), delivery);
        System.out.println("Order #1001 accepted: " + placed);
        frontDesk.printStock();

        // ---------- 8. Facade rollback ----------
        title("8. Facade: payment fails, stock goes back");

        Order second = new Order(1002);
        second.addDish(veggieBurger);
        second.addDish(new Dish("Salad", 2.20, 90));

        frontDesk.setPaymentLimit(5.00);   // make the payment fail on purpose
        boolean placedAgain = frontDesk.placeOrder(second, new NormalPricingStrategy(), dineIn);
        System.out.println("Order #1002 accepted: " + placedAgain);
        frontDesk.printStock();

        title("Done");
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("======================================================");
        System.out.println(text);
        System.out.println("======================================================");
    }
}
