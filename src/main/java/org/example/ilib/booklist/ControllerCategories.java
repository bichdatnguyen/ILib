package org.example.ilib.booklist;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.book.ControllerBook;
import org.example.ilib.book.Book;
import org.example.ilib.controller.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ControllerCategories implements Initializable {
    @FXML
    private Button Back;
    @FXML
    private GridPane gridPaneCategory;
    @FXML
    private ComboBox<String> categoryChoice;
    @FXML
    private HBox categoryPage;
    private static List<String> subjects;
    private static Map<String, List<Book>> bookByCategory;

    /** subjects setter.
     * @param subjects set to ControllerCategorise.subjects
     */
    public static void setSubjects(List<String> subjects) {
        ControllerCategories.subjects = subjects;
    }

    /** contructor.
     * @throws SQLException prevent sql exception
     */
    public ControllerCategories() throws SQLException {
        gridPaneCategory = new GridPane();
        DBConnection db = DBConnection.getInstance();
        setSubjects(db.getAllSubjectFromDB());
        bookByCategory = new HashMap<>();
    }

    /** back to menu.
     * @param event back to menu when clicked
     * @throws IOException prevent IO exception
     */
    public void BackToMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
        bookByCategory.clear();
    }

    /** create page's buttons.
     * @param page page's number
     * @param category page's category
     * @return
     */
    public Button createPageButton(String page, String category) {
        Button button = new Button(page);
        button.setOnMouseClicked(_ -> {
            try {
                showResult(Integer.parseInt(page), category);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }

    /** show all pages.
     * @param pages number of pages.
     * @param category page's category
     */
    public void showNumberOfPages(int pages, String category) {
        categoryPage.getChildren().clear();
        for (int i = 1; i <= pages; i++) {
            Button button = createPageButton(String.valueOf(i), category);
            categoryPage.getChildren().add(button);
        }
    }

    /** get book by category from database.
     * @param category book's category
     * @throws SQLException prevent sql exception
     * @throws IOException prevent IO exception
     */
    public void setBooks(String category) throws SQLException, IOException {
        DBConnection db = DBConnection.getInstance();
        bookByCategory.put(category, db.getTopCategories(category));
    }

    /** show books.
     * @param page books are show in this page
     * @param category book's category
     * @throws SQLException prevent sql exception
     */
    public void showResult(int page, String category) throws SQLException {
        if (gridPaneCategory != null) {
            gridPaneCategory.getChildren().clear();
        }

        try {
            int column = 0;
            int row = 0;

            List<Book> books = bookByCategory.get(category);
            for (int i = 8 * page - 8; i < Math.min(8 * page, books.size()); i++) {
                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(books.get(i));
                controllerBook.showBook(books.get(i));

                if(column == 4) {
                    column = 0;
                    row++;
                }
                gridPaneCategory.add(cardbox, column++, row);
                showNumberOfPages((books.size() - 1) / 8 + 1, category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** categories initialize.
     * @param location location
     * @param resources resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        //categoryChoice.setValue(subjects.get(6)); // set philosophy books default
        if (categoryChoice != null) {
            categoryChoice.getItems().addAll(subjects);
            categoryChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        if (!bookByCategory.containsKey(newValue)) {
                            setBooks(newValue);
                        }
                        showResult(1, newValue);
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (gridPaneCategory != null) {
            gridPaneCategory.getChildren().clear();
        }
    }
}
