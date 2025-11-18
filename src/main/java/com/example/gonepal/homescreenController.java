package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class homescreenController {
    @FXML
    public Button explorebutton;
    @FXML
    public Button explorebutton2;
    @FXML
    public MenuItem logout;
    @FXML
    public MenuItem logs;
    @FXML
    public MenuItem report;
    @FXML
    public MenuItem language;
    @FXML
    public MenuItem reportstatus;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane anchorsecond;
    @FXML
    private ImageView imagesecond;
    @FXML
    private Label attraction2;
    @FXML
    private TextArea descriptionArea1;

    // Add a label for displaying the welcome message with user name
    @FXML
    private Label welcomeLabel;

    private String userName = "User"; // Default value

    @FXML
    private void initialize() {
        // Initial welcome message - will be updated when setUserName is called
        displayWelcomeMessage();
        loadAttractionsToHomescreen();
    }

    // Method to set the user name (called from loginController)
    public void setUserName(String userName) {
        this.userName = userName != null ? userName : "User";
        displayWelcomeMessage();
    }

    private void displayWelcomeMessage() {
        String welcomeMessage = "Welcome, " + userName + "!";
        System.out.println(welcomeMessage);

        // If you have a welcome label in your FXML, update it
        if (welcomeLabel != null) {
            welcomeLabel.setText(welcomeMessage);
        }
    }

    private void loadAttractionsToHomescreen() {
        List<Attraction> attractions = AttractionData.loadAttractionsFromCSV();

        // Initially hide the second attraction pane
        anchorsecond.setVisible(false);
        imagesecond.setVisible(false);

        // If we have attractions, show them starting from the second position
        if (!attractions.isEmpty()) {
            // Show the first added attraction in the second position
            Attraction firstAttraction = attractions.get(0);

            // Make the second attraction pane visible
            anchorsecond.setVisible(true);
            imagesecond.setVisible(true);

            // Update the attraction name
            attraction2.setText(firstAttraction.getName());

            // Update the description
            String description = firstAttraction.getDescription();
            // Convert \\n back to actual newlines for display
            description = description.replace("\\n", "\n");
            if (description.length() > 100) {
                description = description.substring(0, 100) + "...";
            }
            descriptionArea1.setText(description);

            // Update the main image
            try {
                String imagePath = firstAttraction.getMainImagePath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Handle both file URI and regular file path
                    Image image;
                    if (imagePath.startsWith("file:")) {
                        image = new Image(imagePath, 260, 186, false, true);
                    } else {
                        File imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            image = new Image(imageFile.toURI().toString(), 260, 186, false, true);
                        } else {
                            // Fallback to default image
                            image = new Image(getClass().getResource("images/Rectangle 46.png").toExternalForm());
                        }
                    }
                    imagesecond.setImage(image);
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                // Keep default image if loading fails
            }
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("GoneNpal - Login");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load login page");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void gotoattarctiondetails(ActionEvent event) {
        System.out.println("Explore button clicked!");

        // Set the selected attraction based on which button was clicked
        Button clickedButton = (Button) event.getSource();
        String targetPage = "arttractiondetails.fxml"; // Default for first attraction

        if (clickedButton == explorebutton2) {
            // Second button clicked - set the attraction from CSV and go to addedattraction page
            List<Attraction> attractions = AttractionData.loadAttractionsFromCSV();
            if (!attractions.isEmpty()) {
                AttractionData.setSelectedAttraction(attractions.get(0));
                targetPage = "addedattraction.fxml"; // Use the custom attraction details page
                System.out.println("Going to added attraction page for: " + attractions.get(0).getName());
            }
        }

        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(targetPage)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + targetPage);
        }
    }

    @FXML
    private void gotobookinglogs(ActionEvent event) {
        try {
            Parent secondRoot = FXMLLoader.load(getClass().getResource("bookinglogs.fxml"));
            Scene secondScene = new Scene(secondRoot);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(secondScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gotofilereport(ActionEvent event) {
        try {
            Parent fileReportRoot = FXMLLoader.load(getClass().getResource("filereport.fxml"));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(fileReportRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load file report page");
        }
    }

    @FXML
    private void gotoreportstatus(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("reportstatus.fxml")));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlelanguage(ActionEvent event) {
    }
}