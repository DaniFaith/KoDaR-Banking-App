package dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (customer_id, account_number, account_type, balance, interest_rate, branch) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ((AbstractAccount) account).getCustomerId());
            pstmt.setString(2, ((AbstractAccount) account).getAccountNumber());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setDouble(5, ((AbstractAccount) account).getInterestRate());
            pstmt.setString(6, ((AbstractAccount) account).getBranch());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        Account account = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                account = createAccountFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Account account = createAccountFromResultSet(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean updateAccountBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean closeAccount(int accountId) {
        String sql = "UPDATE accounts SET is_active = FALSE WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Account createAccountFromResultSet(ResultSet rs) throws SQLException {
        int accountId = rs.getInt("account_id");
        String accountNumber = rs.getString("account_number");
        int customerId = rs.getInt("customer_id");
        String accountType = rs.getString("account_type");
        double balance = rs.getDouble("balance");
        double interestRate = rs.getDouble("interest_rate");
        String branch = rs.getString("branch");

        switch (accountType) {
            case "SAVINGS":
                return new SavingsAccount(accountId, accountNumber, customerId, balance, branch);
            case "INVESTMENT":
                return new InvestmentAccount(accountId, accountNumber, customerId, balance, branch);
            case "CHEQUE":
                return new ChequeAccount(accountId, accountNumber, customerId, balance, branch);
            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
    }
}