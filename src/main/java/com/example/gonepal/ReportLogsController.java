package com.example.gonepal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public class ReportLogsController {
    public Button backbutton;
    @FXML
    private Button chartsButton;
    @FXML
    private TableView<Report> reportsTable;
    @FXML
    private TableColumn<Report, String> nameCol;
    @FXML
    private TableColumn<Report, String> locationCol;
    @FXML
    private TableColumn<Report, String> contactCol;
    @FXML
    private TableColumn<Report, String> typeCol;
    @FXML
    private TableColumn<Report, String> detailsCol;

    private static final String CSV_FILE_PATH = "reports.csv";

    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));
        reportsTable.setItems(loadReports());
    }

    public void gotoadminpannel(ActionEvent event) {
        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("adminpannel.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToCharts(ActionEvent event) {
        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("charts.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Report> loadReports() {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = parseCSVLine(line);
                if (parts.length >= 5) {
                    reports.add(new Report(parts[0], parts[1], parts[2], parts[3], parts[4]));
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
}