package model;

public interface Account {
    void deposit(double amount);
    boolean withdraw(double amount);
    double getBalance();
    double calculateInterest();
    String getAccountType();
}