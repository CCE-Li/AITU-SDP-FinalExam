package facade;

/** Subsystem of the facade: sends a message to the customer. */
public class Notifier {

    public void notifyCustomer(String message) {
        System.out.println("  [Notify] " + message);
    }
}
