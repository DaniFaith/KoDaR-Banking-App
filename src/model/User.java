package model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String password;
    private String userType; // STAFF or CUSTOMER
    private int customerId;
    private boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastLogin;

    public User(int userId, String username, String password, String userType,
                int customerId, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.customerId = customerId;
        this.isActive = isActive;
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
    public int getCustomerId() { return customerId; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getLastLogin() { return lastLogin; }

    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isStaff() {
        return "STAFF".equals(userType);
    }

    public boolean isCustomer() {
        return "CUSTOMER".equals(userType);
    }
}