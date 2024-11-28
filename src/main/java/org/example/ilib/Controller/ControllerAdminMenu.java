package org.example.ilib.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAdminMenu implements Initializable {

    @FXML
    public AnchorPane ShowScene;
    @FXML
    private HBox searchhbox;
    @FXML
    private HBox exithbox;

    @FXML
    private HBox userhbox;

    @FXML
    private HBox bookshbox;

    @FXML
    void SearchHboxEnter(MouseEvent event) {
        searchhbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void SearchHboxExit(MouseEvent event) {
        searchhbox.setStyle("-fx-background-color: #fff");
    }
    @FXML
    void SearchHboxClick(MouseEvent event) {

    }

    @FXML
    void BookHboxEnter(MouseEvent event) {
        bookshbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void BookHboxExit(MouseEvent event) {
        bookshbox.setStyle("-fx-background-color: #fff");
    }
    @FXML
    void BookHboxClick(MouseEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/BookAdmin.fxml"));
            AnchorPane memberEditPane = fxmlLoader.load();
            ShowScene.getChildren().clear(); // Xóa các phần tử cũ trong AnchorPane
            ShowScene.getChildren().add(memberEditPane); // Thêm nội dung mới

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void UserHboxEnter(MouseEvent event) {
        userhbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void UserHboxExit(MouseEvent event) {
        userhbox.setStyle("-fx-background-color: #fff");
    }
    @FXML
    void UserHboxClick(MouseEvent event) {

    }

    @FXML
    void ExitHboxEnter(MouseEvent event) {
        exithbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void ExitHboxExit(MouseEvent event) {
        exithbox.setStyle("-fx-background-color: #fff");
    }
    @FXML
    void ExitHboxClick(MouseEvent event) {
        Alert alert = showErrAndEx.showAlert("Bạn có muốn thoát ra không");
        if(alert.getResult() == ButtonType.OK){
            System.exit(0);
            Platform.exit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       try{
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/MemberAdminstrator.fxml"));
           AnchorPane memberEditPane = fxmlLoader.load();
           ShowScene.getChildren().clear(); // Xóa các phần tử cũ trong AnchorPane
           ShowScene.getChildren().add(memberEditPane); // Thêm nội dung mới

       } catch(IOException e){
           e.printStackTrace();
       }

    }
}
