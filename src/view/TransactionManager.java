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

public class TransactionManager {
    private BankingController controller;
    private UIHelper uiHelper;
    private Main main;

    public TransactionManager(BankingController controller, UIHelper uiHelper, Main main) {
        this.controller = controller;
        this.uiHelper = uiHelper;
        this.main = main;
    }

    public void showDepositForm() {
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

        TextField amountField = uiHelper.createFormField("Enter amount");
        TextField descriptionField = uiHelper.createFormField("Enter description");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        java.util.List<String> accountNumbers = controller.getAllAccountNumbers();
        accountCombo.getItems().addAll(accountNumbers);

        formGrid.add(uiHelper.createFormLabel("Account:"), 0, 0);
        formGrid.add(accountCombo, 1, 0);
        formGrid.add(uiHelper.createFormLabel("Amount:"), 0, 1);
        formGrid.add(amountField, 1, 1);
        formGrid.add(uiHelper.createFormLabel("Description:"), 0, 2);
        formGrid.add(descriptionField, 1, 2);
        formGrid.add(messageLabel, 1, 3);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = uiHelper.createSuccessButton("Process Deposit");
        Button cancelButton = uiHelper.createDangerButton("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        submitButton.setOnAction(e -> handleDeposit(accountCombo, amountField, descriptionField, messageLabel));

        buttonBox.getChildren().addAll(cancelButton, submitButton);
        formGrid.add(buttonBox, 1, 4);

        mainLayout.getChildren().addAll(titleLabel, formGrid);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void handleDeposit(ComboBox<String> accountCombo, TextField amountField, TextField descriptionField, Label messageLabel) {
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
    }

    public void showWithdrawalForm() {
        // Similar structure to showDepositForm but for withdrawals
        // Implementation would follow the same pattern
    }

    public void showStaffTransferForm() {
        // Similar structure to showDepositForm but for transfers
        // Implementation would follow the same pattern
    }

    public void showAllTransactions() {
        Stage stage = new Stage();
        stage.setTitle("All Transactions - KoDaR Banking");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("All Transactions");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-font-weight: bold;");

        TableView<model.Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");
        table.setPlaceholder(new Label("No transactions found"));

        TableColumn<model.Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getTransactionDate().toString()));

        TableColumn<model.Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getTransactionType()));

        TableColumn<model.Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                String.format("P %.2f", cell.getValue().getAmount())));

        TableColumn<model.Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getDescription()));

        TableColumn<model.Transaction, String> refCol = new TableColumn<>("Reference");
        refCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getReferenceNumber()));

        TableColumn<model.Transaction, String> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(cell -> {
            model.Transaction transaction = cell.getValue();
            model.Account account = controller.getAccountById(transaction.getAccountId());
            return new javafx.beans.property.SimpleStringProperty(
                    account != null ? ((model.AbstractAccount)account).getAccountNumber() : "Unknown");
        });

        table.getColumns().addAll(dateCol, typeCol, amountCol, descCol, refCol, accountCol);

        java.util.List<model.Transaction> transactions = controller.getAllTransactions();
        if (transactions != null) {
            table.getItems().addAll(transactions);
        }

        layout.getChildren().addAll(titleLabel, table);
        Scene scene = new Scene(layout, 1200, 600);
        stage.setScene(scene);
        stage.show();
    }
}
