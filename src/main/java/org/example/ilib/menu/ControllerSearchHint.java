package org.example.ilib.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.book.Book;
import org.example.ilib.book.BookDetail.ControllerBookDetail;

import java.io.IOException;
import java.sql.SQLException;


public class ControllerSearchHint  {
    @FXML
    private Label bookTitle;
    @FXML
    private HBox SearchHintHbox;

    private Book book;

    /** book setter.
     * @param book set to this.book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /** book getter.
     * @return this.book
     */
    public Book getBook() {
        return this.book;
    }

    /** show book's information.
     * @param book book's information
     */
    public void showBook(Book book) {
        bookTitle.setText(book.getTitle());
    }

    /** go to book's detail.
     * @param event show book detail when click on book.
     * @throws IOException prevent IO exception
     * @throws SQLException prevent SQL exception
     */
    @FXML
    public void gotoBookDetail(MouseEvent event) throws IOException, SQLException {
        Stage stage = (Stage) bookTitle.getScene().getWindow();

        FXMLLoader fx = new FXMLLoader(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
        Parent root = fx.load();
        ControllerBookDetail controllerBookDetail = fx.getController();
        controllerBookDetail.saveForwardScene(bookTitle.getScene());
        controllerBookDetail.setOptionInBook(this.book);
        controllerBookDetail.showInformation(this.book);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    public void setStyleWhite(){
        SearchHintHbox.setStyle("-fx-background-color: white;");
    }

    @FXML
    public void SearchHintHboxEnter(MouseEvent event) {
        SearchHintHbox.setStyle("-fx-background-color: grey;");
    }
    @FXML
    public void SearchHintHboxExit(MouseEvent event) {
        SearchHintHbox.setStyle("-fx-background-color: white;");
    }

}
