package view;

import controller.BankingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountManager {
    private BankingController controller;
    private UIHelper uiHelper;
    private Main main;

    public AccountManager(BankingController controller, UIHelper uiHelper, Main main) {
        this.controller = controller;
        this.uiHelper = uiHelper;
        this.main = main;
    }

    public void showCreateAccountForm() {
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

        TextField customerIdField = uiHelper.createFormField("Enter customer ID");
        TextField accountNumberField = uiHelper.createFormField("Enter account number");

        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        accountTypeCombo.getItems().addAll("SAVINGS", "INVESTMENT", "CHEQUE");
        accountTypeCombo.setPromptText("Select account type");

        TextField initialDepositField = uiHelper.createFormField("Enter initial deposit");
        TextField branchField = uiHelper.createFormField("Enter branch name");

        formGrid.add(uiHelper.createFormLabel("Customer ID:"), 0, 0);
        formGrid.add(customerIdField, 1, 0);
        formGrid.add(uiHelper.createFormLabel("Account Number:"), 0, 1);
        formGrid.add(accountNumberField, 1, 1);
        formGrid.add(uiHelper.createFormLabel("Account Type:"), 0, 2);
        formGrid.add(accountTypeCombo, 1, 2);
        formGrid.add(uiHelper.createFormLabel("Initial Deposit:"), 0, 3);
        formGrid.add(initialDepositField, 1, 3);
        formGrid.add(uiHelper.createFormLabel("Branch:"), 0, 4);
        formGrid.add(branchField, 1, 4);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = uiHelper.createSuccessButton("Create Account");
        Button cancelButton = uiHelper.createDangerButton("Cancel");
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
                    main.showAlert("Success", "Account created successfully!");
                    stage.close();
                } else {
                    main.showAlert("Error", "Failed to create account. Please check:\n" +
                            "- Customer ID exists\n" +
                            "- Initial deposit meets requirements\n" +
                            "- Employment status for cheque accounts");
                }
            } catch (NumberFormatException ex) {
                main.showAlert("Error", "Please enter valid numbers for Customer ID and Initial Deposit.");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 5);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void showAllAccounts() {
        Stage stage = new Stage();
        stage.setTitle("All Accounts - KoDaR Banking");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("All Bank Accounts");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-font-weight: bold;");

        TableView<model.Account> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");
        table.setPlaceholder(new Label("No accounts found"));

        TableColumn<model.Account, String> accNumCol = new TableColumn<>("Account Number");
        accNumCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                ((model.AbstractAccount)cell.getValue()).getAccountNumber()));

        TableColumn<model.Account, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getAccountType()));

        TableColumn<model.Account, String> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                String.format("P %.2f", cell.getValue().getBalance())));

        TableColumn<model.Account, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                ((model.AbstractAccount)cell.getValue()).getBranch()));

        TableColumn<model.Account, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(cell -> {
            model.Account account = cell.getValue();
            model.Customer customer = controller.getCustomer(((model.AbstractAccount)account).getCustomerId());
            return new javafx.beans.property.SimpleStringProperty(
                    customer != null ? customer.getFirstName() + " " + customer.getLastName() : "Unknown");
        });

        TableColumn<model.Account, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                ((model.AbstractAccount)cell.getValue()).isActive() ? "Active" : "Closed"));

        table.getColumns().addAll(accNumCol, typeCol, balanceCol, branchCol, customerCol, statusCol);

        java.util.List<model.Account> accounts = controller.getAllAccounts();
        if (accounts != null) {
            table.getItems().addAll(accounts);
        }

        layout.getChildren().addAll(titleLabel, table);
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void showCloseAccountForm() {
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

        java.util.List<model.Account> accounts = controller.getAllAccounts();
        for (model.Account account : accounts) {
            if (((model.AbstractAccount)account).isActive()) {
                accountCombo.getItems().add(((model.AbstractAccount)account).getAccountNumber());
            }
        }

        formGrid.add(uiHelper.createFormLabel("Account:"), 0, 0);
        formGrid.add(accountCombo, 1, 0);
        formGrid.add(messageLabel, 1, 1);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = uiHelper.createDangerButton("Close Account");
        Button cancelButton = uiHelper.createPrimaryButton("Cancel");
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
}
