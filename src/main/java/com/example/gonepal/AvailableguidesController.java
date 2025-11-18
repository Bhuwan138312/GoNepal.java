package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Objects;

public class AvailableguidesController {

    @FXML
    public Button homebutton;
    @FXML
    public Button backbutton;
    @FXML
    public Button book;

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

    @FXML
    public void gotoattractiondetails(ActionEvent event) {
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

    @FXML
    public void gotoguidebook(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("bookguide.fxml")));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
