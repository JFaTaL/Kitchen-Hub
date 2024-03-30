import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class groceryCart {
    // Class to manage the grocery cart, including items, quantities, and expiration dates

    private LinkedList<String> cart = new LinkedList<>();
    private int currentCartID;
    private Map<Object, Integer> quantities = new HashMap<>();
    


    // Method to create a new cart and generate a cart ID
    public void createNewCart() {
        currentCartID = cartIDManager.createCartID();
    }

    // Method to get the current cart ID
    public int getCurrentCartID() {
        return currentCartID;
    }

    // Method to add items to the cart
    public void addToCart(String productName, int quantity) {
        cart.add(productName);
        quantities.put(productName, quantities.getOrDefault(productName, 0) + quantity);
    }

    public int getQuantityFromCart(String productName) {
        return quantities.getOrDefault(productName, 0);
    }

    private String extractProductName(String item) {
        int nameIndex = item.indexOf("Name: ");
        if (nameIndex != -1) {
            // Extract the substring starting from the position after "Name: "
            String nameSubstring = item.substring(nameIndex + "Name: ".length());
    
            // Split the substring by comma and take the first part (which is the name)
            String[] parts = nameSubstring.split(", ");
            String productName = parts[0].trim(); // Remove leading/trailing whitespace if any
            return productName;
        } else {
            // Handle case where "Name: " is not found in the item string
            System.out.println("Product name not found in the string.");
            return "";
        }
    }

    private int extractQuantity(String item) {
        // Find the index of " (Quantity: " in the item string
        int quantityIndex = item.indexOf(" (Quantity: ");
        
        if (quantityIndex != -1) {
            // Extract the quantity substring starting from the position after " (Quantity: "
            String quantitySubstring = item.substring(quantityIndex + " (Quantity: ".length());
            
            // Find the index of ")" in the quantitySubstring
            int endIndex = quantitySubstring.indexOf(")");
            
            if (endIndex != -1) {
                // Extract the substring containing the quantity
                String quantityStr = quantitySubstring.substring(0, endIndex);
                
                // Parse the quantity string to an integer and return it
                return Integer.parseInt(quantityStr.trim());
            }
        }
        // Return 0 if quantity is not found or invalid
        return 0;
    }

    // Method to print the cart along with total and item count
    public void printCart() {
        double total = 0; // Initialize total within the method
        int cartID = getCurrentCartID();
        System.out.println("Cart ID: " + cartID);
        

    
        for (String item : cart) {
            System.out.println(item);
            int quantity = extractQuantity(item);
    
            // Find the index of "Price: " in the item string
            int priceIndex = item.indexOf("Price: ");
            
            // If "Price: " is found, extract the substring from the index after it
            if (priceIndex != -1) {
                // Extract the substring starting from the position after "Price: "0
                String priceSubstring = item.substring(priceIndex + "Price: ".length());
        
                 // Split the substring by whitespace and take the first part (which is the price)0
                String[] parts = priceSubstring.split(" ");
                String priceStr = parts[0].trim(); // Remove leading/trailing whitespace if any
        
                double price = Double.parseDouble(priceStr);
            
                // Calculate the total price for the current item and add it to the existing total
                total += (price * quantity);
            } else {
                System.out.println("Price information not found in the string.");
                System.out.println(item);
            }
        }
           
        //System.out.println("Total: $" + total);
        System.out.printf("Total: $" + "%.2f\n", total ); 
        System.out.println("Number of items in the cart: " + cart.size());
    }

    // Method to get the current cart
    public LinkedList<String> getCart() {
        return cart;
    }

    // Method to display search options for users
    public void displaySearchOptions() {
        System.out.println("Choose an option:");
        System.out.println("1. Search for a product");
        System.out.println("2. Filter by department");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                searchByNameOrId();
                break;
            case 2:
                displayDepartments();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    // Method to search for a product by name or ID
    public void searchByNameOrId() {
        System.out.println("Enter product name or ID: ");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        try {
            int productId = Integer.parseInt(userInput);
            // If the user input is a number, search by ID
            searchProductById(productId);
            addToCartById(productId); // Add the product to the cart
        } catch (NumberFormatException e) {
            // If the user input is not a number, search by name
            searchProductByName(userInput);
            addToCartByName(userInput); // Add the product to the cart
        }      
    }

    // Method to add items to the cart by product ID
    public void addToCartById(int productId) {
        System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
        Scanner scanner = new Scanner(System.in);
        int selectedOption = scanner.nextInt();

        if (selectedOption > 0) {
            // Prompt the user to enter the quantity
            System.out.print("Enter quantity to add to the cart: ");
            int quantity = scanner.nextInt();

            // Add the selected item to the cart
            addToCart(getSelectedProductDetails(productId) + " (Quantity: " + quantity + ")", quantity);
        }
    }

    // Method to add items to the cart by product name
    public void addToCartByName(String productName) {
        System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
        Scanner scanner = new Scanner(System.in);
        int selectedOption = scanner.nextInt();

        if (selectedOption > 0) {
            // Prompt the user to enter the quantity
            System.out.print("Enter quantity to add to the cart: ");
            int quantity = scanner.nextInt();

            // Retrieve the details of the selected product
            String selectedProductDetails = getSelectedProductDetails(productName, selectedOption);

            // Add the selected item to the cart
            addToCart(selectedProductDetails + " (Quantity: " + quantity + ")", quantity);
        }
    }


    // Method to retrieve details of the selected product by ID
    private String getSelectedProductDetails(int productId) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT * FROM Products WHERE ProductID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String productName = resultSet.getString("productName");
                        double productPrice = resultSet.getDouble("productPrice");

                        return "Product ID: " + productId + ", Name: " + productName + ", Price: " + productPrice;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";  // Return an empty string if no details are found
    }

    // Method to retrieve details of the selected product by name and option number
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

    // Method to search for a product by name
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

    // Method to display available departments
    public void displayDepartments() {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT DISTINCT department FROM Products";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Departments:");

                    while (resultSet.next()) {
                        String department = resultSet.getString("department");
                        System.out.println(department);
                    }

                    System.out.print("Enter department: ");
                    Scanner scanner = new Scanner(System.in);
                    String selectedDepartment = scanner.next();
                    if (!selectedDepartment.isEmpty()) {
                        searchByDepartment(selectedDepartment);
                    } else {
                        System.out.println("Invalid department. Returning to the main menu.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for products by department
    public void searchByDepartment(String department) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT * FROM Products WHERE department = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, department);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int optionNumber = 1;
                    Map<Integer, String> productOptions = new HashMap<>();

                    while (resultSet.next()) {
                        int productId = resultSet.getInt("productID");
                        String productNameResult = resultSet.getString("productName");
                        double productPrice = resultSet.getDouble("productPrice");

                        System.out.println(optionNumber + ". Product ID: " + productId + ", Name: " + productNameResult + ", Price: " + productPrice);
                        productOptions.put(optionNumber, productNameResult);
                        optionNumber++;
                    }

                    // Prompt the user to select an item
                    System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
                    Scanner scanner = new Scanner(System.in);
                    int selectedOption = scanner.nextInt();

                    if (selectedOption > 0 && productOptions.containsKey(selectedOption)) {
                        // Call the addToCartByDepartment method with the selected product name
                        addToCartByDepartment(productOptions.get(selectedOption));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add items to the cart by department
public void addToCartByDepartment(String productName) {
    try (Connection connection = MYSQLUtil.getConnection()) {
        String query = "SELECT * FROM Products WHERE productName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("productID");

                    // Prompt the user to enter the quantity
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter quantity to add to the cart: ");
                    int quantity = scanner.nextInt();

                        // Retrieve the details of the selected product
                    String selectedProductDetails = getSelectedProductDetails(productId);

                        // Add the selected item to the cart
                    String cartItem = selectedProductDetails + " (Quantity: " + quantity + ")";
                    addToCart(cartItem, quantity);
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
        addToCart(selectedProductDetails + " (Quantity: " + quantity + ")", quantity);
    }

}
