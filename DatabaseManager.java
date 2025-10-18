import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/kodar_bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initializeConnection();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeConnection() {
        try {
            // Try to load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            System.err.println("Please download MySQL Connector/J from:");
            System.err.println("https://dev.mysql.com/downloads/connector/j/");
            System.err.println("And add the JAR file to your classpath.");
            return;
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✓ Connected to database: kodar_bank");
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("Please make sure:");
            System.err.println("1. XAMPP MySQL is running");
            System.err.println("2. Database 'kodar_bank' exists");
            System.err.println("3. Username and password are correct");
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to initialize database tables
    public void initializeDatabase() {
        if (!isConnected()) {
            System.err.println("✗ Cannot initialize database - no connection");
            return;
        }
        createTables();
        insertSampleData();
    }

    private void createTables() {
        if (!isConnected()) return;

        String[] createTableSQLs = {
                // Customers table
                "CREATE TABLE IF NOT EXISTS customers (" +
                        "customer_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "first_name VARCHAR(50) NOT NULL, " +
                        "last_name VARCHAR(50) NOT NULL, " +
                        "address TEXT, " +
                        "phone_number VARCHAR(15), " +
                        "email VARCHAR(100), " +
                        "employment_status VARCHAR(20), " +
                        "company_name VARCHAR(100), " +
                        "company_address TEXT, " +
                        "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",

                // Accounts table
                "CREATE TABLE IF NOT EXISTS accounts (" +
                        "account_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "customer_id INT NOT NULL, " +
                        "account_number VARCHAR(20) UNIQUE NOT NULL, " +
                        "account_type ENUM('SAVINGS', 'INVESTMENT', 'CHEQUE') NOT NULL, " +
                        "balance DECIMAL(15,2) DEFAULT 0.00, " +
                        "branch VARCHAR(100), " +
                        "interest_rate DECIMAL(5,4), " +
                        "is_active BOOLEAN DEFAULT TRUE, " +
                        "opened_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))",

                // Transactions table
                "CREATE TABLE IF NOT EXISTS transactions (" +
                        "transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "account_id INT NOT NULL, " +
                        "transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER', 'INTEREST') NOT NULL, " +
                        "amount DECIMAL(15,2) NOT NULL, " +
                        "description TEXT, " +
                        "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "reference_number VARCHAR(50) UNIQUE, " +
                        "FOREIGN KEY (account_id) REFERENCES accounts(account_id))"
        };

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : createTableSQLs) {
                stmt.executeUpdate(sql);
            }
            System.out.println("✓ Database tables created successfully");
        } catch (SQLException e) {
            System.err.println("✗ Error creating tables: " + e.getMessage());
        }
    }

    private void insertSampleData() {
        if (!isConnected()) return;

        // Check if sample data already exists
        String checkSQL = "SELECT COUNT(*) FROM customers";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSQL)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // Insert sample customers
                String[] insertSQLs = {
                        "INSERT INTO customers (first_name, last_name, address, phone_number, email, employment_status, company_name, company_address) VALUES " +
                                "('John', 'Smith', '123 Main St', '2671234567', 'john.smith@email.com', 'EMPLOYED', 'Tech Corp', '456 Business Ave'), " +
                                "('Sarah', 'Johnson', '456 Oak Ave', '2672345678', 'sarah.j@email.com', 'EMPLOYED', 'Finance Inc', '789 Corporate St'), " +
                                "('Michael', 'Brown', '789 Pine Rd', '2673456789', 'm.brown@email.com', 'SELF_EMPLOYED', NULL, NULL), " +
                                "('Emily', 'Davis', '321 Elm St', '2674567890', 'emily.d@email.com', 'UNEMPLOYED', NULL, NULL), " +
                                "('David', 'Wilson', '654 Maple Dr', '2675678901', 'd.wilson@email.com', 'EMPLOYED', 'Retail Co', '123 Shop St')",

                        "INSERT INTO accounts (customer_id, account_number, account_type, balance, branch, interest_rate) VALUES " +
                                "(1, 'SAV001', 'SAVINGS', 1000.00, 'Main Branch', 0.0005), " +
                                "(1, 'INV001', 'INVESTMENT', 1500.00, 'Main Branch', 0.05), " +
                                "(2, 'SAV002', 'SAVINGS', 2500.00, 'City Branch', 0.0005), " +
                                "(3, 'CHQ001', 'CHEQUE', 500.00, 'Main Branch', 0.0), " +
                                "(4, 'SAV003', 'SAVINGS', 750.00, 'Town Branch', 0.0005)",

                        "INSERT INTO transactions (account_id, transaction_type, amount, description, reference_number) VALUES " +
                                "(1, 'DEPOSIT', 1000.00, 'Initial deposit', 'TXN001'), " +
                                "(2, 'DEPOSIT', 1500.00, 'Initial deposit', 'TXN002'), " +
                                "(3, 'DEPOSIT', 2500.00, 'Initial deposit', 'TXN003'), " +
                                "(2, 'WITHDRAWAL', 200.00, 'Cash withdrawal', 'TXN004'), " +
                                "(1, 'INTEREST', 0.50, 'Monthly interest', 'TXN005')"
                };

                try (Statement insertStmt = getConnection().createStatement()) {
                    for (String sql : insertSQLs) {
                        insertStmt.executeUpdate(sql);
                    }
                    System.out.println("✓ Sample data inserted successfully");
                }
            } else {
                System.out.println("✓ Sample data already exists");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error inserting sample data: " + e.getMessage());
        }
    }
}