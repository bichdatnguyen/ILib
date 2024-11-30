package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.sql.SQLException;


public class ControllerSearchHint  {
    @FXML
    private Label bookTitle;

    private Book book;

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return this.book;
    }

    public void showBook(Book book) {
        bookTitle.setText(book.getTitle());
    }

    @FXML
    public void gotoBookDetail(MouseEvent event) throws IOException, SQLException {
        Stage stage = (Stage) bookTitle.getScene().getWindow();

        FXMLLoader fx = new FXMLLoader(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
        Parent root = fx.load();
        ControllerBookDetail controllerBookDetail = fx.getController();
        controllerBookDetail.saveForwardScene(bookTitle.getScene());
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
