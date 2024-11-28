package org.example.ilib.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.ilib.Processor.Member;

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
}
