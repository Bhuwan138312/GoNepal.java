package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class FileReportController {
    @FXML
    private TextField fullname;
    @FXML
    private TextField fullname1; // Location
    @FXML
    private TextField contact;
    @FXML
    private TextArea reportDetail;
    @FXML
    private MenuButton typeMenuButton;

    private static final String CSV_FILE_PATH = "reports.csv";

    @FXML
    private void initialize() {
        // Set selected type when menu item is clicked
        for (MenuItem item : typeMenuButton.getItems()) {
            item.setOnAction(e -> typeMenuButton.setText(item.getText()));
        }
    }

    @FXML
    private void fileReport(ActionEvent event) {
        String name = fullname.getText();
        String location = fullname1.getText();
        String contactNo = contact.getText();
        String type = typeMenuButton.getText();
        String detail = reportDetail.getText();

        if(name.isEmpty() || location.isEmpty() || contactNo.isEmpty() || type.isEmpty() || detail.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        // Save to CSV
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            // If file is empty, write header
            File file = new File(CSV_FILE_PATH);
            if(file.length() == 0) {
                writer.write("Full Name,Location,Contact,Type,Details\n");
            }
            // Escape commas in fields
            writer.write(escape(name)+","+escape(location)+","+escape(contactNo)+","+escape(type)+","+escape(detail)+"\n");
        } catch (IOException e) {
            showAlert("Error saving report!");
            e.printStackTrace();
            return;
        }

        showAlert("Report filed successfully!");

        // Redirect to homescreen
        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homescreen.fxml")));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String escape(String s) {
        if(s == null) return "";
        if(s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    public void gothomescreen(ActionEvent event) {
        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homescreen.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}