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


public class ControllerBook  {
    @FXML
    private ImageView image;
    @FXML
    private Label title;
    @FXML
    private Label author;




    void setBook(Book book) {
        Image image1  = new Image(getClass().getResourceAsStream(book.getImage()));
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        image.setImage(image1);

    }
    @FXML
    void gotoBookDetail(MouseEvent event) throws IOException {
        Stage stage = (Stage) image.getScene().getWindow();


        FXMLLoader fx = new FXMLLoader(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
        Parent root = fx.load();
        ControllerBookDetail controllerBookDetail = fx.getController();
        controllerBookDetail.setAuthorText(author.getText());
        controllerBookDetail.setTitleText(title.getText());
        controllerBookDetail.setThumbnail(image.getImage());

        stage.setScene(new Scene(root));
    }







}