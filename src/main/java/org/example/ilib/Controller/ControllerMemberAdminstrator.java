package org.example.ilib.Controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ilib.Processor.Member;
import org.example.ilib.Processor.MemberAdminstrator;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControllerMemberAdminstrator {
    public TableView<Member> tableView;
    public TableColumn<Member,String> emailC;
    public TableColumn<Member,String> nameC;
    public TableColumn<Member,String> phoneC;
    public TableColumn<Member,String> passwordC;
    public ObservableList<Member> members = FXCollections.observableArrayList();
    public Button backButton;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;
    public Button refreshButton;
    public static boolean editMode = false;

    public void initialize() {
        getInformationOfUsers();
        emailC.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        nameC.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneC.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        passwordC.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());

        tableView.setItems(members);

    }

    private void getInformationOfUsers() {
        try {
            DBConnection connection = DBConnection.getInstance();

            String query = "SELECT email, fullname, phoneNumber, password FROM user";
            Statement stmt = connection.createStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("fullname");
                String phone = rs.getString("phoneNumber");
                String password = rs.getString("password");

                members.add(new Member(email, name, phone, password));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public void backClicked(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
    }

    public void addMemberClicked(MouseEvent mouseEvent) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/AddMemberDetail.fxml"));
            Parent root = fxmlLoader.load();
            ControllerAddMemberDetail controllerAddMemberDetail = fxmlLoader.getController();
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Add User detail");
            Scene scene = new Scene(root);
            popup.setScene(scene);

            popup.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editButtonClicked(MouseEvent mouseEvent) throws IOException, SQLException {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            showErrAndEx.showAlert("Please select an account");
        } else {
            editMode = true;
            MemberAdminstrator.getInstance().setMemberFullname(
                    tableView.getSelectionModel().getSelectedItem().nameProperty().get()
            );
            MemberAdminstrator.getInstance().setMemberPhoneNumber(
                    tableView.getSelectionModel().getSelectedItem().phoneProperty().get()
            );
            MemberAdminstrator.getInstance().setMemberEmailAddress(
                    tableView.getSelectionModel().getSelectedItem().emailProperty().get()
            );
            MemberAdminstrator.getInstance().setMemberPassword(
                    tableView.getSelectionModel().getSelectedItem().passwordProperty().get()
            );
            deleteButtonClicked(mouseEvent);
            addMemberClicked(mouseEvent);
        }

        editMode = false;
    }

    public void deleteButtonClicked(MouseEvent mouseEvent) throws SQLException {
        if(tableView.getSelectionModel().getSelectedItem() == null){
            showErrAndEx.showAlert("Please select an account");
        } else {
            if (editMode == false) {
                Alert alert = showErrAndEx.showAlert("Bạn chắc chắn muốn xóa chứ");
                if(alert.getResult() == ButtonType.OK){
                    String selectedEmail = tableView.getSelectionModel().getSelectedItem().emailProperty().get();
                    String selectedPassword = tableView.getSelectionModel().getSelectedItem().passwordProperty().get();
                    DBConnection.getInstance().deleteUserDetail(selectedEmail, selectedPassword);
                }
            } else {
                String selectedEmail = tableView.getSelectionModel().getSelectedItem().emailProperty().get();
                String selectedPassword = tableView.getSelectionModel().getSelectedItem().passwordProperty().get();
                DBConnection.getInstance().deleteUserDetail(selectedEmail, selectedPassword);
            }
        }
    }

    public void refreshButtonClicked(MouseEvent mouseEvent) {
        tableView.getItems().clear();
        initialize();
    }
}
