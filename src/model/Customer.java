package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private String employmentStatus;
    private String companyName;
    private String companyAddress;
    private LocalDateTime createdDate;
    private List<Account> accounts;

    public Customer(int customerId, String firstName, String lastName, String address,
                    String phoneNumber, String email, String employmentStatus,
                    String companyName, String companyAddress) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.employmentStatus = employmentStatus;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.createdDate = LocalDateTime.now();
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public boolean canOpenChequeAccount() {
        return "EMPLOYED".equals(employmentStatus) || "SELF_EMPLOYED".equals(employmentStatus);
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getEmploymentStatus() { return employmentStatus; }
    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
    public LocalDateTime getCreatedDate() { return createdDate; }

    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }
}