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
        if(!emailTextField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            showErrAndEx.showAlert("Email không hợp lệ");
            return;
        }
        if(!phoneTextField.getText().matches("[0-9]+")){
            showErrAndEx.showAlert("Số điện thoại không hợp lệ");
            return;
        }
        if(!nameTextField.getText().matches("^[\\p{L}]+([\\s][\\p{L}]+)*$")){
            showErrAndEx.showAlert("Tên không hợp lệ");
            return;
        }
        if(!passwordTextField.getText().matches("[0-9a-zA-Z]+")){
            showErrAndEx.showAlert("Password không hợp lệ");
            return;
        }

        DBConnection db = DBConnection.getInstance();

        boolean check = db.checkDataExit(emailTextField.getText(),passwordTextField.getText());
        if(check) {
           showErrAndEx.showAlert("Tài khoản hiện đã có");
           return;
        }
        boolean check2 = db.checkDataExit(emailTextField.getText());
        if(check2){
           showErrAndEx.showAlert("Email này da tồn tại");
           return;
        }
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String fullName = nameTextField.getText();
        String phoneNumber = phoneTextField.getText();
        db.createAccount(email, phoneNumber, fullName, password, null);
        db.createVoucher(email, 50);

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
