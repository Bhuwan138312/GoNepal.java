package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Objects;

public class ArttractiondetailsController {


    @FXML
    public Button viewguides;
    @FXML
    public Button backbutton;


    public void availableguides(ActionEvent event) {
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
}
