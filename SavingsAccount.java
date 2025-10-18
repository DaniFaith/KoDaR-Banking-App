public class SavingsAccount extends Account {
    private static final double SAVINGS_INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount() {
        super();
        this.interestRate = SAVINGS_INTEREST_RATE;
    }

    public SavingsAccount(int customerId, String accountNumber, double balance, String branch) {
        super(customerId, accountNumber, balance, branch);
        this.interestRate = SAVINGS_INTEREST_RATE;
    }

    @Override
    public boolean withdraw(double amount) {
        // Savings account does not allow withdrawals according to requirements
        return false;
    }

    @Override
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
}