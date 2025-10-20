package model;

public class ChequeAccount extends AbstractAccount {
    public ChequeAccount(int accountId, String accountNumber, int customerId,
                         double balance, String branch) {
        super(accountId, accountNumber, customerId, balance, 0.0, branch);
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
        return "CHEQUE";
    }
}