package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class UIHelper {
    
    public TextField createFormField(String promptText) {
        TextField field = new TextField();
        field.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-font-size: 14px;");
        field.setPromptText(promptText);
        return field;
    }

    public Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #34495e; -fx-font-weight: 600; -fx-font-size: 13px;");
        return label;
    }

    public Button createPrimaryButton(String text) {
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

    public Button createSuccessButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #27ae60, #229954); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; " +
                "-fx-border-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        return button;
    }

    public Button createDangerButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; " +
                "-fx-border-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        return button;
    }

    public VBox createDashboardCard(String title, String value, String subtitle) {
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
}
