package org.example.ilib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerRegister {
    @FXML
    private Button CreateAccountButton;
    @FXML
    TextField CCCDtextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    private Button BackButton;
    @FXML
    void CreateAccount(MouseEvent event) throws IOException {
        if(!(CCCDtextField.getText().matches("\\d+")) && CCCDtextField.getText().length() != 12){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid CCD number");
            alert.showAndWait();
            return;
        } else if(!emailTextField.getText().matches("[a-zA-Z]+@gmail.com")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid email address");
            alert.showAndWait();
            return;
        } else if(!passwordTextField.getText().matches("[0-9a-zA-Z]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid password");
            alert.showAndWait();
            return;

        } else if(!nameTextField.getText().matches("[a-zA-Z]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid name");
            alert.showAndWait();
            return;
        } else if(!phoneTextField.getText().matches("[0-9]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid phone number");
            alert.showAndWait();
            return ;
        }


        Stage stage = (Stage) CreateAccountButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
     public void Back(ActionEvent event) throws IOException {
        Stage stage = (Stage)BackButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/LoginAndRegister.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
     }

}
