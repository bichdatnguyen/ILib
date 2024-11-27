package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;
import org.example.ilib.Processor.AdminApp;
import org.example.ilib.Processor.Book;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ControllerMenu implements Initializable {
    @FXML
    public ImageView avatarUser;

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
    private MenuItem account;

    @FXML
    private MenuItem Cart;

    @FXML
    private MenuItem Admin;
    @FXML
    private MenuItem TransactionItem;


    private List<Book>recentlyBooks = new ArrayList<>();

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);// Tạo ExecutorService duy nhất

    public void accountSwitchScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) account.getParentPopup().getOwnerWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Account.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        shutdownExecutorService();
        stage.setScene(scene);
    }

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

    private void loadProperties() {
        String query = "SELECT avatarPath FROM user WHERE email = ? and password = ?";
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, Account.getInstance().getPassword());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Account.getInstance().setAvatarPath(resultSet.getString("avatarPath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    executorService = Executors.newFixedThreadPool(8);
                    System.out.println("ExecutorService restart");
                }

                executorService.submit(() -> {
                    try {
                        GoogleBooksAPI api = new GoogleBooksAPI();
                        JsonArray bookDetails = api.getInformation(searchText, 5);

                        if (bookDetails != null && !bookDetails.isEmpty()) {
                            Platform.runLater(() -> {
                                try {
                                    Stage stage = (Stage) search.getScene().getWindow();
                                    FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/org/example/ilib/SearchingBook.fxml"));
                                    Parent root = fxmlLoader1.load();
                                    ControllerSearchingBook controllerSearchingBook = fxmlLoader1.getController();
                                    controllerSearchingBook.setLabel(searchText);

                                    for (JsonElement book : bookDetails) {
                                        JsonObject item = book.getAsJsonObject();
                                        String id = item.get("id").getAsString();

                                        Book bk = api.getBooksByID(id);
                                        controllerSearchingBook.addBook(bk);
                                    }
                                    controllerSearchingBook.showSearchResult(1);
                                    controllerSearchingBook.showNumberOfPages((bookDetails.size() - 1) / 4 + 1);
                                    Scene scene1 = new Scene(root);
                                    stage.setScene(scene1);

                                } catch (IOException e) {
                                    showErrAndEx.showAlert("Lỗi khi tải giao diện tìm kiếm.");
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
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
        try{
            AdminApp.getInstance().adminChecking();
            loadProperties();
            if (Account.getInstance().getAvatarPath() != null) {
                avatarUser.setImage(new Image(Account.getInstance().getAvatarPath()));
            }
            DBConnection db = DBConnection.getInstance();
            recentlyBooks = db.getRecentlyBooks(9);
            try {
                for (int i = 0; i < recentlyBooks.size(); i++) {
                    FXMLLoader fx = new FXMLLoader();
                    fx.setLocation(getClass().getResource("/org/example/ilib/book.fxml"));
                    HBox cardbox = (HBox) fx.load();
                    ControllerBook controllerBook = (ControllerBook) fx.getController();
                    controllerBook.setBook(recentlyBooks.get(i));
                    controllerBook.showBook(recentlyBooks.get(i));
                    recentlyAddHbox.getChildren().add(cardbox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void gotoTransactionHistory(ActionEvent event) {
        try {
            Stage stage = (Stage) TransactionItem.getParentPopup().getOwnerWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/TransactionHistory.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}