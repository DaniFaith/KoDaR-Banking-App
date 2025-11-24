package view;

import controller.BankingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {
    private Main main;
    private BankingController controller;
    private UIHelper uiHelper;

    public LoginScreen(Main main, BankingController controller) {
        this.main = main;
        this.controller = controller;
        this.uiHelper = new UIHelper();
    }

    public void show(Stage primaryStage) {
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");

        VBox loginContainer = new VBox(30);
        loginContainer.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-radius: 15; " +
                "-fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        loginContainer.setPadding(new Insets(40));
        loginContainer.setMaxWidth(400);

        // Header
        VBox headerBox = createHeader();
        
        // Login form
        VBox formBox = createLoginForm();

        loginContainer.getChildren().addAll(headerBox, formBox);
        mainContainer.getChildren().add(loginContainer);

        Scene scene = new Scene(mainContainer, 1000, 700);
        primaryStage.setScene(scene);
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label logoLabel = new Label("KᴑDₐR");
        logoLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label titleLabel = new Label("Banking System Login");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");

        headerBox.getChildren().addAll(logoLabel, titleLabel);
        return headerBox;
    }

    private VBox createLoginForm() {
        VBox formBox = new VBox(20);

        TextField usernameField = uiHelper.createFormField("Username");
        PasswordField passwordField = createPasswordField();
        Button loginButton = uiHelper.createPrimaryButton("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        loginButton.setOnAction(e -> handleLogin(usernameField, passwordField, errorLabel));

        VBox demoBox = createDemoCredentialsBox();

        formBox.getChildren().addAll(
                uiHelper.createFormLabel("Username"), usernameField,
                uiHelper.createFormLabel("Password"), passwordField,
                loginButton, errorLabel, demoBox
        );
        return formBox;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        passwordField.setPromptText("Password");
        return passwordField;
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField, Label errorLabel) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        if (controller.login(username, password)) {
            if (controller.isStaffLoggedIn()) {
                main.showStaffDashboard();
            } else if (controller.isCustomerLoggedIn()) {
                main.showCustomerDashboard();
            }
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }

    private VBox createDemoCredentialsBox() {
        VBox demoBox = new VBox(10);
        demoBox.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 8; -fx-padding: 15;");
        
        Label demoLabel = new Label("Demo Credentials:");
        demoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label staffDemo = new Label("Staff: admin / admin123");
        staffDemo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        Label customerDemo = new Label("Customer: kagiso / customer123");
        customerDemo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        demoBox.getChildren().addAll(demoLabel, staffDemo, customerDemo);
        return demoBox;
    }
}
