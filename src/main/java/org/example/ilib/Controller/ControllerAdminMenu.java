package org.example.ilib.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
    private HBox menuhbox;
    @FXML
    private HBox exithbox;

    @FXML
    private HBox userhbox;

    @FXML
    private HBox bookshbox;
    @FXML
    private Label AdminLabel;

    @FXML
    void MenuHboxEnter(MouseEvent event) {
        menuhbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void MenuHboxExit(MouseEvent event) {
        menuhbox.setStyle("-fx-background-color: transparent");
    }
    @FXML
    void MenuHboxClick(MouseEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/MenuAdmin.fxml"));
            AnchorPane memberEditPane = fxmlLoader.load();
            ShowScene.getChildren().clear(); // Xóa các phần tử cũ trong AnchorPane
            ShowScene.getChildren().add(memberEditPane); // Thêm nội dung mới

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void BookHboxEnter(MouseEvent event) {
        bookshbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void BookHboxExit(MouseEvent event) {
        bookshbox.setStyle("-fx-background-color: transparent");
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
        userhbox.setStyle("-fx-background-color: transparent");
    }
    @FXML
    void UserHboxClick(MouseEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/MemberAdminstrator.fxml"));
            AnchorPane memberEditPane = fxmlLoader.load();
            ShowScene.getChildren().clear(); // Xóa các phần tử cũ trong AnchorPane
            ShowScene.getChildren().add(memberEditPane); // Thêm nội dung mới

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void ExitHboxEnter(MouseEvent event) {
        exithbox.setStyle("-fx-background-color: green");
    }
    @FXML
    void ExitHboxExit(MouseEvent event) {
        exithbox.setStyle("-fx-background-color: transparent");
    }
    @FXML
    void ExitHboxClick(MouseEvent event) {
        Alert alert = showErrAndEx.showAlert("Bạn có muốn thoát ra không");
        if(alert.getResult() == ButtonType.OK){
           try{
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Menu.fxml"));
               Stage stage = (Stage) ShowScene.getScene().getWindow();
               Scene scene = new Scene(fxmlLoader.load());
               stage.setScene(scene);
               stage.show();
           } catch(IOException e){
               e.printStackTrace();
           }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // AdminLabel.setStyle("-fx-background-color: black");
        menuhbox.setStyle("-fx-background-color: transparent");
        bookshbox.setStyle("-fx-background-color: transparent");
        userhbox.setStyle("-fx-background-color: transparent");
        exithbox.setStyle("-fx-background-color: transparent");
       try{
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/MenuAdmin.fxml"));
           AnchorPane memberEditPane = fxmlLoader.load();
           ShowScene.getChildren().clear(); // Xóa các phần tử cũ trong AnchorPane
           ShowScene.getChildren().add(memberEditPane); // Thêm nội dung mới

       } catch(IOException e){
           e.printStackTrace();
       }

    }
}
