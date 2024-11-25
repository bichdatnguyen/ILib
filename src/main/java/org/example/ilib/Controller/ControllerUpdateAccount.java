package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;
import org.example.ilib.Processor.updateAccount;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControllerUpdateAccount {
    @FXML
    public Button backButton;
    public TextField emailBox;
    public TextField phoneNumberBox;
    public TextField userNameBox;
    public TextField oldPassWordBox;
    public TextField acceptedPassword;
    public TextField newPassWordBox;
    public Button commitButton;
    public Text commentText;

    public void initialize() {
        emailBox.setText(Account.getInstance().getEmail());
        phoneNumberBox.setText(Account.getInstance().getPhone());
        userNameBox.setText(Account.getInstance().getFullName());
    }
    public void backToAccountScene(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage)backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Account.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    private boolean checkingOldPassword() {
        return oldPassWordBox.getText().equals(Account.getInstance().getPassword());
    }

    private boolean checkingNewPassword() {
        return newPassWordBox.getText().equals(updateAccount.getInstance().getAcceptedPassword());
    }

    private void updatePropertiesInDatabase() {
        String nEmail = updateAccount.getInstance().getNewEmail();
        String nPhoneNumber = updateAccount.getInstance().getNewPhoneNumber();
        String nPassword = updateAccount.getInstance().getNewPassword();
        String nFullname = updateAccount.getInstance().getNewName();
        String updateQuery = "UPDATE user SET Email = ?, "
                + "phoneNumber=?, fullName=?, password=? "
                + "where Email = ? and password = ?";
        try {
            //DBConnection conn = DBConnection.getInstance().getConnection().prepareStatement(updateQuery)
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(updateQuery);
            stmt.setString(1, nEmail);
            stmt.setString(2, nPhoneNumber);
            stmt.setString(3, nFullname);
            stmt.setString(4, nPassword);
            stmt.setString(5, Account.getInstance().getEmail());
            stmt.setString(6, Account.getInstance().getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commited(MouseEvent keyEvent) {
        updateAccount.getInstance().setNewEmail(emailBox.getText());
        System.out.println("new Email: " + updateAccount.getInstance().getNewEmail());
        updateAccount.getInstance().setNewName(userNameBox.getText());
        updateAccount.getInstance().setAcceptedPassword(acceptedPassword.getText());
        updateAccount.getInstance().setNewPassword(newPassWordBox.getText());
        updateAccount.getInstance().setNewPhoneNumber(phoneNumberBox.getText());
        if (checkingOldPassword()) {
            if (checkingNewPassword()) {
                updatePropertiesInDatabase();
                commentText.setText("Cập nhật thông tin thành công!");
                commentText.setStyle("-fx-fill: green;");
            } else {
                commentText.setText("Mật khẩu mới không khớp.");
                commentText.setStyle("-fx-fill: red;");
            }
        } else {
            commentText.setText("Mật khẩu cũ không khớp?");
            commentText.setStyle("-fx-fill: red;");
        }
    }
}
