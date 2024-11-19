package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.ilib.Processor.Book;



public class ControllerBook  {
    @FXML
    private ImageView image;
    @FXML
    private Label title;
    @FXML
    private Label author;
    @FXML
    private HBox book_layout;

    void setBook(Book book) {
        Image image1  = new Image(getClass().getResourceAsStream(book.getImage()));
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        image.setImage(image1);

    }











}
