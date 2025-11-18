package com.example.gonepal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChartsController {
    @FXML
    private PieChart reportTypeChart;
    @FXML
    private Button backButton;

    private static final String CSV_FILE_PATH = "reports.csv";

    @FXML
    private void initialize() {
        loadChartData();
    }

    private void loadChartData() {
        Map<String, Integer> reportCounts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = parseCSVLine(line);
                if (parts.length >= 4) {
                    String type = parts[3].trim();
                    reportCounts.put(type, reportCounts.getOrDefault(type, 0) + 1);
                }
            }
        } catch (Exception e) {
            // If file doesn't exist or is empty, show default message
            System.out.println("No data available for chart");
        }

        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if (reportCounts.isEmpty()) {
            // Show placeholder data if no reports exist
            pieChartData.add(new PieChart.Data("No Data", 1));
        } else {
            int total = reportCounts.values().stream().mapToInt(Integer::intValue).sum();

            for (Map.Entry<String, Integer> entry : reportCounts.entrySet()) {
                String type = entry.getKey();
                int count = entry.getValue();
                double percentage = (double) count / total * 100;

                // Format label with count and percentage
                String label = String.format("%s (%d - %.1f%%)", type, count, percentage);
                pieChartData.add(new PieChart.Data(label, count));
            }
        }

        reportTypeChart.setData(pieChartData);
        reportTypeChart.setTitle("Report Types Distribution");
    }

    @FXML
    private void goToReportLogs(ActionEvent event) {
        try {
            Parent nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("reportlog.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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