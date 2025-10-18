public abstract class Account {
    protected int accountId;
    protected int customerId;
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected double interestRate;
    protected boolean isActive;

    public Account() {}

    public Account(int customerId, String accountNumber, double balance, String branch) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.isActive = true;
    }

    // Abstract methods
    public abstract boolean withdraw(double amount);
    public abstract void applyInterest();
    public abstract String getAccountType();

    // Concrete methods
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    // Getters and setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}