package org.example.ilib.Menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Book.ControllerBook;
import org.example.ilib.Book.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerSearchingBook  {

    @FXML
    private Button Back;
    @FXML
    private GridPane gridPaneBook;
    @FXML
    private Label searchLabel;
    @FXML
    private HBox pageBox;

    private List<Book> booksearching = new ArrayList();

    /** set searchLabel.
     * @param label set text to searchLabel
     */
    public void setLabel(String label){
        searchLabel.setText(label);
    }

    /** add all finding books.
     * @param book booked which is found
     */
    public void addBook(Book book) {
       booksearching.add(book);
    }

    /** back to menu
     * @param event go back to menu when button is clicked.
     * @throws IOException prevent IO exception
     */
    @FXML
   public void BackToMenu(MouseEvent event) throws IOException {
            Stage stage = (Stage) Back.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
    }

    /** create one page's button.
     * @param page page's number
     * @return create button
     */
    public Button createPageButton(String page) {
        Button button = new Button(page);
        button.setOnMouseClicked(event -> {
            showSearchResult(Integer.parseInt(page));
        });
        return button;
    }

    /** create all buttons.
     * @param pages the number of pages.
     */
    public void showNumberOfPages(int pages) {
        pageBox.getChildren().clear();
        for (int i = 1; i <= pages; i++) {
            Button button = createPageButton(String.valueOf(i));
            pageBox.getChildren().add(button);
        }
    }

    /** show all searching result.
     * @param page page which is shown.
     */
   public void showSearchResult(int page){
        gridPaneBook.getChildren().clear();
        try {
            int column = 0;
            int row = 0;

           for (int i = 8 * page - 8; i < Math.min(8 * page, booksearching.size()); i++) {
               Book book = booksearching.get(i);
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/book.fxml"));

               HBox hbox = (HBox) fxmlLoader.load();
               ControllerBook controllerBook = (ControllerBook) fxmlLoader.getController();
               controllerBook.setBook(book);
               controllerBook.showBook(book);
               if(column == 4) {
                   column = 0;
                   row++;
               }
               gridPaneBook.add(hbox, column++, row);
           }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
