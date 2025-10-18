import java.util.List;

public class IntegrationTest {

    public static void runFullIntegrationTest() {
        System.out.println("=== Running Full Integration Test ===\n");

        CustomerController customerController = new CustomerController();
        AccountController accountController = new AccountController();

        // 1. Create a new customer
        System.out.println("1. Creating new customer...");
        boolean customerCreated = customerController.createCustomer(
                "Integration", "Test", "789 Integration St", "2679998888",
                "integration.test@email.com", "EMPLOYED", "Integration Corp", "789 Test Ave"
        );
        System.out.println(customerCreated ? "✓ Customer created" : "✗ Customer creation failed");

        // 2. Create different types of accounts
        System.out.println("\n2. Creating accounts...");
        boolean savingsCreated = accountController.createSavingsAccount(1, "SAV001", 1000.00, "Main Branch");
        System.out.println(savingsCreated ? "✓ Savings account created" : "✗ Savings account creation failed");

        boolean investmentCreated = accountController.createInvestmentAccount(1, "INV001", 600.00, "Main Branch");
        System.out.println(investmentCreated ? "✓ Investment account created" : "✗ Investment account creation failed");

        boolean chequeCreated = accountController.createChequeAccount(1, "CHQ001", 500.00, "Main Branch");
        System.out.println(chequeCreated ? "✓ Cheque account created" : "✗ Cheque account creation failed");

        // 3. Test transactions
        System.out.println("\n3. Testing transactions...");
        boolean depositSuccess = accountController.deposit(1, 200.00);
        System.out.println(depositSuccess ? "✓ Deposit successful" : "✗ Deposit failed");

        boolean withdrawalSuccess = accountController.withdraw(2, 100.00);
        System.out.println(withdrawalSuccess ? "✓ Withdrawal from investment account successful" : "✗ Withdrawal from investment account failed");

        // 4. Test account listing
        System.out.println("\n4. Testing account retrieval...");
        List<Account> accounts = accountController.getCustomerAccounts(1);
        System.out.println("✓ Retrieved " + accounts.size() + " accounts for customer");

        // 5. Test transaction history
        System.out.println("\n5. Testing transaction history...");
        List<Transaction> transactions = accountController.getAccountTransactions(1);
        System.out.println("✓ Retrieved " + transactions.size() + " transactions for account");

        System.out.println("\n=== Integration Test Complete ===");
    }
}