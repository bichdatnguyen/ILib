package org.example.ilib.AdminMenu.Member;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.example.ilib.Controller.DBConnection;

import java.io.IOException;
import java.sql.SQLException;

import static org.example.ilib.AdminMenu.Member.ControllerMemberAdminstrator.editMode;

public class ControllerAddMemberDetail {
    @FXML
    public TextField userFullname;

    @FXML
    public TextField userEmailAddress;

    @FXML
    public TextField userPhoneNumber;

    @FXML
    public TextField userPassword;

    @FXML
    public RadioButton userRoleAsAdmin;

    @FXML
    public RadioButton userRoleAsUser;

    @FXML
    public Button acceptButton;

    @FXML
    public Button cancelButton;

    @FXML
    public Label alertField;

    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();
        userRoleAsAdmin.setToggleGroup(toggleGroup);
        userRoleAsUser.setToggleGroup(toggleGroup);

        if (editMode == true) {
            setDefaultInformation(
                    MemberAdminstrator.getInstance().getMemberEmailAddress(),
                    MemberAdminstrator.getInstance().getMemberPassword(),
                    MemberAdminstrator.getInstance().getMemberFullname(),
                    MemberAdminstrator.getInstance().getMemberPhoneNumber(),
                    "user"
            );
        }
    }

    /**
     * this method keep editButton in MemberAdminstrator work
     * @param userEmail email address of user
     * @param userPassword password of user
     * @param userFullname fullname of user
     * @param userPhoneNumber phone number of user
     * @param userRole role of user
     */
    public void setDefaultInformation(String userEmail, String userPassword,
                                      String userFullname, String userPhoneNumber,
                                      String userRole) {
        this.userEmailAddress.setText(userEmail);
        this.userPassword.setText(userPassword);
        this.userFullname.setText(userFullname);
        this.userPhoneNumber.setText(userPhoneNumber);
        if (userRole.equals("admin")) {
            this.userRoleAsAdmin.setSelected(true);
        } else {
            this.userRoleAsUser.setSelected(true);
        }
    }

    public void acceptButtonClicked(MouseEvent mouseEvent) throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        String uFullname = userFullname.getText();
        String uEmailAddress = userEmailAddress.getText();
        String uPhoneNumber = userPhoneNumber.getText();
        String uPassword = userPassword.getText();
        String userRole = new String();
        if (userRoleAsAdmin.isSelected()) {
            userRole = "admin";
        }

        if (userRoleAsUser.isSelected()) {
            userRole = "user";
        }

        if (editMode == false) {
            //checking account in database
            if (connection.checkDataExit(uEmailAddress)) {
                alertField.setText("Email address has already in database");
                alertField.setStyle("-fx-text-fill: red;");
                alertField.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event -> alertField.setVisible(false));
                pause.play();
            } else {
                alertField.setText("Changing database successfully");
                alertField.setStyle("-fx-text-fill: green;");
                alertField.setVisible(true);

                connection.insertUserDetail(uFullname,uEmailAddress,uPhoneNumber,uPassword,userRole);

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event -> alertField.setVisible(false));
                pause.play();

            }
        } else {
            alertField.setText("Changing database successfully");
            alertField.setStyle("-fx-text-fill: green;");
            alertField.setVisible(true);

            connection.insertUserDetail(uFullname,uEmailAddress,uPhoneNumber,uPassword,userRole);

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> alertField.setVisible(false));
            pause.play();
        }

    }

    public void cancelButtonClicked(MouseEvent mouseEvent) throws IOException {
        userFullname.clear();
        userEmailAddress.clear();
        userPassword.clear();
        userPhoneNumber.clear();
        userRoleAsUser.setSelected(false);
        userRoleAsAdmin.setSelected(false);
    }
}
