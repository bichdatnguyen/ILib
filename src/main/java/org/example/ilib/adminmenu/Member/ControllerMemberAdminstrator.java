package org.example.ilib.adminmenu.Member;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ilib.controller.DBConnection;
import org.example.ilib.controller.showErrAndEx;

import java.io.IOException;
import java.sql.*;

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
    public ScrollPane searchingScrolPane;
    public VBox searchVbox;
    public TextField searchText;

    public void initialize() {
        tableView.getItems().clear();
        getInformationOfUsers();
        emailC.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        nameC.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneC.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        passwordC.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        tableView.setItems(members);
        tableView.refresh();
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
        initialize();
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
            initialize();
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
            initialize();
        }
    }

    public void refreshButtonClicked(MouseEvent mouseEvent) {
        initialize();
    }

    public void searchInfofromDB(String search) throws SQLException {
        String query = "select email from user where email like ? "
                + "or phoneNumber like ? "
                + "or fullname like ? "
                + "or password like ?;";
        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String queryPattern = "%" + search + "%";
            preparedStatement.setString(1, queryPattern);
            preparedStatement.setString(2, queryPattern);
            preparedStatement.setString(3, queryPattern);
            preparedStatement.setString(4, queryPattern);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String title = resultSet.getString("email");
                    Text text = new Text(title);
                    text.setOnMouseClicked(mouseEvent -> {
                        searchText.clear();
                        searchText.setText(text.getText());
                    });
                    text.setOnMouseEntered(mouseEvent -> {
                        text.setFill(javafx.scene.paint.Color.BLUE);
                    });
                    text.setOnMouseExited(mouseEvent -> {
                        text.setFill(javafx.scene.paint.Color.BLACK);
                    });
                    searchVbox.getChildren().add(text);
                }
                if (!searchVbox.getChildren().isEmpty()) {
                    searchingScrolPane.setVisible(true);
                } else {
                    searchingScrolPane.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void searchEnter(KeyEvent keyEvent) {
        if(!searchText.getText().equals("")){
            FilteredList<Member> filteredBooks = new FilteredList<>(members, b -> true);
            tableView.setItems(filteredBooks);
            searchText.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredBooks.setPredicate(Member -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Member.emailProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (Member.nameProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
        }
    }

    public void searchKeyboard(KeyEvent keyEvent) throws SQLException {
        if(!searchText.getText().equals("")){
            searchVbox.getChildren().clear();
            searchInfofromDB(searchText.getText());
        } else{
            searchVbox.getChildren().clear();
            searchingScrolPane.setVisible(false);
        }
    }
}
