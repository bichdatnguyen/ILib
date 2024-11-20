package org.example.ilib.Controller;

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


public class ControllerTopBooks extends Booklist implements Initializable {

    @FXML
    private Button Back;
    @FXML
    private HBox top_book_hbox;
    @FXML
    private HBox hbox_top_book2;

    public ControllerTopBooks() {
        super(Booklist.TOP_BOOK);
    }



    public void initialize(URL location, ResourceBundle resources) {

        try {

            for (int i = 0; i < 5 ; i++ ) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));
                top_book_hbox.getChildren().add(cardbox);


            }
            for (int i = 5; i < 9 ; i++ ) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));
                hbox_top_book2.getChildren().add(cardbox);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void BackToMenu(MouseEvent e) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader newStage = new FXMLLoader();
        newStage.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(newStage.load());
        stage.setScene(scene);

    }
}
