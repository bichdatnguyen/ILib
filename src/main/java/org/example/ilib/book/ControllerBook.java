package org.example.ilib.book;

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
import org.example.ilib.book.BookDetail.ControllerBookDetail;

import java.io.IOException;
import java.sql.SQLException;


public class ControllerBook {

    @FXML
    private ImageView image;
    @FXML
    private Label title;
    @FXML
    private Label author;
    @FXML
    private HBox BookHbox;

    private Book book;

    private Image loadImage(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return new Image(path, true);
        } else {
            return new Image(getClass().getResourceAsStream(path));
        }
    }

    /**
     * this.book setter.
     *
     * @param book set to this.book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * this.book getter.
     *
     * @return this.book
     */
    public Book getBook() {
        return this.book;
    }

    /**
     * show book information.
     *
     * @param book information needed to show
     */
    public void showBook(Book book) {
        Image img = loadImage(book.getImage());
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        image.setImage(img);
    }

    /**
     * go to book detail.
     *
     * @param event after click book will go to book detail
     * @throws IOException  prevent IO exception
     * @throws SQLException prevent sql exception
     */
    @FXML
    public void gotoBookDetail(MouseEvent event) throws IOException, SQLException {
        Stage stage = (Stage) image.getScene().getWindow();

        FXMLLoader fx = new FXMLLoader(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
        Parent root = fx.load();
        ControllerBookDetail controllerBookDetail = fx.getController();
        controllerBookDetail.saveForwardScene(image.getScene());
        controllerBookDetail.setOptionInBook(this.book);
        controllerBookDetail.showInformation(this.book);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void BookHboxEnter(MouseEvent event) {
        BookHbox.setStyle("-fx-background-color: white");
    }

    @FXML
    public void BookHboxExit(MouseEvent event) {
        BookHbox.setStyle("-fx-background-color: transparent");
    }

}
