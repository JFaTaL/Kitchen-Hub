import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class boughtItemsTable {
    public static Hashtable<String, Date> boughtItemsTable = new Hashtable<>();
    public static Scanner input = new Scanner(System.in);
    public static SimpleDateFormat dateSQLType = new SimpleDateFormat("yyyy-MM-dd");
    
    
    public static Hashtable createTable(LinkedList<String> product, LinkedList<Date> expiration) {
        
        for (int i = 0; i < product.size(); i++) {
            boughtItemsTable.put(product.get(i), expiration.get(i));
        }
        return boughtItemsTable;
    } 
    
    public static void main(String[] args) {
        groceryCart cart = new groceryCart();

        String[] test = {"milk", "bread", "fish"};
        LinkedList<Date> testExpDates = new LinkedList<>();
        for (int i = 0; i < test.length; i++){
            String dateInput = input.next();
            Date date = Date.valueOf(dateInput);
            cart.addToCart(test[i]);
            testExpDates.add(date);
        }

        LinkedList<String> testCart = cart.GetCart();

        
        cart.printCart();
        Hashtable<String, Date> testTable = new Hashtable<>();
        testTable = createTable(testCart, testExpDates);
        System.out.println(testTable);

    }

    

}