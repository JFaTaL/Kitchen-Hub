import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class main{
    public static void main(String[] args) {
        cartIDManager cartIDManager = new cartIDManager();
        groceryCart groceryCart = new groceryCart();
        Scanner scanner = new Scanner(System.in);

        groceryCart.createNewCart();

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Search for products");
            System.out.println("2. View Cart");
            System.out.println("3. Confirm Purchased Items");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1/2/3/4): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            
            switch (choice) {
                case 1:
                    groceryCart.displaySearchOptions();
                    break;

                case 2:
                    // View Cart
                    System.out.println("Your current cart:");
                    groceryCart.printCart();
                    break;

                case 3:
                    // Confirm Purchased Items
                    boughtItemsTable boughtItemsTable = new boughtItemsTable();
                    boughtItemsTable.confirmPurchasedItems(groceryCart);
                    boughtItemsTable.displayPurchasedItems();
                    boughtItemsTable.savePurchasedItems(Integer.toString(cartIDManager.getCartID()));
                    // Save the cart to the database
                    LinkedList<String> cartItemsList = new LinkedList<>(boughtItemsTable.quantities.keySet());
                    String cartItems = boughtItemsTable.serializeCart(cartItemsList);
                    int cartID = cartIDManager.getCartID();
                    boughtItemsTable.savePurchasedItems(cartID, boughtItemsTable);
                    groceryCart.getCart().clear();
                    groceryCart.createNewCart();
                    break;

                case 4:
                    // Exit
                    System.out.println("Exiting the application.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please choose 1, 2, 3, or 4.");
            }

        }
    }
}
            
