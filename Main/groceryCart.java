import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class groceryCart {
    private LinkedList<String> cart = new LinkedList<>();
    private int currentCartID;
    private String selectedDepartment;
    private Map<String, Integer> quantities = new HashMap<>();
    private Map<String, String> expirationDates = new HashMap<>();

    public void createNewCart() {
        currentCartID = cartIDManager.createCartID();
    }

    public int getCurrentCartID() {
        return currentCartID;
    }

    // Method to add items to the cart
    public void addToCart(String productName) {
        cart.add(productName);
    }

    // Method to print the cart along with total and item count
    public void printCart() {
        double total = 0; // Initialize total within the method
        int cartID = getCurrentCartID();
        System.out.println("Cart ID: " + cartID);

        Pattern pricePattern = Pattern.compile("Price: (\\d+\\.\\d+)");

        for (String item : cart) {
            System.out.println(item);

            // Use regular expressions to extract the price from each item
            Matcher priceMatcher = pricePattern.matcher(item);
            if (priceMatcher.find()) {
                // Extract and parse the matched price group
                    double price = Double.parseDouble(priceMatcher.group(1));
                    total += price;
                }
                else {
                System.out.println("Error: Price pattern not Found");
            }
        }
    
        // Display the total and item count
        System.out.println("Total: $" + total);
        System.out.println("Number of items in the cart: " + cart.size());
    }

    public LinkedList<String> getCart() {
        return cart;
    }

    public void displaySearchOptions() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Search for a product");
        System.out.println("2. Filter by department");
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

    // Method to search by name/id
    public void searchByNameOrId() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Enter product name or ID: ");
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
        int selectedOption = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        if (selectedOption > 0) {
            // Prompt the user to enter the quantity
            System.out.print("Enter quantity to add to the cart: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character
    
            // Add the selected item to the cart
            addToCart(getSelectedProductDetails(productId) + " (Quantity: " + quantity + ")");
        }
    }
    
    // Method to add items to the cart by product name
    public void addToCartByName(String productName) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
        int selectedOption = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        if (selectedOption > 0) {
            // Prompt the user to enter the quantity
            System.out.print("Enter quantity to add to the cart: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            // Retrieve the details of the selected product
            String selectedProductDetails = getSelectedProductDetails(productName, selectedOption);

            // Add the selected item to the cart
            addToCart(selectedProductDetails + " (Quantity: " + quantity + ")");
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
                        // Customize this part based on your needs
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
    
    // Method to retrieve details of the selected product by name
    private String getSelectedProductDetails(String productName) {
        try (Connection connection = MYSQLUtil.getConnection()) {
            String query = "SELECT * FROM Products WHERE productName LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + productName + "%");
    
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Customize this part based on your needs
                        int productId = resultSet.getInt("productID");
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
    
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter department: ");
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
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter the number of the item you want to add to the cart (or 0 to skip): ");
                    int selectedOption = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline character
    
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
                        scanner.nextLine();  // Consume the newline character
    
                        // Retrieve the details of the selected product
                        String selectedProductDetails = getSelectedProductDetails(productId);
    
                        // Add the selected item to the cart
                        addToCart(selectedProductDetails + " (Quantity: " + quantity + ")");
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
        cart.add(selectedProductDetails + " (Quantity: " + quantity + ")");
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
}
