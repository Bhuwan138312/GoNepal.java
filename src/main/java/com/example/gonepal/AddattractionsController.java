package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddattractionsController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    public Button backbutton;
    @FXML
    private ImageView mainImageView;
    @FXML
    private Button uploadMainImageButton;
    @FXML
    private ImageView miniImageView1;
    @FXML
    private Button uploadMiniImageButton1;
    @FXML
    private ImageView miniImageView2;
    @FXML
    private Button uploadMiniImageButton2;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea descriptionField;

    private String mainImagePath = null;
    private String miniImagePath1 = null;
    private String miniImagePath2 = null;

    private static final String CSV_FILE_PATH = "attractions_data.csv";

    @FXML
    public void gotoavailableguides(ActionEvent event) {
        try {
<<<<<<< HEAD
            javafx.scene.Parent nextRoot = FXMLLoader.load(getClass().getResource("adminpannel.fxml"));
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
=======
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(getClass().getResource("adminpannel.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
>>>>>>> 70cb5c8126e78850dc2a35ffa83d5f1650eeaaae
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUploadMainImage(ActionEvent event) {
        mainImagePath = uploadImageToImageView(mainImageView, mainImageView.getFitWidth(), mainImageView.getFitHeight(), event);
    }

    @FXML
    public void handleUploadMiniImage1(ActionEvent event) {
        miniImagePath1 = uploadImageToImageView(miniImageView1, miniImageView1.getFitWidth(), miniImageView1.getFitHeight(), event);
    }

    @FXML
    public void handleUploadMiniImage2(ActionEvent event) {
        miniImagePath2 = uploadImageToImageView(miniImageView2, miniImageView2.getFitWidth(), miniImageView2.getFitHeight(), event);
    }

    private String uploadImageToImageView(ImageView imageView, double fitWidth, double fitHeight, ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Image img = new Image(file.toURI().toString(), fitWidth, fitHeight, false, true);
            imageView.setImage(img);
            return file.getAbsolutePath(); // Store absolute path instead of URI
        }
        return null;
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        String name = nameField.getText();
        String location = locationField.getText();
        String description = descriptionField.getText();

        if (name == null || name.isEmpty() || location == null || location.isEmpty() || description == null || description.isEmpty() || mainImagePath == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please fill all fields and upload at least a main image!");
            errorAlert.showAndWait();
            return;
        }

        // Save to CSV file
        try {
            saveAttractionToCSV(name, location, description, mainImagePath, miniImagePath1, miniImagePath2);

            // Create and set the new attraction as selected
            List<String> miniImages = new ArrayList<>();
            if (miniImagePath1 != null && !miniImagePath1.isEmpty()) {
                miniImages.add(miniImagePath1);
            }
            if (miniImagePath2 != null && !miniImagePath2.isEmpty()) {
                miniImages.add(miniImagePath2);
            }

            Attraction newAttraction = new Attraction(name, location, description, mainImagePath, miniImages);
            AttractionData.setSelectedAttraction(newAttraction);

            // Success popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Attraction added successfully! You can now see it on the homescreen.");
            alert.showAndWait();

            // Navigate back to admin panel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminpannel.fxml"));
            javafx.scene.Parent nextRoot = loader.load();

<<<<<<< HEAD
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
=======
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
>>>>>>> 70cb5c8126e78850dc2a35ffa83d5f1650eeaaae
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Failed to save attraction data: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void saveAttractionToCSV(String name, String location, String description, String mainImage, String miniImage1, String miniImage2) throws IOException {
        File csvFile = new File(CSV_FILE_PATH);
        boolean isNewFile = !csvFile.exists();

        try (FileWriter writer = new FileWriter(csvFile, true);
             PrintWriter printWriter = new PrintWriter(writer)) {

            // Write header if it's a new file
            if (isNewFile) {
                printWriter.println("Name,Location,Description,MainImage,MiniImage1,MiniImage2");
            }

            // Escape commas and quotes in data
            String escapedName = escapeCSVField(name);
            String escapedLocation = escapeCSVField(location);
            String escapedDescription = escapeCSVField(description);
            String escapedMainImage = escapeCSVField(mainImage != null ? mainImage : "");
            String escapedMiniImage1 = escapeCSVField(miniImage1 != null ? miniImage1 : "");
            String escapedMiniImage2 = escapeCSVField(miniImage2 != null ? miniImage2 : "");

            // Write data row
            printWriter.printf("%s,%s,%s,%s,%s,%s%n",
                    escapedName, escapedLocation, escapedDescription,
                    escapedMainImage, escapedMiniImage1, escapedMiniImage2);
        }
    }

    private String escapeCSVField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            // Replace actual newlines with \\n for CSV storage, then escape quotes
            String escaped = field.replace("\r\n", "\\n").replace("\n", "\\n").replace("\r", "\\n");
            return "\"" + escaped.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}