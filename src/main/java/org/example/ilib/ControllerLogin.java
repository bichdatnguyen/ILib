package org.example.ilib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerLogin {
    @FXML
    private Button LoginSuccessButton;

    @FXML
    void loginSuccess(MouseEvent event) throws IOException {
        Stage stage = (Stage) LoginSuccessButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

    }

    public void backToMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginAndRegister.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}
