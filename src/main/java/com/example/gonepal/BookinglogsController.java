package com.example.gonepal;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookinglogsController implements Initializable {
    @FXML
    public Button backbutton;

    @FXML
    private TableView<BookingEntry> bookingTable;

    @FXML
    private TableColumn<BookingEntry, String> nameColumn;

    @FXML
    private TableColumn<BookingEntry, String> contactColumn;

    @FXML
    private TableColumn<BookingEntry, String> dateColumn;

    @FXML
    private TableColumn<BookingEntry, String> timeColumn;

    @FXML
    private TableColumn<BookingEntry, String> totalColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadBookingsFromCSV();
    }

    private void setupTableColumns() {
        // Set up table columns with proper cell value factories
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuideName()));
        contactColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        totalColumn.setCellValueFactory(cellData -> new SimpleStringProperty("NRP " + cellData.getValue().getTotal()));

        // Set column widths
        nameColumn.setPrefWidth(100);
        contactColumn.setPrefWidth(120);
        dateColumn.setPrefWidth(100);
        timeColumn.setPrefWidth(100);
        totalColumn.setPrefWidth(100);
    }

    private void loadBookingsFromCSV() {
        ObservableList<BookingEntry> bookings = FXCollections.observableArrayList();
        String fileName = "bookings.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }

                // Handle CSV parsing with potential commas in data
                String[] parts = parseCSVLine(line);
                if (parts.length >= 7) {
                    BookingEntry booking = new BookingEntry(
                            parts[0], // customerName
                            parts[1], // guideName
                            parts[2], // contact
                            parts[3], // date
                            parts[4], // time
                            parts[5], // duration
                            parts[6]  // total
                    );
                    bookings.add(booking);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing bookings file found or error reading file: " + e.getMessage());
            // Show message to user if no bookings found
            if (bookings.isEmpty()) {
                showNoBookingsMessage();
            }
        }

        // Set the data to the table
        bookingTable.setItems(bookings);

        if (bookings.isEmpty()) {
            showNoBookingsMessage();
        }
    }

    private String[] parseCSVLine(String line) {
        // Simple CSV parser that handles basic cases
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private void showNoBookingsMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Bookings");
        alert.setHeaderText(null);
        alert.setContentText("No booking records found. Make a booking first to see it here.");
        alert.showAndWait();
    }

    @FXML
    public void gotohomescreen(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homescreen.fxml")));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Public class to hold booking data
    public static class BookingEntry {
        private final String customerName;
        private final String guideName;
        private final String contact;
        private final String date;
        private final String time;
        private final String duration;
        private final String total;

        public BookingEntry(String customerName, String guideName, String contact,
                            String date, String time, String duration, String total) {
            this.customerName = customerName;
            this.guideName = guideName;
            this.contact = contact;
            this.date = date;
            this.time = time;
            this.duration = duration;
            this.total = total;
        }

        // Getters
        public String getCustomerName() { return customerName; }
        public String getGuideName() { return guideName; }
        public String getContact() { return contact; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getDuration() { return duration; }
        public String getTotal() { return total; }
    }
}