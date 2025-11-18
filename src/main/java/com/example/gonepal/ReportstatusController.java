package com.example.gonepal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public class ReportstatusController {
    @FXML
    public Button backbutton;
    @FXML
    private TableView<ReportWithStatus> reportsTable;
    @FXML
    private TableColumn<ReportWithStatus, String> nameCol;
    @FXML
    private TableColumn<ReportWithStatus, String> locationCol;
    @FXML
    private TableColumn<ReportWithStatus, String> contactCol;
    @FXML
    private TableColumn<ReportWithStatus, String> typeCol;
    @FXML
    private TableColumn<ReportWithStatus, String> detailsCol;
    @FXML
    private TableColumn<ReportWithStatus, String> statusCol;

    private static final String CSV_FILE_PATH = "reports.csv";

    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        reportsTable.setItems(loadReports());
    }

    private ObservableList<ReportWithStatus> loadReports() {
        ObservableList<ReportWithStatus> reports = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = parseCSVLine(line);
                if (parts.length >= 5) {
                    reports.add(new ReportWithStatus(parts[0], parts[1], parts[2], parts[3], parts[4], "Pending"));
                }
            }
        } catch (Exception e) {
            // file may not exist yet, that's okay
        }
        return reports;
    }

    // Simple CSV parsing that handles quoted commas
    private String[] parseCSVLine(String line) {
        java.util.List<String> tokens = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        sb.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else {
                if (c == ',') {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    sb.append(c);
                }
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
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

    // Nested class for the table rows
    public static class ReportWithStatus {
        private String fullName;
        private String location;
        private String contact;
        private String type;
        private String details;
        private String status;

        public ReportWithStatus(String fullName, String location, String contact, String type, String details, String status) {
            this.fullName = fullName;
            this.location = location;
            this.contact = contact;
            this.type = type;
            this.details = details;
            this.status = status;
        }

        public String getFullName() { return fullName; }
        public String getLocation() { return location; }
        public String getContact() { return contact; }
        public String getType() { return type; }
        public String getDetails() { return details; }
        public String getStatus() { return status; }
    }
}