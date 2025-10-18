public class InvestmentAccount extends Account {
    private static final double INVESTMENT_INTEREST_RATE = 0.05; // 5%
    private static final double MIN_OPENING_BALANCE = 500.00;

    public InvestmentAccount() {
        super();
        this.interestRate = INVESTMENT_INTEREST_RATE;
    }

    public InvestmentAccount(int customerId, String accountNumber, double balance, String branch) {
        super(customerId, accountNumber, balance, branch);
        this.interestRate = INVESTMENT_INTEREST_RATE;
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
        double interest = balance * interestRate;
        balance += interest;
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }

    public static double getMinOpeningBalance() {
        return MIN_OPENING_BALANCE;
    }
}