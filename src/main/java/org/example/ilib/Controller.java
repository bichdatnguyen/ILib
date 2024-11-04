package org.example.ilib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {

    @FXML
    private Button LoginButton;

    @FXML
    private Button RegisterButton;

    @FXML
    private Button CreateAccountButton;

    @FXML
    private Button LoginSuccessButton;

    @FXML
    private TextField RegisterEmailLabel;

    @FXML
    private TextField RegisterFullNameLabel;

    @FXML
    private TextField RegisterIdentityNumberLabel;

    @FXML
    private PasswordField RegisterPasswordLabel;

    @FXML
    private TextField RegisterPhoneNumberLabel;

    @FXML
    private TextField search;

    @FXML
    private TextArea printInfo;

    @FXML
    void LoginAccount(MouseEvent event) throws IOException {
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    @FXML
    void RegisterAccount(MouseEvent event) throws IOException {
        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Register.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    @FXML
    void CreateAccount(MouseEvent event) throws IOException, SQLException {
        Stage stage = (Stage) CreateAccountButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        String email = RegisterEmailLabel.getText();
        String fullName = RegisterFullNameLabel.getText();
        String identityNumber = RegisterIdentityNumberLabel.getText();
        String password = RegisterPasswordLabel.getText();
        String phoneNumber = RegisterPhoneNumberLabel.getText();
        DBConnection db = new DBConnection(email, password, fullName, phoneNumber, identityNumber);
        db.createAccount();
    }

    @FXML
    void loginSuccess(MouseEvent event) throws IOException {
        Stage stage = (Stage) LoginSuccessButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    @FXML
    void find(ActionEvent event) throws IOException {
        Stage stage = (Stage) search.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        String query = search.getText();
        GoogleBooksAPI qr = new GoogleBooksAPI(query);
        qr.information();
    }
}