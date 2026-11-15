package bridge;

/** Take away / delivery, delivery costs 2.00 extra. */
public class DeliveryMode implements DiningMode {

    private static final double DELIVERY_FEE = 2.00;

    @Override
    public String getName() {
        return "Delivery";
    }

    @Override
    public String serve(String foodName) {
        return foodName + " is packed and sent to the customer";
    }

    @Override
    public double extraFee() {
        return DELIVERY_FEE;
    }
}
