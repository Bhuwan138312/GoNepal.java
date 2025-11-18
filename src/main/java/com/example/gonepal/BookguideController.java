package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class BookguideController {
    @FXML
    public Button backbutton;
    @FXML
    public Button confirm;
    @FXML
    private TextField fullname;
    @FXML
    private TextField gmail;
    @FXML
    private TextField contact;
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox<String> time;
    @FXML
    private ChoiceBox<String> duration;
    @FXML
    private TextField promocode;
    @FXML
    private TextField total;

    private final int HOURLY_RATE = 2000;
    private final String GUIDE_NAME = "Hitler"; // You can make this dynamic later

    private boolean peakWarningShown = false; // To avoid showing repeatedly

    @FXML
    public void initialize() {
        time.getItems().addAll("10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM", "6:00 PM");
        duration.getItems().addAll("2 Hrs", "4 Hrs", "6 Hrs", "8 Hrs");

        // Listen for changes in duration selection
        duration.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            calculateTotal(newVal);
        });

        // Listen for changes in promo code
        promocode.textProperty().addListener((obs, oldText, newText) -> {
            calculateTotal(duration.getValue());
        });

        // Listen for changes in time selection
        time.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldVal, String newVal) {
                if (newVal != null && (newVal.equals("10:00 AM") || newVal.equals("6:00 PM"))) {
                    showPeakHourPopup();
                }
                calculateTotal(duration.getValue());
            }
        });

        // Set up confirm button action
        confirm.setOnAction(this::confirmBooking);
    }

    // Show peak hour popup with proceed/cancel
    private void showPeakHourPopup() {
        // Only show once per selection
        if (!peakWarningShown) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Peak Hour Warning");
            alert.setHeaderText("Peak hour selected");
            alert.setContentText("You have chosen a peak time (10:00 AM or 6:00 PM). There may be many people and crowds at this time. Do you wish to proceed?");
            ButtonType proceedBtn = new ButtonType("Proceed", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(proceedBtn, cancelBtn);
            alert.showAndWait().ifPresent(response -> {
                if (response == cancelBtn) {
                    // Reset time selection if user cancels
                    time.getSelectionModel().clearSelection();
                }
            });
            peakWarningShown = true;
        }
    }

    // Calculate and display total
    private void calculateTotal(String durationString) {
        peakWarningShown = false; // Reset warning for each calculation
        if (durationString != null && !durationString.isEmpty()) {
            // Extract the number of hours from the string (e.g., "4 Hrs" -> 4)
            String hoursStr = durationString.split(" ")[0];
            int hours = 0;
            try {
                hours = Integer.parseInt(hoursStr);
            } catch (NumberFormatException e) {
                total.setText("NRP |-");
                return;
            }

            int amount = HOURLY_RATE * hours;

            // Apply promo code discount if available
            String promoText = promocode.getText();
            if (promoText != null && !promoText.trim().isEmpty()) {
                if (promoText.equalsIgnoreCase("DISCOUNT10")) {
                    amount = (int) (amount * 0.9); // 10% discount
                } else if (promoText.equalsIgnoreCase("SAVE20")) {
                    amount = (int) (amount * 0.8); // 20% discount
                } else if (promoText.equalsIgnoreCase("GONEPAL")) {
                    amount = (int) (amount * 0.8); // 20% discount for GONEPAL
                }
            }

            total.setText("NRP | " + amount);
        } else {
            total.setText("NRP |-");
        }
    }

    @FXML
    public void confirmBooking(ActionEvent event) {
        // Validate form fields
        if (!validateForm()) {
            return;
        }

        try {
            // Save booking to CSV
            saveBookingToCSV();

            // Show success message
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Booking Confirmed");
            alert.setHeaderText(null);
            alert.setContentText("Your booking has been confirmed successfully!");
            alert.showAndWait();

            // Navigate to booking logs
            gotobookinglogs(event);

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Booking Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save booking. Please try again.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        if (fullname.getText() == null || fullname.getText().trim().isEmpty()) {
            showAlert("Please enter your full name.");
            return false;
        }
        if (gmail.getText() == null || gmail.getText().trim().isEmpty()) {
            showAlert("Please enter your email.");
            return false;
        }
        if (contact.getText() == null || contact.getText().trim().isEmpty()) {
            showAlert("Please enter your contact number.");
            return false;
        }
        if (date.getValue() == null) {
            showAlert("Please select a date.");
            return false;
        }
        if (time.getValue() == null) {
            showAlert("Please select a time.");
            return false;
        }
        if (duration.getValue() == null) {
            showAlert("Please select duration.");
            return false;
        }

        // Check if date is not in the past
        if (date.getValue().isBefore(LocalDate.now())) {
            showAlert("Please select a future date.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveBookingToCSV() throws IOException {
        String fileName = "bookings.csv";
        boolean fileExists = new java.io.File(fileName).exists();

        try (FileWriter writer = new FileWriter(fileName, true)) {
            // Write header if file doesn't exist
            if (!fileExists) {
                writer.append("Customer Name,Guide Name,Contact,Date,Time,Duration,Total,Email,Promo Code,Booking Date\n");
            }

            // Write booking data - handle commas in data by quoting
            writer.append("\"").append(fullname.getText().trim()).append("\",");
            writer.append("\"").append(GUIDE_NAME).append("\",");
            writer.append("\"").append(contact.getText().trim()).append("\",");
            writer.append("\"").append(date.getValue().toString()).append("\",");
            writer.append("\"").append(time.getValue()).append("\",");
            writer.append("\"").append(duration.getValue()).append("\",");
            writer.append("\"").append(total.getText().replace("NRP | ", "")).append("\",");
            writer.append("\"").append(gmail.getText().trim()).append("\",");
            writer.append("\"").append(promocode.getText() != null ? promocode.getText().trim() : "").append("\",");
            writer.append("\"").append(LocalDate.now().toString()).append("\"\n");
        }

        // Debug: Print confirmation that data was saved
        System.out.println("Booking saved to CSV: " + fullname.getText() + " for guide " + GUIDE_NAME);
    }

    @FXML
    public void gotoavailableguides(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("availableguides.fxml")));
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotobookinglogs(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("bookinglogs.fxml")));
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}