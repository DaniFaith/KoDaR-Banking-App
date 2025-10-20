package model;

public class SavingsAccount extends AbstractAccount {
    public SavingsAccount(int accountId, String accountNumber, int customerId,
                          double balance, String branch) {
        super(accountId, accountNumber, customerId, balance, 0.0005, branch);
    }

    @Override
    public boolean withdraw(double amount) {
        // Savings account does not allow withdrawals according to requirements
        return false;
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
}