package model;

public class InvestmentAccount extends AbstractAccount {
    private static final double MIN_INITIAL_DEPOSIT = 500.00;

    public InvestmentAccount(int accountId, String accountNumber, int customerId,
                             double balance, String branch) {
        super(accountId, accountNumber, customerId, balance, 0.05, branch);
    }

    public static boolean isValidInitialDeposit(double amount) {
        return amount >= MIN_INITIAL_DEPOSIT;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }
}