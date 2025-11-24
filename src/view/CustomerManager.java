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

public class CustomerManager {
    private BankingController controller;
    private UIHelper uiHelper;
    private Main main;

    public CustomerManager(BankingController controller, UIHelper uiHelper, Main main) {
        this.controller = controller;
        this.uiHelper = uiHelper;
        this.main = main;
    }

    public void showCreateCustomerForm() {
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

        TextField firstNameField = uiHelper.createFormField("Enter first name");
        TextField lastNameField = uiHelper.createFormField("Enter last name");
        TextField addressField = uiHelper.createFormField("Enter full address");
        TextField phoneField = uiHelper.createFormField("Enter phone number");
        TextField emailField = uiHelper.createFormField("Enter email address");

        ComboBox<String> employmentCombo = new ComboBox<>();
        employmentCombo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        employmentCombo.getItems().addAll("EMPLOYED", "SELF_EMPLOYED", "UNEMPLOYED", "RETIRED", "STUDENT");
        employmentCombo.setPromptText("Select employment status");

        TextField companyField = uiHelper.createFormField("Enter company name (if employed)");
        TextField companyAddressField = uiHelper.createFormField("Enter company address");

        formGrid.add(uiHelper.createFormLabel("First Name:"), 0, 0);
        formGrid.add(firstNameField, 1, 0);
        formGrid.add(uiHelper.createFormLabel("Last Name:"), 0, 1);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(uiHelper.createFormLabel("Address:"), 0, 2);
        formGrid.add(addressField, 1, 2);
        formGrid.add(uiHelper.createFormLabel("Phone:"), 0, 3);
        formGrid.add(phoneField, 1, 3);
        formGrid.add(uiHelper.createFormLabel("Email:"), 0, 4);
        formGrid.add(emailField, 1, 4);
        formGrid.add(uiHelper.createFormLabel("Employment:"), 0, 5);
        formGrid.add(employmentCombo, 1, 5);
        formGrid.add(uiHelper.createFormLabel("Company:"), 0, 6);
        formGrid.add(companyField, 1, 6);
        formGrid.add(uiHelper.createFormLabel("Company Address:"), 0, 7);
        formGrid.add(companyAddressField, 1, 7);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button submitButton = uiHelper.createSuccessButton("Create Customer");
        Button cancelButton = uiHelper.createDangerButton("Cancel");
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
                    main.showAlert("Success", "Customer created successfully!");
                    stage.close();
                } else {
                    main.showAlert("Error", "Failed to create customer. Please check the information.");
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
            main.showAlert("Validation Error", "First name is required.");
            return false;
        }
        if (lastName.getText().isEmpty()) {
            main.showAlert("Validation Error", "Last name is required.");
            return false;
        }
        if (phone.getText().isEmpty()) {
            main.showAlert("Validation Error", "Phone number is required.");
            return false;
        }
        return true;
    }

    public void showAllCustomers() {
        Stage stage = new Stage();
        stage.setTitle("All Customers - KoDaR Banking");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label titleLabel = new Label("Customer Management");
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<model.Customer> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 8; -fx-background-radius: 8;");
        table.setPlaceholder(new Label("No customers found"));

        TableColumn<model.Customer, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().getCustomerId())));

        TableColumn<model.Customer, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getFirstName() + " " + cell.getValue().getLastName()));

        TableColumn<model.Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<model.Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhoneNumber()));

        TableColumn<model.Customer, String> employmentCol = new TableColumn<>("Employment");
        employmentCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmploymentStatus()));

        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, employmentCol);

        for (TableColumn<?, ?> column : table.getColumns()) {
            column.setStyle("-fx-background-color: linear-gradient(to bottom, #34495e, #2c3e50); " +
                    "-fx-text-fill: white; -fx-font-weight: bold;");
        }

        table.getItems().addAll(controller.getAllCustomers());

        layout.getChildren().addAll(titleLabel, table);
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
