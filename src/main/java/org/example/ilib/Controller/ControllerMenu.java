package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Book;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);// Tạo ExecutorService duy nhất





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
        shutdownExecutorService();
        System.out.println(searchText);
    }

    @FXML
    void MoveToCart(ActionEvent event)throws IOException{

        Stage stage = (Stage) Cart.getParentPopup().getOwnerWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Cart.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        shutdownExecutorService();
        stage.setScene(scene);
    }

    public void handleSearch(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String searchText = search.getText().trim(); // Lấy nội dung từ trường tìm kiếm
            if (!searchText.isEmpty()) {
                if (executorService.isShutdown()) {
                    // Nếu đã tắt, khởi tạo lại ExecutorService
                    executorService = Executors.newFixedThreadPool(4);
                    System.out.println("ExecutorService restart");
                }

                executorService.submit(() -> {
                    try {
                        GoogleBooksAPI api = new GoogleBooksAPI();
                        JsonArray bookDetails = api.getInformation(searchText, 4);

                        if (bookDetails != null && !bookDetails.isEmpty()) {
                            Platform.runLater(() -> {
                                try {
                                    Stage stage = (Stage) search.getScene().getWindow();
                                    FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/org/example/ilib/SearchingBook.fxml"));
                                    Parent root = fxmlLoader1.load();
                                    ControllerSearchingBook controllerSearchingBook = fxmlLoader1.getController();
                                    controllerSearchingBook.setLabel(searchText);

                                    for (int i = 0; i < bookDetails.size(); i++) {
                                        JsonObject item = bookDetails.get(i).getAsJsonObject();
                                        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");
                                        String title, author, thumbnail;

                                        if (volumeInfo.has("imageLinks")) {
                                            thumbnail = volumeInfo.getAsJsonObject("imageLinks").get("smallThumbnail").getAsString();
                                        } else {
                                            thumbnail = "/org/assets/noImage.png";
                                        }

                                        if (volumeInfo.has("authors")) {
                                            JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
                                            StringBuilder authorsBuilder = new StringBuilder();
                                            for (int j = 0; j < authorsArray.size(); j++) {
                                                authorsBuilder.append(authorsArray.get(j).getAsString());
                                                if (j < authorsArray.size() - 1) {
                                                    authorsBuilder.append(", ");
                                                }
                                            }
                                            author = authorsBuilder.toString();
                                        } else {
                                            author = "No authors";
                                        }

                                        if (volumeInfo.has("title")) {
                                            title = volumeInfo.get("title").getAsString();
                                        } else {
                                            title = "No title available.";
                                        }

                                        Book bk = new Book(title, thumbnail, author);
                                        controllerSearchingBook.addBook(bk);
                                    }
                                    controllerSearchingBook.show();
                                    Scene scene1 = new Scene(root);
                                    stage.setScene(scene1);

                                } catch (IOException e) {
                                    showErrAndEx.showAlert("Lỗi khi tải giao diện tìm kiếm.");
                                }
                            });
                        } else {
                            Platform.runLater(() -> showErrAndEx.showAlert("Không tìm thấy sách phù hợp"));
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> showErrAndEx.showAlert("Đã xảy ra lỗi trong khi truy vấn API."));
                    }
                });
            }
        }
    }

    public void topBookMenu(MouseEvent event) throws IOException {

        Stage stage = (Stage) topBooks.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/TopBooks.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        shutdownExecutorService();
        stage.setScene(scene);
    }

    public void categoriesMenu(MouseEvent e) throws IOException {

        Stage stage = (Stage) Categories.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Categories.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        shutdownExecutorService();
        stage.setScene(scene);
    }

    public void readingMenu(MouseEvent e) throws IOException {

        Stage stage = (Stage) reading.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Reading.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        shutdownExecutorService();
        stage.setScene(scene);
    }

    // Giải phóng tài nguyên (shutdown)
    public static void shutdownExecutorService() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            System.out.println("ExecutorService đã được tắt.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Booklist bl = new Booklist(Booklist.RECECNTLYADDED_BOOK);
        recentlyBooks = bl.bookList;
        try {

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
