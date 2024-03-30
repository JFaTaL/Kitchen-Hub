import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class boughtItemsTable {
    public Map<String, Integer> quantities;         // Map to store purchased item quantities
    public Map<String, String> expirationDates;     // Map to store purchased item expiration dates

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
            try (Scanner scanner = new Scanner(System.in)) {
                String confirmation = scanner.nextLine().toLowerCase();

                if (confirmation.equals("yes")) {
                    // If purchased, ask for quantity
                    System.out.print("Update Quantity? (or type 'skip' to skip): ");
                    String quantityInput = scanner.nextLine();

                    int quantity = 1;

                    if (!quantityInput.equalsIgnoreCase("skip")) {
                        try {
                            int newQuantity = Integer.parseInt(quantityInput);
                            if (newQuantity >= 0) {
                                quantity = newQuantity;
                            } else {
                                System.out.println("Invalid quantity. Quantity remains unchanged.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid quantity. Quantity remains unchanged.");
                        }
                    }

                    // Ask for expiration date
                    System.out.print("Enter expiration date [YYYY-MM-DD] (or type 'skip' to skip): ");
                    String expirationDate = scanner.nextLine();

                    // Update quantities and expirationDates maps
                    quantities.put(cartItem, quantity);
                    expirationDates.put(cartItem, expirationDate);
                } else {
                    // Break out of the loop to go back to the home screen
                    break;
                }
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
        System.out.println("Purchased items saved for Cart ID: " + cartId);
    }

    // Method to save purchased items to the database
    public void savePurchasedItems(int cartID, boughtItemsTable boughtItemsTable) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "INSERT INTO ShoppingCart (cartID, productID, productName, Quantity, Price, expirationDates) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (Map.Entry<String, Integer> entry : boughtItemsTable.quantities.entrySet()) {
                    String cartItem = entry.getKey();
                    int newQuantity = entry.getValue();

                    // Extract information from the cart item
                    String[] parts = cartItem.split(", ");
                    int productID = Integer.parseInt(parts[0].split(": ")[1]);
                    String productName = parts[1].split(": ")[1];

                    // Extract the numeric part from the string before parsing
                    String priceString = parts[3].split(": ")[1];
                    String numericPart = priceString.replaceAll("[^0-9.]", ""); // Remove non-numeric characters
                    double price = Double.parseDouble(numericPart);

                    preparedStatement.setInt(1, cartID);
                    preparedStatement.setInt(2, productID);
                    preparedStatement.setString(3, productName);
                    preparedStatement.setInt(4, newQuantity);
                    preparedStatement.setDouble(5, price);
                    String expirationDate = boughtItemsTable.expirationDates.get(cartItem);
                    preparedStatement.setString(6, expirationDate);

                    preparedStatement.executeUpdate();
                }
                System.out.println("Confirmed purchased items saved for Cart ID: " + cartID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to serialize the cart for display or storage
    public static String serializeCart(LinkedList<String> cart) {
        StringBuilder serializedCart = new StringBuilder();

        for (String item : cart) {
            serializedCart.append(item).append("\n");
        }

        return serializedCart.toString();
    }
}
