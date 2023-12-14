import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class cartIDManager {
    private static int cartID;

    public static int generateRandomCartID() {
        Random random = new Random();
        return 10000000 + random.nextInt(90000000);
    }

    public static int createCartID() {
        cartID = generateRandomCartID();
        return cartID;
    }

    public static int getCartID() {
        return cartID;
    }
}
