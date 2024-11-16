package org.example.ilib;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerLoginAndRegister {

    @FXML
    private Button LoginButton;

    @FXML
    private Button RegisterButton;

    /**
     * this method will handle event related to Login scene.
     *
     * @param event MouseEvent
     * @throws IOException in case FXML file cannot be loaded
     */
    @FXML
    void Login(MouseEvent event) throws IOException {
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     * this method will handle event related to Register scene.
     *
     * @param event MouseEvent
     * @throws IOException in case FXML file cannot be loaded
     */
    @FXML
    void Register(MouseEvent event) throws IOException {
        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Register.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     * this method will be called when user want to quit application.
     *
     * @param event MouseEvent
     */
    public void Exit(MouseEvent event) {
        // Tạo thông báo xác nhận thoát
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit, or Cancel to stay.");

        // Hiển thị thông báo và chờ người dùng chọn
        if (alert.showAndWait().get() == ButtonType.OK) {
            // Người dùng xác nhận thoát, đóng cửa sổ
            Platform.exit();
            System.exit(0);
        }
        // Nếu chọn Cancel, không cần thực hiện gì thêm (tiếp tục chương trình)
    }


}

