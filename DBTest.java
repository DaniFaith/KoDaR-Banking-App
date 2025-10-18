public class DBTest {

    public static void testDatabaseConnection() {
        System.out.println("Testing database connection...");
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            if (dbManager.getConnection() != null && !dbManager.getConnection().isClosed()) {
                System.out.println("✓ Database connection successful");
            } else {
                System.out.println("✗ Database connection failed");
            }
        } catch (Exception e) {
            System.out.println("✗ Database connection error: " + e.getMessage());
        }
    }

    public static void testCustomerCreation() {
        System.out.println("\nTesting customer creation...");
        CustomerController customerController = new CustomerController();

        boolean success = customerController.createCustomer(
                "Test", "User", "123 Test St", "2671234567",
                "test@email.com", "EMPLOYED", "Test Corp", "123 Test Ave"
        );

        if (success) {
            System.out.println("✓ Customer creation successful");
        } else {
            System.out.println("✗ Customer creation failed");
        }
    }

    public static void testAccountOperations() {
        System.out.println("\nTesting account operations...");
        AccountController accountController = new AccountController();

        // Test savings account creation
        boolean savingsSuccess = accountController.createSavingsAccount(6, "SAV006", 1000.00, "Test Branch");
        System.out.println(savingsSuccess ? "✓ Savings account creation successful" : "✗ Savings account creation failed");

        // Test investment account creation with sufficient funds
        boolean investmentSuccess = accountController.createInvestmentAccount(6, "INV006", 500.00, "Test Branch");
        System.out.println(investmentSuccess ? "✓ Investment account creation successful" : "✗ Investment account creation failed");

        // Test investment account creation with insufficient funds
        boolean investmentFailure = accountController.createInvestmentAccount(6, "INV007", 400.00, "Test Branch");
        System.out.println(!investmentFailure ? "✓ Investment account minimum balance validation working" : "✗ Investment account validation failed");

        // Test cheque account creation
        boolean chequeSuccess = accountController.createChequeAccount(6, "CHQ006", 300.00, "Test Branch");
        System.out.println(chequeSuccess ? "✓ Cheque account creation successful" : "✗ Cheque account creation failed");

        // Test deposit
        boolean depositSuccess = accountController.deposit(6, 100.00);
        System.out.println(depositSuccess ? "✓ Deposit operation successful" : "✗ Deposit operation failed");

        // Test withdrawal
        boolean withdrawalSuccess = accountController.withdraw(7, 50.00);
        System.out.println(withdrawalSuccess ? "✓ Withdrawal operation successful" : "✗ Withdrawal operation failed");
    }
}