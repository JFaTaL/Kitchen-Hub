import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Scanner;

public class groceryCart {
    private LinkedList<String> cart = new LinkedList<>();

    public groceryCart() {}

    // Method to add items to the cart
    public void addToCart(String productName) {
        cart.add(productName);
    }

    // Method to print the cart
    public void printCart() {
        System.out.println(cart);
    }

    // Method to get the cart
    public LinkedList<String> getCart() {
        return cart;
    }

    // Method to add items from the cart to the database
    public void searchProductByName(String productByName) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT * FROM Products WHERE productName LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + productByName + "%");
    
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int optionNumber = 1;
                    while (resultSet.next()) {
                        // Customize this part based on your needs
                        int productId = resultSet.getInt("productID");
                        String productNameResult = resultSet.getString("productName");
                        double productPrice = resultSet.getDouble("productPrice");
    
                        System.out.println(optionNumber + ". Product ID: " + productId + ", Name: " + productNameResult + ", Price: " + productPrice);
                        optionNumber++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for a product by ID
    public void searchProductById(int productId) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT * FROM Products WHERE ProductID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Customize this part based on your needs
                        String productName = resultSet.getString("productName");
                        double productPrice = resultSet.getDouble("productPrice");

                        System.out.println("Product ID: " + productId + ", Name: " + productName + ", Price: " + productPrice);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add items to the cart based on the user's selection
    public void addToCartByOption(String productName, int selectedOption, int quantity) {
        // Retrieve the details of the selected product
        String selectedProductDetails = getSelectedProductDetails(productName, selectedOption);

        // Add the selected item to the cart
        cart.add("Selected option " + selectedOption + ": " + selectedProductDetails + " (Quantity: " + quantity + ")");
    }

    // Method to retrieve details of the selected product
    private String getSelectedProductDetails(String productName, int selectedOption) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT * FROM Products WHERE productName LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + productName + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                        if (count == selectedOption) {
                            // Retrieve details of the selected product
                            int productId = resultSet.getInt("ProductID");
                            String name = resultSet.getString("productName");
                            double price = resultSet.getDouble("productPrice");
                            return "Product ID: " + productId + ", Name: " + name + ", Price: " + price;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";  // Return an empty string if no details are found
    }

    public static void main(String[] args) {
        groceryCart groceryCart = new groceryCart();
        Scanner scanner = new Scanner(System.in);

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

        // Close the scanner
        scanner.close();
    }
}
