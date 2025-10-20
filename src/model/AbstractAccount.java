package model;

import java.time.LocalDateTime;

public abstract class AbstractAccount implements Account {
    protected int accountId;
    protected String accountNumber;
    protected int customerId;
    protected double balance;
    protected double interestRate;
    protected String branch;
    protected boolean isActive;
    protected LocalDateTime openedDate;

    public AbstractAccount(int accountId, String accountNumber, int customerId,
                           double balance, double interestRate, String branch) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.interestRate = interestRate;
        this.branch = branch;
        this.isActive = true;
        this.openedDate = LocalDateTime.now();
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    @Override
    public abstract boolean withdraw(double amount);

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public double calculateInterest() {
        return balance * interestRate;
    }

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public int getCustomerId() { return customerId; }
    public double getInterestRate() { return interestRate; }
    public String getBranch() { return branch; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getOpenedDate() { return openedDate; }

    public void setActive(boolean active) { isActive = active; }
}