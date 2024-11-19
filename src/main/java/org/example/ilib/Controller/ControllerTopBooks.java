package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class ControllerTopBooks {
    @FXML
    private Button Back;
    public void BackToMenu(MouseEvent e) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader newStage = new FXMLLoader();
        newStage.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(newStage.load());
        stage.setScene(scene);

    }
}
