import java.util.Random;

public class cartIDManager {
    private static int cartID;

    // Method to generate a random cart ID
    public static int generateRandomCartID() {
        Random random = new Random();
        return 10000000 + random.nextInt(90000000);
    }

    // Method to create a new cart ID
    public static int createCartID() {
        cartID = generateRandomCartID();
        return cartID;
    }

    // Method to get the current cart ID
    public static int getCartID() {
        return cartID;
    }
}
