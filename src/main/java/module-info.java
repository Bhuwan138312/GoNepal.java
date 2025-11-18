module com.example.gonepal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gonepal to javafx.fxml;
    exports com.example.gonepal;
}