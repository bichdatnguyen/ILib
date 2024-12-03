package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ControllerTopBooks implements Initializable {

    @FXML
    private Button Back;

    @FXML
    private GridPane topBooksGrid;

    private List<Book> bookList;

    /**
     * ControllerTopBooks constructor.
     */
    public ControllerTopBooks() {
       bookList = Booklist.getInstance().getTopBookList();
    }

    /** top books initialize.
     * @param location location
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        try {
            int column = 0;
            int row = 0;

            for (int i = 0; i < Math.min(8, bookList.size()); i++) {
                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));
                controllerBook.showBook(bookList.get(i));

                if(column == 4) {
                    column = 0;
                    row++;
                }
                topBooksGrid.add(cardbox, column++, row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** back to menu.
     * @param e back to menu when button is clicked.
     * @throws IOException prevent IO exception
     */
    public void BackToMenu(MouseEvent e) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader newStage = new FXMLLoader();
        newStage.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(newStage.load());
        stage.setScene(scene);

    }
}
