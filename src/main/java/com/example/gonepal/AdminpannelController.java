package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AdminpannelController {
    @FXML
    public Button add;
    @FXML
    public MenuItem logout;
    @FXML
    public MenuItem reportLogsMenuItem;
    @FXML
    private AnchorPane rootPane;
    // For the second attraction slot
    @FXML
    private ImageView secondAttractionImageView;
    @FXML
    private Label secondAttractionLabel;
    @FXML
    private MenuButton secondAttractionMenu;
    @FXML
    private MenuItem removeSecondAttraction;

    private static final String CSV_FILE_PATH = "attractions_data.csv";

    @FXML
    private void initialize() {
        updateSecondAttractionDisplay();
    }

    public void gotoaddattraction(ActionEvent event) {
        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addattractions.fxml")));
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
        }
    }

    @FXML
    private void handleRemoveSecondAttraction(ActionEvent event) {
        List<Attraction> attractions = AttractionData.loadAttractionsFromCSV();
        if (attractions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Attractions");
            alert.setHeaderText(null);
            alert.setContentText("No attractions found to remove!");
            alert.showAndWait();
            return;
        }

        // Get the latest attraction (the one being displayed)
        Attraction latestAttraction = attractions.get(attractions.size() - 1);

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText("Remove Attraction");
        confirmAlert.setContentText("Are you sure you want to remove \"" + latestAttraction.getName() + "\"?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Remove the latest attraction from the list
                attractions.remove(attractions.size() - 1);

                // Rewrite the CSV file with the updated list
                rewriteCSVFile(attractions);

                // Success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Attraction \"" + latestAttraction.getName() + "\" has been removed successfully!");
                successAlert.showAndWait();

                // Refresh the display
                updateSecondAttractionDisplay();

            } catch (Exception e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to remove attraction from CSV file!");
                errorAlert.showAndWait();
            }
        }
    }

    private void rewriteCSVFile(List<Attraction> attractions) throws IOException {
        File csvFile = new File(CSV_FILE_PATH);

        try (FileWriter writer = new FileWriter(csvFile, false); // false = overwrite
             PrintWriter printWriter = new PrintWriter(writer)) {

            // Write header
            printWriter.println("Name,Location,Description,MainImage,MiniImage1,MiniImage2");

            // Write all remaining attractions
            for (Attraction attraction : attractions) {
                String escapedName = escapeCSVField(attraction.getName());
                String escapedLocation = escapeCSVField(attraction.getLocation());
                String escapedDescription = escapeCSVField(attraction.getDescription());
                String escapedMainImage = escapeCSVField(attraction.getMainImagePath() != null ? attraction.getMainImagePath() : "");

                List<String> miniImages = attraction.getMiniImagePaths();
                String escapedMiniImage1 = escapeCSVField(miniImages.size() > 0 ? miniImages.get(0) : "");
                String escapedMiniImage2 = escapeCSVField(miniImages.size() > 1 ? miniImages.get(1) : "");

                printWriter.printf("%s,%s,%s,%s,%s,%s%n",
                        escapedName, escapedLocation, escapedDescription,
                        escapedMainImage, escapedMiniImage1, escapedMiniImage2);
            }
        }
    }

    private String escapeCSVField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    public void refreshAttractionDisplay() {
        updateSecondAttractionDisplay();
    }

    private void updateSecondAttractionDisplay() {
        List<Attraction> attractions = AttractionData.loadAttractionsFromCSV();
        if (attractions.isEmpty()) {
            hideSecondAttractionSlot();
            return;
        }

        // Use the last added attraction (as requested)
        Attraction latest = attractions.get(attractions.size() - 1);

        if (secondAttractionImageView != null) {
            secondAttractionImageView.setVisible(true);
            try {
                if (latest.getMainImagePath() != null && !latest.getMainImagePath().isEmpty()) {
                    Image img = new Image(latest.getMainImagePath());
                    secondAttractionImageView.setImage(img);

                    // Set fixed size - preserve the original FXML dimensions
                    secondAttractionImageView.setFitWidth(545.0);
                    secondAttractionImageView.setFitHeight(74.0);
                    secondAttractionImageView.setPreserveRatio(false);

                    // Apply rounded corners
                    Rectangle clip = new Rectangle(545.0, 74.0);
                    clip.setArcWidth(20);
                    clip.setArcHeight(20);
                    secondAttractionImageView.setClip(clip);

                    // Center crop logic for better image display
                    double imgW = img.getWidth();
                    double imgH = img.getHeight();
                    double viewW = 545.0;
                    double viewH = 74.0;
                    double scale = Math.max(viewW / imgW, viewH / imgH);
                    double cropW = viewW / scale;
                    double cropH = viewH / scale;
                    double x = Math.max(0, (imgW - cropW) / 2);
                    double y = Math.max(0, (imgH - cropH) / 2);
                    secondAttractionImageView.setViewport(new Rectangle2D(x, y, cropW, cropH));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                secondAttractionImageView.setImage(null);
            }
        }

        if (secondAttractionLabel != null) {
            secondAttractionLabel.setVisible(true);
            secondAttractionLabel.setText(latest.getName().isEmpty() ? "New Attraction" : latest.getName());
        }

        if (secondAttractionMenu != null) {
            secondAttractionMenu.setVisible(true);
        }
    }

    private void hideSecondAttractionSlot() {
        if (secondAttractionImageView != null) {
            secondAttractionImageView.setVisible(false);
        }
        if (secondAttractionLabel != null) {
            secondAttractionLabel.setVisible(false);
        }
        if (secondAttractionMenu != null) {
            secondAttractionMenu.setVisible(false);
        }
    }
    @FXML
    private void gotoreportlogs(ActionEvent event) {
        try {
            Parent reportLogsRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("reportlog.fxml")));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(reportLogsRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load Report Logs page.");
            alert.showAndWait();
        }
    }
}