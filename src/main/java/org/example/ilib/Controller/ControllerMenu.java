package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;

public class ControllerMenu {
    @FXML
    private Button signOut;

    @FXML
    private TextField search;

    /**
     * signOutMenu handle MouseEvent event.
     * this method will switch the scene to the login and register screen.
     *
     * @param event belong to MouseEvent type
     * @throws IOException in case that FXML can not be used
     */
    public void signOutMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) signOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/LoginAndRegister.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        String searchText = search.getText();
        System.out.println(searchText);
    }

    public void handleSearch(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) { // Kiểm tra mã phím ENTER
            String searchText = search.getText().trim(); // Lấy nội dung từ trường tìm kiếm
            if (!searchText.isEmpty()) { // Đảm bảo không để trống
                GoogleBooksAPI api = new GoogleBooksAPI(searchText);
                if (api.getInformation() != null) { // Kiểm tra thông tin trả về từ API
                    // Lấy stage hiện tại
                    Stage stage = (Stage) search.getScene().getWindow();

                    // Load giao diện chi tiết sách
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                } else {
                    System.err.println("Không tìm thấy thông tin sách từ API.");
                }
            } else {
                System.err.println("Hãy nhập nội dung tìm kiếm.");
            }
        }
    }

}
