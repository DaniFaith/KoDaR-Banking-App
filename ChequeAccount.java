public class ChequeAccount extends Account {

    public ChequeAccount() {
        super();
        this.interestRate = 0.0; // No interest for cheque accounts
    }

    public ChequeAccount(int customerId, String accountNumber, double balance, String branch) {
        super(customerId, accountNumber, balance, branch);
        this.interestRate = 0.0;
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
    public void applyInterest() {
        // Cheque accounts don't earn interest
    }

    @Override
    public String getAccountType() {
        return "CHEQUE";
    }
}