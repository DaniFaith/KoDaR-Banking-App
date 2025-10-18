import java.util.List;

public class CustomerController {
    private CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public boolean createCustomer(String firstName, String lastName, String address,
                                  String phoneNumber, String email, String employmentStatus,
                                  String companyName, String companyAddress) {
        Customer customer = new Customer(firstName, lastName, address, phoneNumber, email,
                employmentStatus, companyName, companyAddress);
        return customerDAO.createCustomer(customer);
    }

    public Customer getCustomer(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
}