package view;

import controller.BankingController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private BankingController controller;
    private Stage primaryStage;
    private LoginScreen loginScreen;
    private StaffDashboard staffDashboard;

    @Override
    public void start(Stage primaryStage) {
        this.controller = new BankingController();
        this.primaryStage = primaryStage;
        this.loginScreen = new LoginScreen(this, controller);
        this.staffDashboard = new StaffDashboard(this, controller);

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

    public void showLoginScreen() {
        loginScreen.show(primaryStage);
    }

    public void showCustomerDashboard() {
        CustomerDashboard customerDashboard = new CustomerDashboard(primaryStage, controller);
        customerDashboard.show();
    }

    public void showStaffDashboard() {
        staffDashboard.show(primaryStage);
    }

    public void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
