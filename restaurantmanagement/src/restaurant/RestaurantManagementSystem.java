package restaurant;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class RestaurantManagementSystem extends JFrame {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Palchin@5570";  // Change it to your MySQL password

    // Constructor
    public RestaurantManagementSystem() {
        // Show login page initially
        showLoginPage();
    }

    // Database Connection Method
    private Connection connectToDatabase() {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create a connection to the database
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed!");
            return null;
        }
    }

    // Method to show the login page
    private void showLoginPage() {
        getContentPane().removeAll();
        setTitle("Restaurant Management System - Login");

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(255, 228, 196));

        JLabel titleLabel = new JLabel("Restaurant Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(60, 10, 300, 30);
        loginPanel.add(titleLabel);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(50, 50, 80, 25);
        loginPanel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(150, 50, 160, 25);
        loginPanel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 80, 80, 25);
        loginPanel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 80, 160, 25);
        loginPanel.add(passwordText);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 120, 100, 25);
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(loginButton);

        // Action Listener for login
        loginButton.addActionListener(e -> {
            String userId = userText.getText();
            String password = new String(passwordText.getPassword());

            if (authenticateUser(userId, password)) {
                String role = getUserRole(userId);
                showDashboard(role);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(loginPanel);
        revalidate();
        repaint();
    }

    // Authenticate user from the database
    private boolean authenticateUser(String username, String password) {
        Connection connection = connectToDatabase();
        if (connection == null) return false;

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get user role from the database
    private String getUserRole(String username) {
        Connection connection = connectToDatabase();
        if (connection == null) return null;

        String query = "SELECT role FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to show the dashboard
    private void showDashboard(String role) {
        getContentPane().removeAll();
        setTitle("Dashboard - " + role);

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(null);
        dashboardPanel.setBackground(new Color(173, 216, 230));

        JLabel dashboardLabel = new JLabel(role + " Dashboard");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardLabel.setBounds(100, 10, 300, 30);
        dashboardPanel.add(dashboardLabel);

        // Based on role, show different options
        if (role.equals("Admin")) {
            addAdminOptions(dashboardPanel);
        } else if (role.equals("Cashier")) {
//            addCashierOptions(dashboardPanel);
        } else if (role.equals("Staff")) {
//            addStaffOptions(dashboardPanel);
        }
        
        

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 220, 100, 30);
        logoutButton.setBackground(new Color(255, 69, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardPanel.add(logoutButton);

        logoutButton.addActionListener(e -> showLoginPage());

        add(dashboardPanel);
        revalidate();
        repaint();
    }

    // Admin Dashboard Options
    private void addAdminOptions(JPanel dashboardPanel) {
        JButton manageMenuButton = new JButton("Manage Menu");
        manageMenuButton.setBounds(100, 60, 200, 30);
        manageMenuButton.setBackground(new Color(70, 130, 180));
        manageMenuButton.setForeground(Color.WHITE);
        manageMenuButton.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardPanel.add(manageMenuButton);
        manageMenuButton.addActionListener(e -> showMenuManagementPage());

        JButton manageEmployeesButton = new JButton("Manage Employees");
        manageEmployeesButton.setBounds(100, 100, 200, 30);
        manageEmployeesButton.setBackground(new Color(46, 139, 87));
        manageEmployeesButton.setForeground(Color.WHITE);
        manageEmployeesButton.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardPanel.add(manageEmployeesButton);
        manageEmployeesButton.addActionListener(e -> showEmployeeManagementPage());

        JButton salesReportButton = new JButton("Sales Reports");
        salesReportButton.setBounds(100, 140, 200, 30);
        salesReportButton.setBackground(new Color(255, 140, 0));
        salesReportButton.setForeground(Color.WHITE);
        salesReportButton.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardPanel.add(salesReportButton);
        salesReportButton.addActionListener(e -> showSalesReportsPage());
    }

    // Manage Menu Page (Now with Add, Update, and Delete functionality)
    private void showMenuManagementPage() {
        getContentPane().removeAll();
        setTitle("Manage Menu");

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);

        JLabel menuLabel = new JLabel("Menu Management");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuLabel.setBounds(100, 10, 200, 30);
        menuPanel.add(menuLabel);

        JTextArea menuDisplay = new JTextArea();
        menuDisplay.setBounds(50, 50, 300, 150);
        menuDisplay.setEditable(false);
        updateMenuDisplay(menuDisplay);
        menuPanel.add(menuDisplay);

        JLabel itemLabel = new JLabel("Item Name:");
        itemLabel.setBounds(50, 220, 100, 25);
        menuPanel.add(itemLabel);

        JTextField itemNameField = new JTextField();
        itemNameField.setBounds(150, 220, 150, 25);
        menuPanel.add(itemNameField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(50, 250, 100, 25);
        menuPanel.add(priceLabel);

        JTextField itemPriceField = new JTextField();
        itemPriceField.setBounds(150, 250, 150, 25);
        menuPanel.add(itemPriceField);

        JButton addButton = new JButton("Add Item");
        addButton.setBounds(50, 290, 100, 25);
        menuPanel.add(addButton);
        addButton.addActionListener(e -> {
            String name = itemNameField.getText();
            double price;
            try {
                price = Double.parseDouble(itemPriceField.getText());
                addMenuItemToDatabase(name, price);
                updateMenuDisplay(menuDisplay);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price entered.");
            }
        });

        JButton updateButton = new JButton("Update Item");
        updateButton.setBounds(160, 290, 100, 25);
        menuPanel.add(updateButton);
        updateButton.addActionListener(e -> {
            String name = itemNameField.getText();
            double price;
            try {
                price = Double.parseDouble(itemPriceField.getText());
                updateMenuItemInDatabase(name, price);
                updateMenuDisplay(menuDisplay);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price entered.");
            }
        });

        JButton deleteButton = new JButton("Delete Item");
        deleteButton.setBounds(270, 290, 100, 25);
        menuPanel.add(deleteButton);
        deleteButton.addActionListener(e -> {
            String name = itemNameField.getText();
            deleteMenuItemFromDatabase(name);
            updateMenuDisplay(menuDisplay);
        });

        add(menuPanel);
        revalidate();
        repaint();
    }

    // Add menu item to the database
    private void addMenuItemToDatabase(String name, double price) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "INSERT INTO menu_items (name, price) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update menu item in the database
    private void updateMenuItemInDatabase(String name, double price) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "UPDATE menu_items SET price = ? WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, price);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete menu item from the database
    private void deleteMenuItemFromDatabase(String name) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "DELETE FROM menu_items WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update the menu display with database values
    private void updateMenuDisplay(JTextArea menuDisplay) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "SELECT * FROM menu_items";
        StringBuilder displayText = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                displayText.append(name).append(": $").append(price).append("\n");
            }
            menuDisplay.setText(displayText.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Manage Employees Page
    private void showEmployeeManagementPage() {
        getContentPane().removeAll();
        setTitle("Manage Employees");

        JPanel employeePanel = new JPanel();
        employeePanel.setLayout(null);

        JLabel employeeLabel = new JLabel("Employee Management");
        employeeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        employeeLabel.setBounds(100, 10, 200, 30);
        employeePanel.add(employeeLabel);

        JTextArea employeeDisplay = new JTextArea();
        employeeDisplay.setBounds(50, 50, 300, 150);
        employeeDisplay.setEditable(false);
        updateEmployeeDisplay(employeeDisplay);
        employeePanel.add(employeeDisplay);

        JTextField employeeNameField = new JTextField();
        employeeNameField.setBounds(50, 220, 150, 25);
        employeePanel.add(employeeNameField);

        JTextField employeeRoleField = new JTextField();
        employeeRoleField.setBounds(220, 220, 150, 25);
        employeePanel.add(employeeRoleField);

        JButton addButton = new JButton("Add Employee");
        addButton.setBounds(50, 260, 120, 25);
        employeePanel.add(addButton);
        addButton.addActionListener(e -> {
            String name = employeeNameField.getText();
            String role = employeeRoleField.getText();
            addEmployeeToDatabase(name, role);
            updateEmployeeDisplay(employeeDisplay);
        });

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setBounds(180, 260, 150, 25);
        employeePanel.add(deleteButton);
        deleteButton.addActionListener(e -> {
            String name = employeeNameField.getText();
            deleteEmployeeFromDatabase(name);
            updateEmployeeDisplay(employeeDisplay);
        });

        add(employeePanel);
        revalidate();
        repaint();
    }

    // Add employee to the database
    private void addEmployeeToDatabase(String name, String role) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "INSERT INTO users (username, role, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setString(3, "password");  // Default password
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete employee from the database
    private void deleteEmployeeFromDatabase(String name) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update the employee display with database values
    private void updateEmployeeDisplay(JTextArea employeeDisplay) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "SELECT * FROM users";
        StringBuilder displayText = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("username");
                String role = rs.getString("role");
                displayText.append(name).append(": ").append(role).append("\n");
            }
            employeeDisplay.setText(displayText.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sales Report Page
    private void showSalesReportsPage() {
        getContentPane().removeAll();
        setTitle("Sales Reports");

        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(null);

        JLabel reportLabel = new JLabel("Sales Reports");
        reportLabel.setFont(new Font("Arial", Font.BOLD, 18));
        reportLabel.setBounds(100, 10, 200, 30);
        reportPanel.add(reportLabel);

        JTextArea reportDisplay = new JTextArea();
        reportDisplay.setBounds(50, 50, 300, 200);
        reportDisplay.setEditable(false);
        updateSalesReportDisplay(reportDisplay);
        reportPanel.add(reportDisplay);

        add(reportPanel);
        revalidate();
        repaint();
    }

    // Update sales report display with aggregated data
    private void updateSalesReportDisplay(JTextArea reportDisplay) {
        Connection connection = connectToDatabase();
        if (connection == null) return;

        String query = "SELECT menu_items.name, SUM(orders.quantity) AS total_quantity, SUM(orders.total_price) AS total_sales " +
                "FROM orders " +
                "JOIN menu_items ON orders.menu_item_id = menu_items.id " +
                "GROUP BY menu_items.name";
        StringBuilder displayText = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("total_quantity");
                double totalSales = rs.getDouble("total_sales");
                displayText.append(name).append(": ").append(quantity).append(" sold, $").append(totalSales).append("\n");
            }
            reportDisplay.setText(displayText.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        RestaurantManagementSystem rms = new RestaurantManagementSystem();
        rms.setSize(400, 400);
        rms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rms.setLocationRelativeTo(null);
        rms.setVisible(true);
    }
}
