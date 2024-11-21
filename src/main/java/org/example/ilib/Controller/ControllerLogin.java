package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerLogin {
    @FXML
    private Button LoginSuccessButton;

    @FXML
    private TextField emailText;
    @FXML
    private TextField passwordText;

    /**
     * this method will switch to Menu scene.
     *
     * @param event MouseEvent
     * @throws IOException in case FXML file cannot be loaded
     */
    @FXML
    void loginSuccess(MouseEvent event) throws IOException, SQLException {

        DBConnection db = DBConnection.getInstance();

         if(!db.checkDataExit(emailText.getText(), passwordText.getText())) {
            showErrAndEx.showAlert("Email hoặc mật khẩu kong đúng");
         }


        Stage stage = (Stage) LoginSuccessButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

    }

    /**
     * this method will force user to back to the Menu.
     *
     * @param event MouseEvent
     * @throws IOException in case FXML file cannot be loaded
     */
    public void backToMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/LoginAndRegister.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}
