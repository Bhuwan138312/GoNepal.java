package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Import your custom exceptions
import com.example.gonepal.InvalidEmailException;
import com.example.gonepal.NameTooShortException;
import com.example.gonepal.PasswordTooWeakException;

public class signupController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signupButton;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        if (fullNameField != null) fullNameField.setText("");
        if (emailField != null) emailField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (confirmPasswordField != null) confirmPasswordField.setText("");
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        try {
            validateSignup(fullName, email, password, confirmPassword);

            UserData.addUser(fullName, email, password);

            showAlert("Success", "Account created successfully! You can now login.");
            handleLogin(event);

        } catch (NameTooShortException | InvalidEmailException | PasswordTooWeakException e) {
            showAlert("Error", e.getMessage());
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void validateSignup(String fullName, String email, String password, String confirmPassword)
            throws NameTooShortException, InvalidEmailException, PasswordTooWeakException, Exception {

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            throw new Exception("Please fill in all fields");
        }

        if (fullName.length() < 8) {
            throw new NameTooShortException("Full name must be at least 8 characters");
        }

        if (!isValidEmail(email)) {
            throw new InvalidEmailException("Email must be a valid gmail address");
        }

        if (UserData.userExists(email)) {
            throw new Exception("User with this email already exists");
        }

        if (!isValidPassword(password)) {
            throw new PasswordTooWeakException("Password must be at least 8 characters and contain at least one symbol like '@'");
        }

        if (!password.equals(confirmPassword)) {
            throw new PasswordTooWeakException("Passwords do not match");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("GoneNpal - Login");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load login page");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.+\\-]+@gmail\\.com$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[@!#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}