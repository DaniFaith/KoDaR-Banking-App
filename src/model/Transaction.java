package model;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String transactionType;
    private double amount;
    private String description;
    private LocalDateTime transactionDate;
    private String referenceNumber;

    public Transaction(int transactionId, int accountId, String transactionType,
                       double amount, String description, String referenceNumber) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.referenceNumber = referenceNumber;
        this.transactionDate = LocalDateTime.now();
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public int getAccountId() { return accountId; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public String getReferenceNumber() { return referenceNumber; }
}