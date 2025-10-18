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

    public Customer() {}

    public Customer(String firstName, String lastName, String address, String phoneNumber,
                    String email, String employmentStatus, String companyName, String companyAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.employmentStatus = employmentStatus;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    // Getters and setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }
}