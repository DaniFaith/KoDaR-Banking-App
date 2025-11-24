package view;

import controller.BankingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StaffDashboard {
    private Main main;
    private BankingController controller;
    private UIHelper uiHelper;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private TransactionManager transactionManager;

    public StaffDashboard(Main main, BankingController controller) {
        this.main = main;
        this.controller = controller;
        this.uiHelper = new UIHelper();
        this.customerManager = new CustomerManager(controller, uiHelper, main);
        this.accountManager = new AccountManager(controller, uiHelper, main);
        this.transactionManager = new TransactionManager(controller, uiHelper, main);
    }

    public void show(Stage primaryStage) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8f9fa;");

        layout.setTop(createHeader());
        layout.setCenter(createDashboardContent());

        Scene scene = new Scene(layout, 1200, 800);
        primaryStage.setScene(scene);
    }

    private VBox createHeader() {
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

        MenuBar menuBar = createMenuBar();
        
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menuBar, header);
        return topContainer;
    }

    private MenuBar createMenuBar() {
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

        // Event handlers
        createCustomer.setOnAction(e -> customerManager.showCreateCustomerForm());
        viewCustomers.setOnAction(e -> customerManager.showAllCustomers());
        createAccount.setOnAction(e -> accountManager.showCreateAccountForm());
        viewAccounts.setOnAction(e -> accountManager.showAllAccounts());
        processDeposit.setOnAction(e -> transactionManager.showDepositForm());
        processWithdrawal.setOnAction(e -> transactionManager.showWithdrawalForm());
        processTransfer.setOnAction(e -> transactionManager.showStaffTransferForm());
        viewTransactions.setOnAction(e -> transactionManager.showAllTransactions());
        applyInterest.setOnAction(e -> {
            controller.applyInterest();
            main.showAlert("Success", "Monthly interest has been applied to all eligible accounts.");
        });
        closeAccount.setOnAction(e -> accountManager.showCloseAccountForm());

        return menuBar;
    }

    private VBox createDashboardContent() {
        GridPane dashboardGrid = new GridPane();
        dashboardGrid.setPadding(new Insets(30));
        dashboardGrid.setHgap(20);
        dashboardGrid.setVgap(20);
        dashboardGrid.setAlignment(Pos.CENTER);

        java.util.List<model.Customer> customers = controller.getAllCustomers();
        java.util.List<model.Account> accounts = controller.getAllAccounts();
        double totalBalance = accounts.stream().mapToDouble(model.Account::getBalance).sum();

        VBox totalCustomersCard = uiHelper.createDashboardCard("Total Customers", String.valueOf(customers.size()), "Registered customers");
        VBox totalAccountsCard = uiHelper.createDashboardCard("Total Accounts", String.valueOf(accounts.size()), "All account types");
        VBox totalBalanceCard = uiHelper.createDashboardCard("Total Balance", String.format("P %.2f", totalBalance), "Combined assets");
        VBox activeUsersCard = uiHelper.createDashboardCard("Active Today", "15", "User sessions");

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
        return centerContent;
    }

    private Button createLogoutButton() {
        Button button = new Button("Logout");
        button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 10 20;");
        button.setOnAction(e -> {
            controller.logout();
            main.showLoginScreen();
        });
        return button;
    }
}
