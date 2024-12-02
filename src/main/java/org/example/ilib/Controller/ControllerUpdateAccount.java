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

    private void updatePropertiesInDatabase(String newE, String newP) {
        //String nEmail = updateAccount.getInstance().getNewEmail();
        String nEmail = newE;

        String nPhoneNumber = updateAccount.getInstance().getNewPhoneNumber();

        //String nPassword = updateAccount.getInstance().getNewPassword();
        String nPassword = newP;

        String nFullname = updateAccount.getInstance().getNewName();
        String updateQuery = "UPDATE user SET Email = ?, "
                + "phoneNumber=?, fullName=?, password=? "
                + "where Email = ? and password = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(updateQuery)){
            //DBConnection conn = DBConnection.getInstance().getConnection().prepareStatement(updateQuery)

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
            // case user did not change his/her password
            if (newPassWordBox.getText().equals("")) {
                String e = updateAccount.getInstance().getNewEmail();
                String p = Account.getInstance().getPassword();
                if (!e.equals(Account.getInstance().getEmail())) {
                    try {
                        if (DBConnection.getInstance().checkDataExit(e,p)) {
                            commentText.setText("Email and password already exist in the database!");
                            commentText.setStyle("-fx-fill: red;");
                            return;
                        }
                    } catch (SQLException eN) {
                        eN.printStackTrace();
                    }
                }
                updatePropertiesInDatabase(updateAccount.getInstance().getNewEmail(),Account.getInstance().getPassword());
                commentText.setText("Information updated successfully!");
                commentText.setStyle("-fx-fill: green;");
                return;
            }
            // case user changed his/her password
            if (checkingNewPassword()) {
                String e = updateAccount.getInstance().getNewEmail();
                String p = updateAccount.getInstance().getNewPassword();
                try {
                    if (!DBConnection.getInstance().checkDataExit(e,p)) {
                        updatePropertiesInDatabase(updateAccount.getInstance().getNewEmail(),updateAccount.getInstance().getNewPassword());
                        Account.getInstance().setEmail(e);
                        Account.getInstance().setPassword(p);
                        commentText.setText("Information updated successfully!");
                        commentText.setStyle("-fx-fill: green;");
                    } else {
                        commentText.setText("Email and password are already in the database!");
                        commentText.setStyle("-fx-fill: red;");
                    }
                } catch (SQLException t) {
                    System.out.println(t.getMessage());
                }
            } else {
                commentText.setText("New password does not match.");
                commentText.setStyle("-fx-fill: red;");
            }
        } else {
            commentText.setText("Old password does not match?");
            commentText.setStyle("-fx-fill: red;");
        }
    }

    public void changingPasswordAlert(MouseEvent mouseEvent) {
        commentText.setText("Do you want to change your password?");
        commentText.setStyle("-fx-fill: blue;");
    }

    public void setCommentHidden(MouseEvent mouseEvent) {
        commentText.setText("");
    }

    public void acceptedPasswordAlert(MouseEvent mouseEvent) {
        commentText.setText("Re-enter the password if you have filled in the field above.");
        commentText.setStyle("-fx-fill: blue;");
    }

    public void oldPasswordAlert(MouseEvent mouseEvent) {
        commentText.setText("Enter your current password to confirm the changes (required).");
        commentText.setStyle("-fx-fill: blue;");
    }

    public void userNameAlert(MouseEvent mouseEvent) {
        commentText.setText("Input to change your first and last name.");
        commentText.setStyle("-fx-fill: blue;");
    }

    public void phoneAlert(MouseEvent mouseEvent) {
        commentText.setText("Input to change your phone number.");
        commentText.setStyle("-fx-fill: blue;");
    }

    public void emailAlert(MouseEvent mouseEvent) {
        commentText.setText("Input to change your email address.");
        commentText.setStyle("-fx-fill: blue;");
    }
}
