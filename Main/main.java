import java.util.LinkedList;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        // Initialize necessary objects and variables
        cartIDManager cartIDManager = new cartIDManager();
        groceryCart groceryCart = new groceryCart();
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter a cart ID or skip loading
        System.out.print("Enter your cart ID (or 0 to skip): ");
        int loadCartID = scanner.nextInt();

        // Load cart data if a cart ID is provided; otherwise, start with a new cart
        if (loadCartID != 0) {
            // Load cart data if a cart ID is provided
            cartLoader.printLoadedData(loadCartID);

            // Display the loaded cart data
            System.out.println("Loaded cart data for Cart ID: " + loadCartID);
        } else {
            System.out.println("Skipping cart loading. Starting with a new cart.");
            groceryCart.createNewCart();
        }

        // Main application loop
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Search for products");
            System.out.println("2. View Cart");
            System.out.println("3. Confirm Purchased Items");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1/2/3/4): ");
            int choice = scanner.nextInt();

            // Switch based on user choice
            switch (choice) {
                case 1:
                    // Search for products
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
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please choose 1, 2, 3, or 4.");
            }
        }
    }
}
