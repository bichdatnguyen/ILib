package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

public class ControllerCategories extends Booklist implements Initializable {
    @FXML
    private Button Back;
    @FXML
    private GridPane gridPaneCategory;
    @FXML
    private ChoiceBox<String> categoryChoice;
    @FXML
    private HBox categoryPage;

    private static List<String> subjects;

    public static void setSubjects(List<String> subjects) {
        ControllerCategories.subjects = subjects;
    }

    public ControllerCategories() throws SQLException, IOException {
        super(Booklist.CATEGORIES_BOOK);
        gridPaneCategory = new GridPane();
        DBConnection db = DBConnection.getInstance();
        setSubjects(db.getAllSubjectFromDB());
    }

    public void BackToMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
    }

    public Button createPageButton(String page) {
        Button button = new Button(page);
        return button;
    }

    public void showNumberOfPages(int pages) {
        categoryPage.getChildren().clear();
        for (int i = 1; i <= pages; i++) {
            Button button = createPageButton(String.valueOf(i));
            categoryPage.getChildren().add(button);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {

        if (categoryChoice != null) {
            categoryChoice.getItems().addAll(subjects);
        }

        if (gridPaneCategory != null) {
            gridPaneCategory.getChildren().clear();
        }

        try {
            int column = 0;
            int row = 0;

            for (int i = 0; i < Math.min(bookList.size(), 4); i++) {
                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(bookList.get(i));

                if(column == 2) {
                    column = 0;
                    row++;
                }
                gridPaneCategory.add(cardbox, column++, row);
                showNumberOfPages((bookList.size() - 1) / 4 + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
