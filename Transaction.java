import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String transactionType;
    private double amount;
    private String description;
    private LocalDateTime transactionDate;
    private String referenceNumber;

    public Transaction() {}

    public Transaction(int accountId, String transactionType, double amount, String description) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
        this.referenceNumber = generateReferenceNumber();
    }

    private String generateReferenceNumber() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    // Getters and setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}