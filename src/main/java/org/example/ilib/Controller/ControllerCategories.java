package org.example.ilib.Controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCategories extends Book_list implements Initializable {
    @FXML
    private Button Back;
    @FXML
    private HBox sach_moi_hbox;
    @FXML
    private HBox tai_lieu_hbox;
    @FXML
    private HBox tai_lieu_2_hbox;

    public ControllerCategories() {
        super(Book_list.CATEGORIES_BOOK);
    }
    public void BackToMenu(MouseEvent event)throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
    }


    public void initialize(URL location, ResourceBundle resources) {

        try {
            System.out.println(bookList.size() );
            for (int i = 0; i < 4 ; i++ ) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));
                sach_moi_hbox.getChildren().add(cardbox);


            }
            for (int i = 4; i < 8 ; i++ ) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));
                tai_lieu_hbox.getChildren().add(cardbox);


            }
            for (int i = 8; i < 13 ; i++ ) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));
                tai_lieu_2_hbox.getChildren().add(cardbox);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
