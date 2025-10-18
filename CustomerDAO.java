import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, address, phone_number, email, employment_status, company_name, company_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getEmploymentStatus());
            pstmt.setString(7, customer.getCompanyName());
            pstmt.setString(8, customer.getCompanyAddress());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setCustomerId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        Customer customer = null;

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));
                customer.setEmploymentStatus(rs.getString("employment_status"));
                customer.setCompanyName(rs.getString("company_name"));
                customer.setCompanyAddress(rs.getString("company_address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setEmail(rs.getString("email"));
                customer.setEmploymentStatus(rs.getString("employment_status"));
                customer.setCompanyName(rs.getString("company_name"));
                customer.setCompanyAddress(rs.getString("company_address"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}