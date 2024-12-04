package org.example.ilib.booklist;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.book.ControllerBook;
import org.example.ilib.book.Book;
import org.example.ilib.controller.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class ControllerReading implements Initializable {
    @FXML
    private Button Back;

    @FXML
    private HBox borrowBooks;

    @FXML
    private HBox saveBooks;

    /** back to menu.
     * @param event back to menu when button is clicked
     * @throws IOException prevent IO exception
     */
    public void BackToMenu(MouseEvent event)throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /** show all user's borrow books.
     * @throws IOException prevent IO exception
     * @throws SQLException prevent SQL exception
     */
    public void showBorrowBooks() throws IOException, SQLException {
        List<Book> book = DBConnection.getInstance().allBorrowBooks();

        for (int i = 0; i < book.size(); i++) {
            FXMLLoader fx = new FXMLLoader();
            fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
            HBox cardbox = (HBox) fx.load();
            ControllerBook controllerBook = (ControllerBook) fx.getController();
            controllerBook.setBook(book.get(i));
            controllerBook.showBook(book.get(i));
            borrowBooks.getChildren().add(cardbox);
        }
    }

    /** user's saved books.
     * @throws SQLException prevent SQL exception
     * @throws IOException prevent IO exception
     */
    public void showShelf() throws SQLException, IOException {
        List<Book> book = DBConnection.getInstance().allBookInShelf();

        for (int i = 0; i < book.size(); i++) {
            FXMLLoader fx = new FXMLLoader();
            fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
            HBox cardbox = (HBox) fx.load();
            ControllerBook controllerBook = (ControllerBook) fx.getController();
            controllerBook.setBook(book.get(i));
            controllerBook.showBook(book.get(i));
            saveBooks.getChildren().add(cardbox);
        }
    }

    /** shelf initialize.
     * @param location location
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        borrowBooks.getChildren().clear();
        saveBooks.getChildren().clear();

        try {
            showShelf();
            showBorrowBooks();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
