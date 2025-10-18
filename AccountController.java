import java.util.List;

public class AccountController {
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public boolean createSavingsAccount(int customerId, String accountNumber, double initialDeposit, String branch) {
        SavingsAccount account = new SavingsAccount(customerId, accountNumber, initialDeposit, branch);
        return accountDAO.createAccount(account);
    }

    public boolean createInvestmentAccount(int customerId, String accountNumber, double initialDeposit, String branch) {
        if (initialDeposit < InvestmentAccount.getMinOpeningBalance()) {
            return false; // Minimum opening balance not met
        }
        InvestmentAccount account = new InvestmentAccount(customerId, accountNumber, initialDeposit, branch);
        return accountDAO.createAccount(account);
    }

    public boolean createChequeAccount(int customerId, String accountNumber, double initialDeposit, String branch) {
        ChequeAccount account = new ChequeAccount(customerId, accountNumber, initialDeposit, branch);
        return accountDAO.createAccount(account);
    }

    public boolean deposit(int accountId, double amount) {
        Account account = accountDAO.getAccountById(accountId);
        if (account != null && amount > 0) {
            account.deposit(amount);
            boolean success = accountDAO.updateAccountBalance(accountId, account.getBalance());

            if (success) {
                Transaction transaction = new Transaction(accountId, "DEPOSIT", amount, "Cash deposit");
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
                Transaction transaction = new Transaction(accountId, "WITHDRAWAL", amount, "Cash withdrawal");
                transactionDAO.createTransaction(transaction);
            }
            return success;
        }
        return false;
    }

    public List<Account> getCustomerAccounts(int customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    public List<Transaction> getAccountTransactions(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
}