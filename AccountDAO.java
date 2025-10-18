import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (customer_id, account_number, account_type, balance, branch, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, account.getCustomerId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());
            pstmt.setDouble(6, account.getInterestRate());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setAccountId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        Account account = null;

        try (Connection conn = DatabaseManager.getInstance().getConnection();
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

        try (Connection conn = DatabaseManager.getInstance().getConnection();
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

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Account createAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account;
        String accountType = rs.getString("account_type");

        switch (accountType) {
            case "SAVINGS":
                account = new SavingsAccount();
                break;
            case "INVESTMENT":
                account = new InvestmentAccount();
                break;
            case "CHEQUE":
                account = new ChequeAccount();
                break;
            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }

        account.setAccountId(rs.getInt("account_id"));
        account.setCustomerId(rs.getInt("customer_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setBalance(rs.getDouble("balance"));
        account.setBranch(rs.getString("branch"));
        account.setInterestRate(rs.getDouble("interest_rate"));
        account.setActive(rs.getBoolean("is_active"));

        return account;
    }
}