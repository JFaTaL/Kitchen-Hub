import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class boughtItemsTable {
    private Map<String, Integer> quantities;         // Map to store purchased item quantities
    private Map<String, String> expirationDates;     // Map to store purchased item expiration dates

    public boughtItemsTable() {
        quantities = new HashMap<>();
        expirationDates = new HashMap<>();
    }

    // Method to confirm purchased items
    public void confirmPurchasedItems(groceryCart cart) {
        System.out.println("Confirm your purchased items:");

        // Display purchased items from the cart
        for (String cartItem : cart.getCart()) {
            System.out.println(cartItem);

            // Ask user to confirm each item
            System.out.print("Did you purchase this item? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String confirmation = scanner.nextLine().toLowerCase();

            if (confirmation.equals("yes")) {
                // If purchased, ask for quantity
                System.out.print("Update Quantity? (or type 'skip' to skip): ");
                String quantityInput = scanner.nextLine();

                int quantity;
                if (!quantityInput.equalsIgnoreCase("skip")) {
                    try {
                        quantity = Integer.parseInt(quantityInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity. Quantity will remain unchanged.");
                        quantity = 1; // Default to 1 if invalid quantity
                    }
                } else {
                    quantity = 1; // Default to 1 if user skips
                }

                // Ask for expiration date
                System.out.print("Enter expiration date (or type 'skip' to skip): ");
                String expirationDate = scanner.nextLine();

                // Update quantities and expirationDates maps
                quantities.put(cartItem, quantity);
                expirationDates.put(cartItem, expirationDate);
            }
        }
    }

    // Method to display purchased items, quantities, and expiration dates
    public void displayPurchasedItems() {
        System.out.println("Your purchased items:");

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            String cartItem = entry.getKey();
            int quantity = entry.getValue();
            String expirationDate = expirationDates.get(cartItem);

            System.out.println(cartItem + " - Quantity: " + quantity + ", Expiration Date: " + expirationDate);
        }
    }

    // Method to save purchased items, quantities, and expiration dates
    public void savePurchasedItems(String cartId) {
        // Save the quantities and expirationDates maps to a data store (database, file, etc.) with the associated cartId
        // Implement the logic to store the data as per your requirements
        System.out.println("Purchased items saved for Cart ID: " + cartId);
    }
}
