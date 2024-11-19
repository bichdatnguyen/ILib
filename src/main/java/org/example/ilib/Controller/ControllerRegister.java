package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    void CreateAccount(MouseEvent event) throws IOException, SQLException {
        if(!(CCCDtextField.getText().matches("\\d+")) && CCCDtextField.getText().length() != 12){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid CCD number");
            alert.showAndWait();
            return;
        } else if(!emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
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

        } else if(!nameTextField.getText().matches("^[\\p{L}]+([\\s][\\p{L}]+)*$")){
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
            return;
        }
        DBConnection db = DBConnection.getInstance();

        boolean check = db.checkDataExit(emailTextField.getText());
        if(check){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Tai khoan nay da co");
            alert.showAndWait();
            return;
        }

        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String fullName = nameTextField.getText();
        String phoneNumber = phoneTextField.getText();
        String identityNumber = CCCDtextField.getText();
//        db.Initialize(email,password,fullName,phoneNumber,identityNumber);
//        db.createAccount();
        Stage stage = (Stage) CreateAccountButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

     public void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage)BackButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/LoginAndRegister.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
     }

}
