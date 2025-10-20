package controller;

import model.*;
import dao.*;
import java.util.List;
import java.util.ArrayList;

public class BankingController {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;
    private User currentUser; // Track logged-in user

    public BankingController() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.userDAO = new UserDAO();
        this.currentUser = null;
    }

    // Authentication methods
    public boolean login(String username, String password) {
        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isStaffLoggedIn() {
        return currentUser != null && currentUser.isStaff();
    }

    public boolean isCustomerLoggedIn() {
        return currentUser != null && currentUser.isCustomer();
    }

    // Customer-specific methods
    public Customer getCurrentCustomer() {
        if (isCustomerLoggedIn()) {
            return customerDAO.getCustomerById(currentUser.getCustomerId());
        }
        return null;
    }

    public List<Account> getCurrentCustomerAccounts() {
        if (isCustomerLoggedIn()) {
            return accountDAO.getAccountsByCustomerId(currentUser.getCustomerId());
        }
        return null;
    }

    public List<Transaction> getCurrentCustomerTransactions() {
        if (isCustomerLoggedIn()) {
            // Get all transactions for all accounts of the current customer
            List<Account> accounts = getCurrentCustomerAccounts();
            List<Transaction> allTransactions = new ArrayList<>();

            if (accounts != null) {
                for (Account account : accounts) {
                    List<Transaction> accountTransactions = transactionDAO.getTransactionsByAccountId(((AbstractAccount) account).getAccountId());
                    allTransactions.addAll(accountTransactions);
                }
            }
            return allTransactions;
        }
        return null;
    }

    // Customer operations
    public boolean createCustomer(String firstName, String lastName, String address,
                                  String phoneNumber, String email, String employmentStatus,
                                  String companyName, String companyAddress) {
        Customer customer = new Customer(0, firstName, lastName, address, phoneNumber,
                email, employmentStatus, companyName, companyAddress);
        return customerDAO.createCustomer(customer);
    }

    public Customer getCustomer(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int customerId) {
        return customerDAO.deleteCustomer(customerId);
    }

    // Account operations
    public boolean createSavingsAccount(int customerId, String accountNumber,
                                        double initialDeposit, String branch) {
        SavingsAccount account = new SavingsAccount(0, accountNumber, customerId, initialDeposit, branch);
        return accountDAO.createAccount(account);
    }

    public boolean createInvestmentAccount(int customerId, String accountNumber,
                                           double initialDeposit, String branch) {
        if (!InvestmentAccount.isValidInitialDeposit(initialDeposit)) {
            return false;
        }
        InvestmentAccount account = new InvestmentAccount(0, accountNumber, customerId, initialDeposit, branch);
        return accountDAO.createAccount(account);
    }

    public boolean createChequeAccount(int customerId, String accountNumber,
                                       double initialDeposit, String branch) {
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null || !customer.canOpenChequeAccount()) {
            return false;
        }
        ChequeAccount account = new ChequeAccount(0, accountNumber, customerId, initialDeposit, branch);
        return accountDAO.createAccount(account);
    }

    public List<Account> getCustomerAccounts(int customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    // Method to get all accounts in the system
    public List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        List<Customer> customers = customerDAO.getAllCustomers();

        for (Customer customer : customers) {
            List<Account> customerAccounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
            allAccounts.addAll(customerAccounts);
        }
        return allAccounts;
    }

    // Method to get all transactions in the system
    public List<Transaction> getAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        List<Customer> customers = customerDAO.getAllCustomers();

        for (Customer customer : customers) {
            List<Account> customerAccounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
            for (Account account : customerAccounts) {
                List<Transaction> accountTransactions = transactionDAO.getTransactionsByAccountId(((AbstractAccount) account).getAccountId());
                allTransactions.addAll(accountTransactions);
            }
        }
        return allTransactions;
    }

    public boolean deposit(int accountId, double amount) {
        Account account = accountDAO.getAccountById(accountId);
        if (account != null && amount > 0) {
            account.deposit(amount);
            boolean success = accountDAO.updateAccountBalance(accountId, account.getBalance());

            if (success) {
                Transaction transaction = new Transaction(0, accountId, "DEPOSIT",
                        amount, "Cash deposit", transactionDAO.generateReferenceNumber());
                transactionDAO.createTransaction(transaction);
            }
            return success;
        }
        return false;
    }

    // Method to process deposit (for staff)
    public boolean processDeposit(int accountId, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            account.deposit(amount);
            boolean success = accountDAO.updateAccountBalance(accountId, account.getBalance());

            if (success) {
                Transaction transaction = new Transaction(0, accountId, "DEPOSIT",
                        amount, description, transactionDAO.generateReferenceNumber());
                transactionDAO.createTransaction(transaction);
            }
            return success;
        }
        return false;
    }

    public boolean withdraw(int accountId, double amount) {
        Account account = accountDAO.getAccountById(accountId);
        if (account != null && account.withdraw(amount)) {
            boolean success = accountDAO.updateAccountBalance(accountId, account.getBalance());

            if (success) {
                Transaction transaction = new Transaction(0, accountId, "WITHDRAWAL",
                        amount, "Cash withdrawal", transactionDAO.generateReferenceNumber());
                transactionDAO.createTransaction(transaction);
            }
            return success;
        }
        return false;
    }

    // Method to process withdrawal (for staff)
    public boolean processWithdrawal(int accountId, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account != null && account.withdraw(amount)) {
            boolean success = accountDAO.updateAccountBalance(accountId, account.getBalance());

            if (success) {
                Transaction transaction = new Transaction(0, accountId, "WITHDRAWAL",
                        amount, description, transactionDAO.generateReferenceNumber());
                transactionDAO.createTransaction(transaction);
            }
            return success;
        }
        return false;
    }

    public boolean transfer(int fromAccountId, int toAccountId, double amount) {
        Account fromAccount = accountDAO.getAccountById(fromAccountId);
        Account toAccount = accountDAO.getAccountById(toAccountId);

        if (fromAccount != null && toAccount != null && fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);

            boolean success1 = accountDAO.updateAccountBalance(fromAccountId, fromAccount.getBalance());
            boolean success2 = accountDAO.updateAccountBalance(toAccountId, toAccount.getBalance());

            if (success1 && success2) {
                String ref = transactionDAO.generateReferenceNumber();
                Transaction withdrawal = new Transaction(0, fromAccountId, "TRANSFER",
                        amount, "Transfer to account " + ((AbstractAccount) toAccount).getAccountNumber(), ref);
                Transaction deposit = new Transaction(0, toAccountId, "TRANSFER",
                        amount, "Transfer from account " + ((AbstractAccount) fromAccount).getAccountNumber(), ref);

                transactionDAO.createTransaction(withdrawal);
                transactionDAO.createTransaction(deposit);
                return true;
            }
        }
        return false;
    }

    public void applyInterest() {
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            List<Account> accounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
            for (Account account : accounts) {
                double interest = account.calculateInterest();
                if (interest > 0) {
                    account.deposit(interest);
                    accountDAO.updateAccountBalance(((AbstractAccount) account).getAccountId(), account.getBalance());

                    Transaction transaction = new Transaction(0, ((AbstractAccount) account).getAccountId(),
                            "INTEREST", interest, "Monthly interest", transactionDAO.generateReferenceNumber());
                    transactionDAO.createTransaction(transaction);
                }
            }
        }
    }

    public List<Transaction> getAccountTransactions(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    public boolean closeAccount(int accountId) {
        return accountDAO.closeAccount(accountId);
    }

    // Add this method to create user when creating customer
    public boolean createCustomerWithUser(String firstName, String lastName, String address,
                                          String phoneNumber, String email, String employmentStatus,
                                          String companyName, String companyAddress,
                                          String username, String password) {
        // First create customer
        Customer customer = new Customer(0, firstName, lastName, address, phoneNumber,
                email, employmentStatus, companyName, companyAddress);
        boolean customerCreated = customerDAO.createCustomer(customer);

        if (customerCreated) {
            // Get the newly created customer ID
            int latestCustomerId = getLatestCustomerId();

            // Create user account
            User user = new User(0, username, password, "CUSTOMER", latestCustomerId, true);
            return userDAO.createUser(user);
        }
        return false;
    }

    // Helper method to get the latest customer ID
    private int getLatestCustomerId() {
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            return 1;
        }
        return customers.stream()
                .mapToInt(Customer::getCustomerId)
                .max()
                .orElse(1);
    }

    // Method to get account by account number
    public Account getAccountByNumber(String accountNumber) {
        List<Account> accounts = getAllAccounts();
        for (Account account : accounts) {
            if (((AbstractAccount) account).getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // Method for customer to transfer funds
    public boolean customerTransfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = getAccountByNumber(fromAccountNumber);
        Account toAccount = getAccountByNumber(toAccountNumber);

        if (fromAccount != null && toAccount != null && fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);

            boolean success1 = accountDAO.updateAccountBalance(((AbstractAccount) fromAccount).getAccountId(), fromAccount.getBalance());
            boolean success2 = accountDAO.updateAccountBalance(((AbstractAccount) toAccount).getAccountId(), toAccount.getBalance());

            if (success1 && success2) {
                String ref = transactionDAO.generateReferenceNumber();
                Transaction withdrawal = new Transaction(0, ((AbstractAccount) fromAccount).getAccountId(), "TRANSFER",
                        amount, "Transfer to account " + toAccountNumber, ref);
                Transaction deposit = new Transaction(0, ((AbstractAccount) toAccount).getAccountId(), "TRANSFER",
                        amount, "Transfer from account " + fromAccountNumber, ref);

                transactionDAO.createTransaction(withdrawal);
                transactionDAO.createTransaction(deposit);
                return true;
            }
        }
        return false;
    }

    // Method to get account by ID
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    // Method to get all account numbers for dropdowns
    public List<String> getAllAccountNumbers() {
        List<String> accountNumbers = new ArrayList<>();
        List<Account> accounts = getAllAccounts();

        for (Account account : accounts) {
            accountNumbers.add(((AbstractAccount) account).getAccountNumber());
        }
        return accountNumbers;
    }

    // Method to get account ID by account number
    public int getAccountIdByNumber(String accountNumber) {
        List<Account> accounts = getAllAccounts();
        for (Account account : accounts) {
            if (((AbstractAccount) account).getAccountNumber().equals(accountNumber)) {
                return ((AbstractAccount) account).getAccountId();
            }
        }
        return -1;
    }
}
