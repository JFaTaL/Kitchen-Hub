import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class cartLoader {

    // Method to print loaded data for a given cart ID
    public static void printLoadedData(int cartID) {
        double total = 0;

        System.out.println("Cart ID: " + cartID);

        try (Connection connection = MYSQLUtil.getConnection()) {
            // SQL query to retrieve cart data for the specified cart ID
            String query = "SELECT * FROM ShoppingCart WHERE cartID = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, cartID);

                // Execute the query and process the result set
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Extract product details from the result set
                        String productName = resultSet.getString("productName");
                        int quantity = resultSet.getInt("quantity");
                        double price = resultSet.getDouble("price");

                        // Display loaded item details
                        System.out.println(productName + " (Quantity: " + quantity + ", Price: $" + price + ")");
                        
                        // Update total cost
                        total += quantity * price;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Display the total and item count for the loaded cart
        System.out.println("Total: $" + total);
        System.out.println("Number of items in the cart: " + total);
    }
}
