package com.example.gonepal;

import com.example.gonepal.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

public class loginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private Label loginLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label noAccountLabel;
    @FXML
    private MenuButton languageMenuButton;
    @FXML
    private MenuItem englishMenuItem;
    @FXML
    private MenuItem nepaliMenuItem;

    // For showing password
    private TextField passwordTextField;

    // Predefined admin credentials
    private final String adminEmail = "bam@gmail.com";
    private final String adminPassword = "123";

    private ResourceBundle messages;

    @FXML
    private void initialize() {
        setLanguage("en");
        if (emailField != null) emailField.setText("");
        if (passwordField != null) passwordField.setText("");


        setupShowPassword();

        englishMenuItem.setOnAction(e -> setLanguage("en"));
        nepaliMenuItem.setOnAction(e -> setLanguage("ne"));

        // Load CSS
        loadCSS();
    }

    private void loadCSS() {
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            if (loginLabel != null && loginLabel.getScene() != null) {
                loginLabel.getScene().getStylesheets().add(css);
            }
        } catch (Exception e) {
            System.out.println("Could not load CSS file: " + e.getMessage());
        }
    }

    private void setLanguage(String langCode) {
        Locale locale = "ne".equals(langCode) ? new Locale("ne") : new Locale("en");
        messages = ResourceBundle.getBundle("messages", locale);

        if (loginLabel != null) loginLabel.setText(messages.getString("login"));
        if (emailLabel != null) emailLabel.setText(messages.getString("email"));
        if (passwordLabel != null) passwordLabel.setText(messages.getString("password"));
        if (loginButton != null) loginButton.setText(messages.getString("login"));
        if (signupButton != null) signupButton.setText(messages.getString("signup"));
        if (noAccountLabel != null) noAccountLabel.setText(messages.getString("no_account"));
        if (languageMenuButton != null) languageMenuButton.setText(messages.getString("language"));
        if (englishMenuItem != null) englishMenuItem.setText(messages.getString("english"));
        if (nepaliMenuItem != null) nepaliMenuItem.setText(messages.getString("nepali"));
        if (showPasswordCheckBox != null) showPasswordCheckBox.setText(messages.getString("show_password"));
    }

    private void setupShowPassword() {
        // Create the TextField with same properties as PasswordField
        passwordTextField = new TextField();

        // Copy all the properties from passwordField to passwordTextField
        passwordTextField.setLayoutX(passwordField.getLayoutX());
        passwordTextField.setLayoutY(passwordField.getLayoutY());
        passwordTextField.setPrefHeight(passwordField.getPrefHeight());
        passwordTextField.setPrefWidth(passwordField.getPrefWidth());
        passwordTextField.setOpacity(passwordField.getOpacity());
        passwordTextField.setStyle(passwordField.getStyle());

        // Initially hide the text field
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        // Add it to the same parent
        ((AnchorPane) passwordField.getParent()).getChildren().add(passwordTextField);

        // Bind the text properties
        passwordField.textProperty().bindBidirectional(passwordTextField.textProperty());

        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                // Show TextField, hide PasswordField
                passwordTextField.setText(passwordField.getText());
                passwordTextField.setManaged(true);
                passwordTextField.setVisible(true);
                passwordField.setManaged(false);
                passwordField.setVisible(false);

                // Focus on the text field
                passwordTextField.requestFocus();
                passwordTextField.positionCaret(passwordTextField.getText().length());
            } else {
                // Show PasswordField, hide TextField
                passwordField.setText(passwordTextField.getText());
                passwordField.setManaged(true);
                passwordField.setVisible(true);
                passwordTextField.setManaged(false);
                passwordTextField.setVisible(false);

                // Focus on the password field
                passwordField.requestFocus();
                passwordField.positionCaret(passwordField.getText().length());
            }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = showPasswordCheckBox.isSelected() ? passwordTextField.getText().trim() : passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(messages.getString("error"), messages.getString("fill_fields"));
            return;
        }

        if (email.equals(adminEmail) && password.equals(adminPassword)) {
            loadAdminPanel(event);
            return;
        }

        if (UserData.validateUser(email, password)) {
            // Get user's full name and pass it to home screen
            String userName = UserData.getUserName(email);
            loadHomeScreen(event, userName);
        } else {
            showAlert(messages.getString("login_failed"), messages.getString("invalid_email_password"));
        }
    }

    private void loadHomeScreen(ActionEvent event, String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homescreen.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the user name
            homescreenController controller = loader.getController();
            controller.setUserName(userName);

            // Apply CSS to new scene
            Scene scene = new Scene(root);
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GoneNpal - Home");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(messages.getString("error"), "Could not load home screen");
        }
    }

    private void loadAdminPanel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminpannel.fxml"));
            Parent root = loader.load();

            // Apply CSS to new scene
            Scene scene = new Scene(root);
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GoneNpal - Admin Panel");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(messages.getString("error"), "Could not load admin panel");
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();

            // Apply CSS to new scene
            Scene scene = new Scene(root);
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GoneNpal - Signup");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(messages.getString("error"), "Could not load signup page");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}