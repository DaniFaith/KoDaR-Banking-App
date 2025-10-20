package view;

import controller.BankingController;
import model.Customer;
import model.Account;
import model.Transaction;
import model.AbstractAccount;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.FXCollections;
import java.util.List;

public class CustomerDashboard {
    private Stage stage;
    private BankingController controller;
    private Customer currentCustomer;

    public CustomerDashboard(Stage stage, BankingController controller) {
        this.stage = stage;
        this.controller = controller;
        this.currentCustomer = controller.getCurrentCustomer();
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader();
        layout.setTop(header);

        // Main content
        TabPane tabPane = createMainTabs();
        layout.setCenter(tabPane);

        Scene scene = new Scene(layout, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("KoDaR Banking - Customer Portal");
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #34495e); " +
                "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(5);
        Label titleLabel = new Label("KoDaR Banking - Customer Portal");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label welcomeLabel = new Label("Welcome, " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
        welcomeLabel.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 16px;");

        headerText.getChildren().addAll(titleLabel, welcomeLabel);

        Button logoutButton = createLogoutButton();
        HBox.setHgrow(headerText, Priority.ALWAYS);

        header.getChildren().addAll(headerText, logoutButton);
        return header;
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

    private void showLoginScreen() {
        Main main = new Main();
        main.start(stage);
    }

    private TabPane createMainTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");

        // Dashboard Tab
        Tab dashboardTab = new Tab("Dashboard");
        dashboardTab.setContent(createDashboardContent());
        dashboardTab.setClosable(false);

        // Accounts Tab
        Tab accountsTab = new Tab("My Accounts");
        accountsTab.setContent(createAccountsContent());
        accountsTab.setClosable(false);

        // Transactions Tab
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setContent(createTransactionsContent());
        transactionsTab.setClosable(false);

        // Transfer Tab
        Tab transferTab = new Tab("Transfer Funds");
        transferTab.setContent(createTransferContent());
        transferTab.setClosable(false);

        // Profile Tab
        Tab profileTab = new Tab("My Profile");
        profileTab.setContent(createProfileContent());
        profileTab.setClosable(false);

        tabPane.getTabs().addAll(dashboardTab, accountsTab, transactionsTab, transferTab, profileTab);
        return tabPane;
    }

    private VBox createDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label titleLabel = new Label("Account Overview");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        // Account summary cards
        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(20);
        cardsGrid.setVgap(20);
        cardsGrid.setAlignment(Pos.CENTER);

        List<Account> accounts = controller.getCurrentCustomerAccounts();
        double totalBalance = 0;
        int totalAccounts = 0;

        if (accounts != null) {
            totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
            totalAccounts = accounts.size();
        }

        VBox balanceCard = createDashboardCard("Total Balance", String.format("P %.2f", totalBalance), "All Accounts");
        VBox accountsCard = createDashboardCard("Number of Accounts", String.valueOf(totalAccounts), "Active Accounts");
        VBox recentActivityCard = createDashboardCard("Recent Transactions", "View History", "Check your transactions");

        cardsGrid.add(balanceCard, 0, 0);
        cardsGrid.add(accountsCard, 1, 0);
        cardsGrid.add(recentActivityCard, 2, 0);

        // Quick actions
        VBox quickActions = createQuickActions();

        content.getChildren().addAll(titleLabel, cardsGrid, quickActions);
        return content;
    }

    private VBox createDashboardCard(String title, String value, String subtitle) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(200, 120);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);
        return card;
    }

    private VBox createQuickActions() {
        VBox actionsBox = new VBox(15);
        actionsBox.setPadding(new Insets(20, 0, 0, 0));

        Label titleLabel = new Label("Quick Actions");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);

        Button viewAccountsBtn = createActionButton("View Accounts");
        Button transferBtn = createActionButton("Transfer Money");
        Button statementBtn = createActionButton("View Statement");

        viewAccountsBtn.setOnAction(e -> switchToTab(1)); // Accounts tab
        transferBtn.setOnAction(e -> switchToTab(3)); // Transfer tab
        statementBtn.setOnAction(e -> switchToTab(2)); // Transactions tab

        buttonsBox.getChildren().addAll(viewAccountsBtn, transferBtn, statementBtn);
        actionsBox.getChildren().addAll(titleLabel, buttonsBox);

        return actionsBox;
    }

    private void switchToTab(int tabIndex) {
        TabPane tabPane = (TabPane) ((BorderPane) stage.getScene().getRoot()).getCenter();
        tabPane.getSelectionModel().select(tabIndex);
    }

    private Button createActionButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 10 20;");
        return button;
    }

    private ScrollPane createAccountsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label titleLabel = new Label("My Accounts");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        TableView<Account> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");

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

        table.getColumns().addAll(accNumCol, typeCol, balanceCol, branchCol);

        // Load accounts
        List<Account> accounts = controller.getCurrentCustomerAccounts();
        if (accounts != null) {
            table.getItems().addAll(accounts);
        }

        content.getChildren().addAll(titleLabel, table);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createTransactionsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label titleLabel = new Label("Recent Transactions");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");

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

        table.getColumns().addAll(dateCol, typeCol, amountCol, descCol, refCol);

        // Load transactions
        List<Transaction> transactions = controller.getCurrentCustomerTransactions();
        if (transactions != null) {
            table.getItems().addAll(transactions);
        } else {
            table.setPlaceholder(new Label("No transactions found"));
        }

        content.getChildren().addAll(titleLabel, table);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createTransferContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label titleLabel = new Label("Transfer Funds");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

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
        fromAccountCombo.setPromptText("Select your account");

        TextField toAccountField = new TextField();
        toAccountField.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        toAccountField.setPromptText("Enter destination account number");

        TextField amountField = new TextField();
        amountField.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        amountField.setPromptText("Enter amount");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        // Load current customer's accounts
        List<Account> accounts = controller.getCurrentCustomerAccounts();
        if (accounts != null) {
            for (Account account : accounts) {
                fromAccountCombo.getItems().add(((AbstractAccount)account).getAccountNumber());
            }
        }

        formGrid.add(createFormLabel("From Account:"), 0, 0);
        formGrid.add(fromAccountCombo, 1, 0);
        formGrid.add(createFormLabel("To Account:"), 0, 1);
        formGrid.add(toAccountField, 1, 1);
        formGrid.add(createFormLabel("Amount:"), 0, 2);
        formGrid.add(amountField, 1, 2);
        formGrid.add(messageLabel, 1, 3);

        Button transferButton = new Button("Transfer Funds");
        transferButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 12 25;");

        transferButton.setOnAction(e -> {
            String fromAccount = fromAccountCombo.getValue();
            String toAccount = toAccountField.getText();
            String amountText = amountField.getText();

            if (fromAccount == null || toAccount.isEmpty() || amountText.isEmpty()) {
                messageLabel.setText("Please fill all fields");
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
                    messageLabel.setText("Transfer successful!");
                    // Clear fields
                    toAccountField.clear();
                    amountField.clear();
                } else {
                    messageLabel.setText("Transfer failed. Check account numbers and balance.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid amount");
            }
        });

        formGrid.add(transferButton, 1, 4);

        content.getChildren().addAll(titleLabel, formGrid);
        return content;
    }

    private VBox createProfileContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label titleLabel = new Label("My Profile");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        GridPane profileGrid = new GridPane();
        profileGrid.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 0);");
        profileGrid.setPadding(new Insets(25));
        profileGrid.setVgap(15);
        profileGrid.setHgap(15);

        // Display customer information
        profileGrid.add(createFormLabel("Customer ID:"), 0, 0);
        profileGrid.add(createFormValue(String.valueOf(currentCustomer.getCustomerId())), 1, 0);

        profileGrid.add(createFormLabel("Name:"), 0, 1);
        profileGrid.add(createFormValue(currentCustomer.getFirstName() + " " + currentCustomer.getLastName()), 1, 1);

        profileGrid.add(createFormLabel("Email:"), 0, 2);
        profileGrid.add(createFormValue(currentCustomer.getEmail()), 1, 2);

        profileGrid.add(createFormLabel("Phone:"), 0, 3);
        profileGrid.add(createFormValue(currentCustomer.getPhoneNumber()), 1, 3);

        profileGrid.add(createFormLabel("Address:"), 0, 4);
        profileGrid.add(createFormValue(currentCustomer.getAddress()), 1, 4);

        profileGrid.add(createFormLabel("Employment:"), 0, 5);
        profileGrid.add(createFormValue(currentCustomer.getEmploymentStatus()), 1, 5);

        if (currentCustomer.getCompanyName() != null && !currentCustomer.getCompanyName().isEmpty()) {
            profileGrid.add(createFormLabel("Company:"), 0, 6);
            profileGrid.add(createFormValue(currentCustomer.getCompanyName()), 1, 6);
        }

        content.getChildren().addAll(titleLabel, profileGrid);
        return content;
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #34495e; -fx-font-weight: 600; -fx-font-size: 13px;");
        return label;
    }

    private Label createFormValue(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14px;");
        return label;
    }
}