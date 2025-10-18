public class MainApp {
    public static void main(String[] args) {
        // Initialize database connection
        DatabaseManager dbManager = DatabaseManager.getInstance();

        // Initialize database tables and sample data
        dbManager.initializeDatabase();

        // Test the system
        DBTest.testDatabaseConnection();
        DBTest.testCustomerCreation();
        DBTest.testAccountOperations();

        // Run full integration test
        System.out.println("\n==================================================");
        IntegrationTest.runFullIntegrationTest();

        // Close connection when done
        dbManager.closeConnection();

        System.out.println("KoDar Banking System - All tests completed!");
    }
}