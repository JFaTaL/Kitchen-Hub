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
            /* 
            while (true) {
                // Search for products
                System.out.println("Search for products:");
                System.out.print("Enter product name or product ID: ");
                String searchInput = scanner.nextLine();
    
                // Check if the user entered a numeric value (product ID)
                if (searchInput.matches("\\d+")) {
                    int productId = Integer.parseInt(searchInput);
                    System.out.println("Searching for product by ID...");
                    groceryCart.searchProductById(productId);
                } else {
                    System.out.println("Searching for products by name...");
                    groceryCart.searchProductByName(searchInput);
    
                    // Prompt the user to select an item from the list
                    System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
                    int selectedOption = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline character
    
                    if (selectedOption > 0) {
                        // Add the selected item to the cart
                        System.out.print("Enter quantity to add to the cart: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();  // Consume the newline character
    
                        // Add the selected item to the cart
                        groceryCart.addToCartByOption(searchInput, selectedOption, quantity);
                    }
                }
    
                // Ask the user if they want to perform another search
                System.out.print("Do you want to perform another search? (yes/no): ");
                String performAnotherSearchInput = scanner.nextLine();
    
                if (!performAnotherSearchInput.equalsIgnoreCase("yes")) {
                    break;
                }
            }
    
            // Display the final cart
            System.out.println("Your final cart: ");
            groceryCart.printCart(); 
            
            // Confirm purchased items
            System.out.print("Do you want to confirm purchased items? (yes/no): ");
            String confirmPurchasedItemsInput = scanner.nextLine();
    
            if (confirmPurchasedItemsInput.equalsIgnoreCase("yes")) {
                boughtItemsTable boughtItemsTable = new boughtItemsTable();
                boughtItemsTable.confirmPurchasedItems(groceryCart);
                boughtItemsTable.displayPurchasedItems();
                boughtItemsTable.savePurchasedItems(Integer.toString(cartID.getCartID()));
            }
            scanner.close();
        }
    }
}
*/
