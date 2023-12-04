import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class main {

    public static void main(String[] args) {

        String[] test = {"milk", "bread", "fish"};
        for (int i = 0; i < test.length; i++){
            cart.addToCart(test[i]);
        }
        

    }
}