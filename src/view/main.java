package view;

import controller.BankingController;
import model.Customer;
import model.Account;
import model.Transaction;
import model.AbstractAccount;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.FXCollections;
import java.util.List;

public class Main extends Application {

    private BankingController controller;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.controller = new BankingController();
        this.primaryStage = primaryStage;

        // Test database connection
        testDatabaseConnection();

        primaryStage.setTitle("KoDaR Banking System");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        showLoginScreen();
        primaryStage.show();
    }

    private void testDatabaseConnection() {
        try {
            dao.DatabaseConnection.getConnection();
            System.out.println("✅ Database connection successful!");
        } catch (Exception e) {
            showAlert("Database Error", "Cannot connect to database. Please ensure:\n" +
                    "1. XAMPP is running\n" +
                    "2. MySQL is started\n" +
                    "3. Database 'kodar_banking' exists\n\n" +
                    "Error: " + e.getMessage());
        }
    }

    private void showLoginScreen() {
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");

        // Login panel
        VBox loginContainer = new VBox(30);
        loginContainer.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-radius: 15; " +
                "-fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        loginContainer.setPadding(new Insets(40));
        loginContainer.setMaxWidth(400);

        // Logo and title
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label logoLabel = new Label("KᴑDₐR");
        logoLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label titleLabel = new Label("Banking System Login");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");

        headerBox.getChildren().addAll(logoLabel, titleLabel);

        // Login form
        VBox formBox = new VBox(20);

        TextField usernameField = createFormField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        passwordField.setPromptText("Password");

        Button loginButton = createPrimaryButton("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password");
                return;
            }

            if (controller.login(username, password)) {
                if (controller.isStaffLoggedIn()) {
                    showStaffDashboard();
                } else if (controller.isCustomerLoggedIn()) {
                    showCustomerDashboard();
                }
            } else {
                errorLabel.setText("Invalid username or password");
            }
        });

        // Demo credentials
        VBox demoBox = new VBox(10);
        demoBox.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 8; -fx-padding: 15;");
        Label demoLabel = new Label("Demo Credentials:");
        demoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label staffDemo = new Label("Staff: admin / admin123");
        staffDemo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        Label customerDemo = new Label("Customer: kagiso / customer123");
        customerDemo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        demoBox.getChildren().addAll(demoLabel, staffDemo, customerDemo);

        formBox.getChildren().addAll(
                createFormLabel("Username"), usernameField,
                createFormLabel("Password"), passwordField,
                loginButton, errorLabel, demoBox
        );

        loginContainer.getChildren().addAll(headerBox, formBox);
        mainContainer.getChildren().add(loginContainer);

        Scene scene = new Scene(mainContainer, 1000, 700);
        primaryStage.setScene(scene);
    }

    private TextField createFormField(String promptText) {
        TextField field = new TextField();
        field.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        field.setPromptText(promptText);
        return field;
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #34495e; -fx-font-weight: 600; -fx-font-size: 13px;");
        return label;
    }

    private Button createPrimaryButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 12 25; " +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: linear-gradient(to bottom, #2980b9, #2573a7); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; " +
                    "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 12 25; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(52, 152, 219, 0.4), 10, 0, 0, 0);");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; " +
                    "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 12 25;");
        });

        return button;
    }

    private Button createSuccessButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #27ae60, #229954); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; " +
                "-fx-border-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        return button;
    }

    private Button createDangerButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; " +
                "-fx-border-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        return button;
    }

    private void showCustomerDashboard() {
        CustomerDashboard customerDashboard = new CustomerDashboard(primaryStage, controller);
        customerDashboard.show();
    }

    private void showStaffDashboard() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #34495e); " +
                "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(5);
        Label titleLabel = new Label("KoDaR Banking System");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Staff Dashboard - Managing Botswana's Finances");
        subtitleLabel.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 14px;");

        headerText.getChildren().addAll(titleLabel, subtitleLabel);

        Button logoutButton = createLogoutButton();
        HBox.setHgrow(headerText, Priority.ALWAYS);

        header.getChildren().addAll(headerText, logoutButton);

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #34495e;");

        Menu customerMenu = new Menu("Customer Management");
        customerMenu.setStyle("-fx-text-fill: white;");
        MenuItem createCustomer = new MenuItem("Create New Customer");
        MenuItem viewCustomers = new MenuItem("View All Customers");

        customerMenu.getItems().addAll(createCustomer, viewCustomers);

        Menu accountMenu = new Menu("Account Management");
        accountMenu.setStyle("-fx-text-fill: white;");
        MenuItem createAccount = new MenuItem("Create New Account");
        MenuItem viewAccounts = new MenuItem("View All Accounts");
        MenuItem closeAccount = new MenuItem("Close Account");

        accountMenu.getItems().addAll(createAccount, viewAccounts, closeAccount);

        Menu transactionMenu = new Menu("Transactions");
        transactionMenu.setStyle("-fx-text-fill: white;");
        MenuItem processDeposit = new MenuItem("Process Deposit");
        MenuItem processWithdrawal = new MenuItem("Process Withdrawal");
        MenuItem processTransfer = new MenuItem("Process Transfer");
        MenuItem applyInterest = new MenuItem("Apply Monthly Interest");
        MenuItem viewTransactions = new MenuItem("View All Transactions");

        transactionMenu.getItems().addAll(processDeposit, processWithdrawal, processTransfer, applyInterest, viewTransactions);

        menuBar.getMenus().addAll(customerMenu, accountMenu, transactionMenu);

        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menuBar, header);
        layout.setTop(topContainer);

        // Center content with dashboard cards
        GridPane dashboardGrid = new GridPane();
        dashboardGrid.setPadding(new Insets(30));
        dashboardGrid.setHgap(20);
        dashboardGrid.setVgap(20);
        dashboardGrid.setAlignment(Pos.CENTER);

        // Get actual statistics
        List<Customer> customers = controller.getAllCustomers();
        List<Account> accounts = controller.getAllAccounts();
        double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();

        VBox totalCustomersCard = createDashboardCard("Total Customers", String.valueOf(customers.size()), "Registered customers");
        VBox totalAccountsCard = createDashboardCard("Total Accounts", String.valueOf(accounts.size()), "All account types");
        VBox totalBalanceCard = createDashboardCard("Total Balance", String.format("P %.2f", totalBalance), "Combined assets");
        VBox activeUsersCard = createDashboardCard("Active Today", "15", "User sessions");

        dashboardGrid.add(totalCustomersCard, 0, 0);
        dashboardGrid.add(totalAccountsCard, 1, 0);
        dashboardGrid.add(totalBalanceCard, 0, 1);
        dashboardGrid.add(activeUsersCard, 1, 1);

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to KoDaR Banking Staff Portal");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        centerContent.getChildren().addAll(welcomeLabel, dashboardGrid);
        layout.setCenter(centerContent);

        // Event handlers
        createCustomer.setOnAction(e -> showCreateCustomerForm());
        viewCustomers.setOnAction(e -> showAllCustomers());
        createAccount.setOnAction(e -> showCreateAccountForm());
        viewAccounts.setOnAction(e -> showAllAccounts());
        processDeposit.setOnAction(e -> showDepositForm());
        processWithdrawal.setOnAction(e -> showWithdrawalForm());
        processTransfer.setOnAction(e -> showStaffTransferForm());
        viewTransactions.setOnAction(e -> showAllTransactions());
        applyInterest.setOnAction(e -> {
            controller.applyInterest();
            showAlert("Success", "Monthly interest has been applied to all eligible accounts.");
        });
        closeAccount.setOnAction(e -> showCloseAccountForm());

        Scene scene = new Scene(layout, 1200, 800);
        primaryStage.setScene(scene);
    }

    private Button createLogoutButton() {
        Button button = new Button("Logout");
        button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 10 20;");
        button.setOnAction(e -> {
            controller.logout();
            showLoginScreen();
        });
        return button;
    }

    private VBox createDashboardCard(String title, String value, String subtitle) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250, 150);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);
        return card;
    }

    private void showCreateCustomerForm() {
        Stage stage = new Stage();
        stage.setTitle("Create New Customer - KoDaR Banking");
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Create New Customer");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 15 0;");

        GridPane formGrid = new GridPane();
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        formGrid.setPadding(new Insets(25));
        formGrid.setVgap(15);
        formGrid.setHgap(15);

        // Form fields
        TextField firstNameField = createFormField("Enter first name");
        TextField lastNameField = createFormField("Enter last name");
        TextField addressField = createFormField("Enter full address");
        TextField phoneField = createFormField("Enter phone number");
        TextField emailField = createFormField("Enter email address");

        ComboBox<String> employmentCombo = new ComboBox<>();
        employmentCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        employmentCombo.getItems().addAll("EMPLOYED", "SELF_EMPLOYED", "UNEMPLOYED", "RETIRED", "STUDENT");
        employmentCombo.setPromptText("Select employment status");

        TextField companyField = createFormField("Enter company name (if employed)");
        TextField companyAddressField = createFormField("Enter company address");

        // Add form elements to grid
        formGrid.add(createFormLabel("First Name:"), 0, 0);
        formGrid.add(firstNameField, 1, 0);
        formGrid.add(createFormLabel("Last Name:"), 0, 1);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(createFormLabel("Address:"), 0, 2);
        formGrid.add(addressField, 1, 2);
        formGrid.add(createFormLabel("Phone:"), 0, 3);
        formGrid.add(phoneField, 1, 3);
        formGrid.add(createFormLabel("Email:"), 0, 4);
        formGrid.add(emailField, 1, 4);
        formGrid.add(createFormLabel("Employment:"), 0, 5);
        formGrid.add(employmentCombo, 1, 5);
        formGrid.add(createFormLabel("Company:"), 0, 6);
        formGrid.add(companyField, 1, 6);
        formGrid.add(createFormLabel("Company Address:"), 0, 7);
        formGrid.add(companyAddressField, 1, 7);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = createSuccessButton("Create Customer");
        Button cancelButton = createDangerButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> {
            if (validateCustomerForm(firstNameField, lastNameField, phoneField)) {
                boolean success = controller.createCustomer(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        addressField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        employmentCombo.getValue(),
                        companyField.getText(),
                        companyAddressField.getText()
                );

                if (success) {
                    showAlert("Success", "Customer created successfully!");
                    stage.close();
                } else {
                    showAlert("Error", "Failed to create customer. Please check the information.");
                }
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 8);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    private boolean validateCustomerForm(TextField firstName, TextField lastName, TextField phone) {
        if (firstName.getText().isEmpty()) {
            showAlert("Validation Error", "First name is required.");
            return false;
        }
        if (lastName.getText().isEmpty()) {
            showAlert("Validation Error", "Last name is required.");
            return false;
        }
        if (phone.getText().isEmpty()) {
            showAlert("Validation Error", "Phone number is required.");
            return false;
        }
        return true;
    }

    private void showAllCustomers() {
        Stage stage = new Stage();
        stage.setTitle("All Customers - KoDaR Banking");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Customer Management");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Customer> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");
        table.setPlaceholder(new Label("No customers found"));

        TableColumn<Customer, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().getCustomerId())));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getFirstName() + " " + cell.getValue().getLastName()));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhoneNumber()));

        TableColumn<Customer, String> employmentCol = new TableColumn<>("Employment");
        employmentCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmploymentStatus()));

        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, employmentCol);

        // Style table headers
        for (TableColumn<?, ?> column : table.getColumns()) {
            column.setStyle("-fx-background-color: linear-gradient(to bottom, #34495e, #2c3e50); " +
                    "-fx-text-fill: white; -fx-font-weight: bold;");
        }

        // Load data
        table.getItems().addAll(controller.getAllCustomers());

        layout.getChildren().addAll(titleLabel, table);
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showCreateAccountForm() {
        Stage stage = new Stage();
        stage.setTitle("Create New Account - KoDaR Banking");
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Create New Account");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        formGrid.setPadding(new Insets(25));
        formGrid.setVgap(15);
        formGrid.setHgap(15);

        TextField customerIdField = createFormField("Enter customer ID");
        TextField accountNumberField = createFormField("Enter account number");

        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        accountTypeCombo.getItems().addAll("SAVINGS", "INVESTMENT", "CHEQUE");
        accountTypeCombo.setPromptText("Select account type");

        TextField initialDepositField = createFormField("Enter initial deposit");
        TextField branchField = createFormField("Enter branch name");

        formGrid.add(createFormLabel("Customer ID:"), 0, 0);
        formGrid.add(customerIdField, 1, 0);
        formGrid.add(createFormLabel("Account Number:"), 0, 1);
        formGrid.add(accountNumberField, 1, 1);
        formGrid.add(createFormLabel("Account Type:"), 0, 2);
        formGrid.add(accountTypeCombo, 1, 2);
        formGrid.add(createFormLabel("Initial Deposit:"), 0, 3);
        formGrid.add(initialDepositField, 1, 3);
        formGrid.add(createFormLabel("Branch:"), 0, 4);
        formGrid.add(branchField, 1, 4);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = createSuccessButton("Create Account");
        Button cancelButton = createDangerButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> {
            try {
                int customerId = Integer.parseInt(customerIdField.getText());
                double initialDeposit = Double.parseDouble(initialDepositField.getText());
                String accountType = accountTypeCombo.getValue();
                boolean success = false;

                switch (accountType) {
                    case "SAVINGS":
                        success = controller.createSavingsAccount(customerId, accountNumberField.getText(), initialDeposit, branchField.getText());
                        break;
                    case "INVESTMENT":
                        success = controller.createInvestmentAccount(customerId, accountNumberField.getText(), initialDeposit, branchField.getText());
                        break;
                    case "CHEQUE":
                        success = controller.createChequeAccount(customerId, accountNumberField.getText(), initialDeposit, branchField.getText());
                        break;
                }

                if (success) {
                    showAlert("Success", "Account created successfully!");
                    stage.close();
                } else {
                    showAlert("Error", "Failed to create account. Please check:\n" +
                            "- Customer ID exists\n" +
                            "- Initial deposit meets requirements\n" +
                            "- Employment status for cheque accounts");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numbers for Customer ID and Initial Deposit.");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 5);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showAllAccounts() {
        Stage stage = new Stage();
        stage.setTitle("All Accounts - KoDaR Banking");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("All Bank Accounts");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-font-weight: bold;");

        TableView<Account> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");
        table.setPlaceholder(new Label("No accounts found"));

        // Columns
        TableColumn<Account, String> accNumCol = new TableColumn<>("Account Number");
        accNumCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                ((AbstractAccount)cell.getValue()).getAccountNumber()));

        TableColumn<Account, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getAccountType()));

        TableColumn<Account, String> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                String.format("P %.2f", cell.getValue().getBalance())));

        TableColumn<Account, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                ((AbstractAccount)cell.getValue()).getBranch()));

        TableColumn<Account, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(cell -> {
            Account account = cell.getValue();
            Customer customer = controller.getCustomer(((AbstractAccount)account).getCustomerId());
            return new javafx.beans.property.SimpleStringProperty(
                    customer != null ? customer.getFirstName() + " " + customer.getLastName() : "Unknown");
        });

        TableColumn<Account, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                ((AbstractAccount)cell.getValue()).isActive() ? "Active" : "Closed"));

        table.getColumns().addAll(accNumCol, typeCol, balanceCol, branchCol, customerCol, statusCol);

        // Load data
        List<Account> accounts = controller.getAllAccounts();
        if (accounts != null) {
            table.getItems().addAll(accounts);
        }

        layout.getChildren().addAll(titleLabel, table);
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showAllTransactions() {
        Stage stage = new Stage();
        stage.setTitle("All Transactions - KoDaR Banking");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("All Transactions");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-font-weight: bold;");

        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");
        table.setPlaceholder(new Label("No transactions found"));

        // Columns
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getTransactionDate().toString()));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getTransactionType()));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                String.format("P %.2f", cell.getValue().getAmount())));

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getDescription()));

        TableColumn<Transaction, String> refCol = new TableColumn<>("Reference");
        refCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getReferenceNumber()));

        TableColumn<Transaction, String> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(cell -> {
            Transaction transaction = cell.getValue();
            Account account = controller.getAccountById(transaction.getAccountId());
            return new javafx.beans.property.SimpleStringProperty(
                    account != null ? ((AbstractAccount)account).getAccountNumber() : "Unknown");
        });

        table.getColumns().addAll(dateCol, typeCol, amountCol, descCol, refCol, accountCol);

        // Load data
        List<Transaction> transactions = controller.getAllTransactions();
        if (transactions != null) {
            table.getItems().addAll(transactions);
        }

        layout.getChildren().addAll(titleLabel, table);
        Scene scene = new Scene(layout, 1200, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showDepositForm() {
        Stage stage = new Stage();
        stage.setTitle("Process Deposit - KoDaR Banking");
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Process Deposit");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        formGrid.setPadding(new Insets(25));
        formGrid.setVgap(15);
        formGrid.setHgap(15);

        ComboBox<String> accountCombo = new ComboBox<>();
        accountCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        accountCombo.setPromptText("Select account");

        TextField amountField = createFormField("Enter amount");
        TextField descriptionField = createFormField("Enter description");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        // Load account numbers
        List<String> accountNumbers = controller.getAllAccountNumbers();
        accountCombo.getItems().addAll(accountNumbers);

        formGrid.add(createFormLabel("Account:"), 0, 0);
        formGrid.add(accountCombo, 1, 0);
        formGrid.add(createFormLabel("Amount:"), 0, 1);
        formGrid.add(amountField, 1, 1);
        formGrid.add(createFormLabel("Description:"), 0, 2);
        formGrid.add(descriptionField, 1, 2);
        formGrid.add(messageLabel, 1, 3);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = createSuccessButton("Process Deposit");
        Button cancelButton = createDangerButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> {
            String accountNumber = accountCombo.getValue();
            String amountText = amountField.getText();
            String description = descriptionField.getText();

            if (accountNumber == null || amountText.isEmpty() || description.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    messageLabel.setText("Amount must be positive");
                    return;
                }

                int accountId = controller.getAccountIdByNumber(accountNumber);
                if (accountId == -1) {
                    messageLabel.setText("Invalid account number");
                    return;
                }

                boolean success = controller.processDeposit(accountId, amount, description);
                if (success) {
                    messageLabel.setStyle("-fx-text-fill: #27ae60;");
                    messageLabel.setText("Deposit processed successfully!");
                    amountField.clear();
                    descriptionField.clear();
                } else {
                    messageLabel.setText("Deposit failed. Please check the information.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid amount");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 4);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showWithdrawalForm() {
        Stage stage = new Stage();
        stage.setTitle("Process Withdrawal - KoDaR Banking");
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Process Withdrawal");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        formGrid.setPadding(new Insets(25));
        formGrid.setVgap(15);
        formGrid.setHgap(15);

        ComboBox<String> accountCombo = new ComboBox<>();
        accountCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        accountCombo.setPromptText("Select account");

        TextField amountField = createFormField("Enter amount");
        TextField descriptionField = createFormField("Enter description");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        // Load account numbers
        List<String> accountNumbers = controller.getAllAccountNumbers();
        accountCombo.getItems().addAll(accountNumbers);

        formGrid.add(createFormLabel("Account:"), 0, 0);
        formGrid.add(accountCombo, 1, 0);
        formGrid.add(createFormLabel("Amount:"), 0, 1);
        formGrid.add(amountField, 1, 1);
        formGrid.add(createFormLabel("Description:"), 0, 2);
        formGrid.add(descriptionField, 1, 2);
        formGrid.add(messageLabel, 1, 3);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = createSuccessButton("Process Withdrawal");
        Button cancelButton = createDangerButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> {
            String accountNumber = accountCombo.getValue();
            String amountText = amountField.getText();
            String description = descriptionField.getText();

            if (accountNumber == null || amountText.isEmpty() || description.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    messageLabel.setText("Amount must be positive");
                    return;
                }

                int accountId = controller.getAccountIdByNumber(accountNumber);
                if (accountId == -1) {
                    messageLabel.setText("Invalid account number");
                    return;
                }

                boolean success = controller.processWithdrawal(accountId, amount, description);
                if (success) {
                    messageLabel.setStyle("-fx-text-fill: #27ae60;");
                    messageLabel.setText("Withdrawal processed successfully!");
                    amountField.clear();
                    descriptionField.clear();
                } else {
                    messageLabel.setText("Withdrawal failed. Check account balance and type.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid amount");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 4);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showStaffTransferForm() {
        Stage stage = new Stage();
        stage.setTitle("Process Transfer - KoDaR Banking");
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Process Transfer");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        formGrid.setPadding(new Insets(25));
        formGrid.setVgap(15);
        formGrid.setHgap(15);

        ComboBox<String> fromAccountCombo = new ComboBox<>();
        fromAccountCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        fromAccountCombo.setPromptText("Select source account");

        ComboBox<String> toAccountCombo = new ComboBox<>();
        toAccountCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        toAccountCombo.setPromptText("Select destination account");

        TextField amountField = createFormField("Enter amount");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        // Load account numbers
        List<String> accountNumbers = controller.getAllAccountNumbers();
        fromAccountCombo.getItems().addAll(accountNumbers);
        toAccountCombo.getItems().addAll(accountNumbers);

        formGrid.add(createFormLabel("From Account:"), 0, 0);
        formGrid.add(fromAccountCombo, 1, 0);
        formGrid.add(createFormLabel("To Account:"), 0, 1);
        formGrid.add(toAccountCombo, 1, 1);
        formGrid.add(createFormLabel("Amount:"), 0, 2);
        formGrid.add(amountField, 1, 2);
        formGrid.add(messageLabel, 1, 3);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = createSuccessButton("Process Transfer");
        Button cancelButton = createDangerButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> {
            String fromAccount = fromAccountCombo.getValue();
            String toAccount = toAccountCombo.getValue();
            String amountText = amountField.getText();

            if (fromAccount == null || toAccount == null || amountText.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                return;
            }

            if (fromAccount.equals(toAccount)) {
                messageLabel.setText("Source and destination accounts cannot be the same");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    messageLabel.setText("Amount must be positive");
                    return;
                }

                boolean success = controller.customerTransfer(fromAccount, toAccount, amount);
                if (success) {
                    messageLabel.setStyle("-fx-text-fill: #27ae60;");
                    messageLabel.setText("Transfer processed successfully!");
                    amountField.clear();
                } else {
                    messageLabel.setText("Transfer failed. Check account numbers and balance.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid amount");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 4);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showCloseAccountForm() {
        Stage stage = new Stage();
        stage.setTitle("Close Account - KoDaR Banking");
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Close Account");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        formGrid.setPadding(new Insets(25));
        formGrid.setVgap(15);
        formGrid.setHgap(15);

        ComboBox<String> accountCombo = new ComboBox<>();
        accountCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        accountCombo.setPromptText("Select account to close");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        // Load active account numbers
        List<Account> accounts = controller.getAllAccounts();
        for (Account account : accounts) {
            if (((AbstractAccount)account).isActive()) {
                accountCombo.getItems().add(((AbstractAccount)account).getAccountNumber());
            }
        }

        formGrid.add(createFormLabel("Account:"), 0, 0);
        formGrid.add(accountCombo, 1, 0);
        formGrid.add(messageLabel, 1, 1);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = createDangerButton("Close Account");
        Button cancelButton = createPrimaryButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> {
            String accountNumber = accountCombo.getValue();

            if (accountNumber == null) {
                messageLabel.setText("Please select an account");
                return;
            }

            int accountId = controller.getAccountIdByNumber(accountNumber);
            if (accountId == -1) {
                messageLabel.setText("Invalid account number");
                return;
            }

            boolean success = controller.closeAccount(accountId);
            if (success) {
                messageLabel.setStyle("-fx-text-fill: #27ae60;");
                messageLabel.setText("Account closed successfully!");
                accountCombo.getItems().remove(accountNumber);
            } else {
                messageLabel.setText("Failed to close account.");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 2);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
