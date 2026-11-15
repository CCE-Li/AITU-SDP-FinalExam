package bridge;

/**
 * Bridge pattern (implementor side).
 * The dining mode only cares about HOW the food is given to the customer,
 * it does not know if the food is a staple or a drink.
 */
public interface DiningMode {

    String getName();

    String serve(String foodName);

    double extraFee();
}
