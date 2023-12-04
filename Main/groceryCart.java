import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class groceryCart {
    public LinkedList<String> cart = new LinkedList<>();
    public groceryCart() {}

    public void addToCart(String prodsToAdd) {
            cart.add(prodsToAdd);
        }

    }

    public static void printCart() {
        System.out.println();
    }


}