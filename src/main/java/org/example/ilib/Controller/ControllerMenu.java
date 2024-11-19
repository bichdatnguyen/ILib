package org.example.ilib.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;

public class ControllerMenu {
    @FXML
    private MenuItem signOut;

    @FXML
    private TextField search;

    @FXML
    private Label topBooks;

    @FXML
    private Label Categories;
    @FXML
    private Label reading;



    /**
     * signOutMenu handle MouseEvent event.
     * this method will switch the scene to the login and register screen.
     *
     * @param actionEvent belong to MouseEvent type
     * @throws IOException in case that FXML can not be used
     */
    public void signOutMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) signOut.getParentPopup().getOwnerWindow();
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


    public void topBookMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) topBooks.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/TopBooks.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void categoriesMenu(MouseEvent e) throws IOException {
        Stage stage = (Stage) Categories.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Categories.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void readingMenu(MouseEvent e) throws IOException {
        Stage stage = (Stage) reading.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Reading.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
