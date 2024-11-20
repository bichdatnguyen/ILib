package org.example.ilib.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.ilib.Controller.ControllerBookDetail.bookDetails;
//import static org.example.ilib.Controller.ControllerBookDetail.getBookDetails;

public class ControllerMenu implements Initializable {
    @FXML
    private MenuItem signOut;

    @FXML
    private TextField search;

    @FXML
    private Label topBooks;

    @FXML
    private Label Categories;

    @FXML
    private Label reading;

    @FXML
    private HBox recentlyAddHbox;

    @FXML
    private MenuItem Cart;
    private List<Book>recentlyBooks = new ArrayList<>();



    /**
     * signOutMenu handle MouseEvent event.
     * this method will switch the scene to the login and register screen.
     *
     * @param actionEvent belong to MouseEvent type
     * @throws IOException in case that FXML can not be used
     */
    public void signOutMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) signOut.getParentPopup().getOwnerWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/LoginAndRegister.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        String searchText = search.getText();
        System.out.println(searchText);
    }

    @FXML
    void MoveToCart(ActionEvent event)throws IOException{
        Stage stage = (Stage) Cart.getParentPopup().getOwnerWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Cart.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void handleSearch(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) { // Kiểm tra mã phím ENTER
            String searchText = search.getText().trim(); // Lấy nội dung từ trường tìm kiếm
            if (!searchText.isEmpty()) { // Đảm bảo không để trống
                GoogleBooksAPI api = new GoogleBooksAPI();
                bookDetails = api.getInformation(searchText);
                if (bookDetails != null && !bookDetails.isEmpty()) { // Kiểm tra thông tin trả về từ API
                    // Lấy stage hiện tại


                    // Load giao diện chi tiết sách
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
                    Parent root = loader.load();

                    ControllerBookDetail controllerBookDetail = loader.getController();
                    controllerBookDetail.setInformation(searchText);

                    Stage stage = (Stage) search.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.err.println("Không tìm thấy thông tin sách từ API.");
                }
            } else {
                System.err.println("Hãy nhập nội dung tìm kiếm.");
            }
        }
    }

    public void topBookMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) topBooks.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/TopBooks.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void categoriesMenu(MouseEvent e) throws IOException {
        Stage stage = (Stage) Categories.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Categories.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void readingMenu(MouseEvent e) throws IOException {
        Stage stage = (Stage) reading.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Reading.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Book_list bl = new Book_list(Book_list.RECECNTLYADDED_BOOK);
        recentlyBooks = bl.bookList;
        try {
            System.out.println(recentlyBooks.size() );
            for (int i = 0; i < recentlyBooks.size() ; i++ ) {

                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                HBox cardbox = (HBox) fx.load();
                ControllerBook controllerBook = (ControllerBook) fx.getController();
                controllerBook.setBook(recentlyBooks.get(i));
                recentlyAddHbox.getChildren().add(cardbox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
