package org.example.ilib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerMenu {
    @FXML
    private Button signOut;
     public void signOutMenu(ActionEvent event) throws IOException {
         Stage stage = (Stage) signOut.getScene().getWindow();
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginAndRegister.fxml"));
         Parent root = fxmlLoader.load();
         Scene scene = new Scene(root);
         stage.setScene(scene);
     }
}
