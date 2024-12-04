package org.example.ilib.menu;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ilib.account.Account;
import org.example.ilib.account.AdminApp;
import org.example.ilib.book.Book;
import org.example.ilib.book.ControllerBook;
import org.example.ilib.booklist.Booklist;
import org.example.ilib.controller.DBConnection;
import org.example.ilib.controller.GoogleBooksAPI;
import org.example.ilib.controller.showErrAndEx;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ControllerMenu implements Initializable {
    @FXML
    public ImageView avatarUser;

    @FXML
    public MenuButton UserButton;

    @FXML
    public MenuItem updateDB;

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
    private HBox recommendHBox;

    @FXML
    private MenuItem account;

    @FXML
    private MenuItem Cart;

    @FXML
    private MenuItem Admin;

    @FXML
    private MenuItem TransactionItem;
    @FXML
    private HBox TopBookHbox;
    @FXML
    private HBox CategoriesHbox;
    @FXML
    private HBox ReadingHbox;
    @FXML
    private VBox hintVbox;
    @FXML
    private Button chatBot;
    @FXML
    private Button clickButton;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private AnchorPane anchorPaneLoad;

    @FXML
    private ImageView music;

    private MediaPlayer mediaPlayer;

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
        //change user abilities
        Account.getInstance().setRole(null);

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
        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)
        ) {

            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, Account.getInstance().getPassword());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Account.getInstance().setAvatarPath(resultSet.getString("avatarPath"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void MoveToCart(ActionEvent event) throws IOException {
        Stage stage = (Stage) Cart.getParentPopup().getOwnerWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Cart.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        shutdownExecutorService();
        stage.setScene(scene);
    }

    /**
     * show hints when search for books.
     */
    public void showHints() {

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                hintVbox.getChildren().clear();
            } else {
                if (executorService.isShutdown()) {
                    executorService = Executors.newFixedThreadPool(2);
                    System.out.println("ExecutorService restart");
                }
                executorService.submit(() -> {
                    try {
                        List<Book> bookHints = DBConnection.getInstance().allHints(newValue);
                        Platform.runLater(() -> {

                            hintVbox.getChildren().clear();
                            for (Book bookHint : bookHints) {
                                FXMLLoader fx = new FXMLLoader();
                                fx.setLocation(getClass().getResource("/org/example/ilib/SearchHint.fxml"));
                                try {
                                    HBox hint = fx.load();
                                    ControllerSearchHint controllerSearchHint = fx.getController();
                                    controllerSearchHint.setStyleWhite();
                                    controllerSearchHint.setBook(bookHint);
                                    controllerSearchHint.showBook(bookHint);
                                    hintVbox.getChildren().add(hint);
                                    hintVbox.setVisible(hintVbox.getChildren().size() != 0);
                                    //   hintVbox.setVisible(true);
                                } catch (IOException e) {
                                    showErrAndEx.showAlert("Lỗi khi tải gợi ý tìm kiếm.");
                                }
                            }
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showErrAndEx.showAlert("Lỗi khi truy vấn cơ sở dữ liệu để lấy gợi ý.");
                    }
                });
            }
        });
    }

    /**
     * handle search bar.
     *
     * @param keyEvent find books when key pressed
     */
    public void handleSearch(KeyEvent keyEvent) {
        showHints();

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Search();
        }
    }

    /**
     * search books using Google Book API.
     */
    public void Search() {
        String searchText = search.getText().trim(); // Lấy nội dung từ trường tìm kiếm
        if (!searchText.isEmpty()) {
            if (executorService.isShutdown()) {
                // Nếu đã tắt, khởi tạo lại ExecutorService
                executorService = Executors.newFixedThreadPool(2);
                System.out.println("ExecutorService restart");
            }
            Loading(true);
            executorService.submit(() -> {
                try {
                    GoogleBooksAPI api = new GoogleBooksAPI();
                    JsonArray bookDetails = api.getInformation(searchText, 30);

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
                                controllerSearchingBook.showNumberOfPages((bookDetails.size() - 1) / 8 + 1);
                                Scene scene1 = new Scene(root);
                                stage.setScene(scene1);
                                Loading(false);

                            } catch (IOException e) {
                                showErrAndEx.showAlert("Lỗi khi tải giao diện tìm kiếm.");
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {

                        Platform.runLater(() -> {
                            Loading(false);
                            showErrAndEx.showAlert("Không tìm thấy sách phù hợp");
                        });
                    }
                } catch (Exception e) {

                    Platform.runLater(() -> {
                        Loading(false);
                        showErrAndEx.showAlert("Đã xảy ra lỗi trong khi truy vấn API.");
                    });
                }
            });
        }
    }

    /**
     * top book menu.
     *
     * @param event go to top book when clicked
     * @throws IOException prevent IO exception
     */
    public void topBookMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) topBooks.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/TopBooks.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        shutdownExecutorService();
        stage.setScene(scene);
    }

    /**
     * categories menu.
     *
     * @param e go to categorize menu when clicked
     * @throws IOException prevent IO exception
     */
    public void categoriesMenu(MouseEvent e) throws IOException {
        Stage stage = (Stage) Categories.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/ilib/Categories.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        shutdownExecutorService();
        stage.setScene(scene);
    }

    /**
     * reading menu.
     *
     * @param e go to reading shelf when clicked
     * @throws IOException prevent IO exception
     */
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

    /**
     * music initialize.
     */
    public void initMusic() {
        try {
            String url = getClass().getResource("/org/music/backgroundMusic.mp3").toExternalForm();
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            music.setImage(new Image(getClass().getResource("/org/assets/mute.png").toExternalForm()));
        } catch (NullPointerException e) {
            System.out.println("File music not found");
        } catch (MediaException e) {
            System.out.println("Can't play media");
        }
    }

    /**
     * turn music.
     *
     * @param event turn on and pause music when clicked
     */
    @FXML
    void turnMusic(MouseEvent event) {
        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
            music.setImage(new Image(getClass().getResource("/org/assets/musicOn.png").toExternalForm()));
        } else {
            mediaPlayer.pause();
            music.setImage(new Image(getClass().getResource("/org/assets/mute.png").toExternalForm()));
        }
    }

    /**
     * menu initialize.
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initMusic();
            showHints();

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/org/assets/robot-chatbot-8201.png")));
            imageView.setFitWidth(30); // Đặt chiều rộng
            imageView.setFitHeight(30); // Đặt chiều cao
            chatBot.setGraphic(imageView);
            chatBot.setStyle("-fx-background-color: transparent;");
            ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/org/assets/search-2907.png")));
            imageView1.setFitWidth(30);
            imageView1.setFitHeight(30);
            clickButton.setGraphic(imageView1);
            clickButton.setStyle("-fx-background-color: transparent;");
            TopBookHbox.setStyle("-fx-background-color: transparent");
            CategoriesHbox.setStyle("-fx-background-color: transparent");
            ReadingHbox.setStyle("-fx-background-color: transparent");
            if (Account.getInstance().getRole() == null) {
                AdminApp.getInstance().adminChecking();
                System.out.println("adminChecking happens");
            }
            if (Account.getInstance().getRole().equals("admin")) {
                UserButton.setText("Admin");
                Admin.setVisible(true);
            } else {
                UserButton.setText("User");
            }
            loadProperties();
            if (Account.getInstance().getAvatarPath() != null) {
                avatarUser.setImage(new Image(Account.getInstance().getAvatarPath()));
            }
            try {
                List<Book> recentlyBooks = Booklist.getInstance().getRecentlyBookList();
                for (int i = 0; i < recentlyBooks.size(); i++) {
                    FXMLLoader fx = new FXMLLoader();
                    fx.setLocation(getClass().getResource("/org/example/ilib/Book.fxml"));
                    HBox cardbox = fx.load();
                    ControllerBook controllerBook = fx.getController();
                    controllerBook.setBook(recentlyBooks.get(i));
                    controllerBook.showBook(recentlyBooks.get(i));
                    recentlyAddHbox.getChildren().add(cardbox);
                }

                List<Book> recommendBooks = Booklist.getInstance().getRecommendBookList();
                System.out.println(recommendBooks.size());
                for (int i = 0; i < recommendBooks.size(); i++) {
                    FXMLLoader fx = new FXMLLoader();
                    fx.setLocation(getClass().getResource("/org/example/ilib/Book.fxml"));
                    HBox cardbox = fx.load();
                    ControllerBook controllerBook = fx.getController();
                    controllerBook.setBook(recommendBooks.get(i));
                    controllerBook.showBook(recommendBooks.get(i));
                    recommendHBox.getChildren().add(cardbox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gotoAdvanceSetting(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) Admin.getParentPopup().getOwnerWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Admin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void TopBookHboxEnter(MouseEvent event) {
        TopBookHbox.setStyle("-fx-background-color: #F2F2F2");
    }

    @FXML
    public void TopBookHboxExit(MouseEvent event) {
        TopBookHbox.setStyle("-fx-background-color: transparent");
    }

    @FXML
    public void CategoriesHboxEnter(MouseEvent event) {
        CategoriesHbox.setStyle("-fx-background-color: #F2F2F2");
    }

    @FXML
    public void CategoriesHboxExit(MouseEvent event) {
        CategoriesHbox.setStyle("-fx-background-color: transparent");
    }

    @FXML
    public void ReadingHboxEnter(MouseEvent event) {
        ReadingHbox.setStyle("-fx-background-color: #F2F2F2");
    }

    @FXML
    public void ReadingHboxExit(MouseEvent event) {
        ReadingHbox.setStyle("-fx-background-color: transparent");
    }

    @FXML
    public void chatBotEnter(MouseEvent event) {
        chatBot.setStyle("-fx-background-color: #F2F2F2");
    }

    @FXML
    public void chatBotExit(MouseEvent event) {
        chatBot.setStyle("-fx-background-color: transparent");
    }

    /**
     * chatbot menu.
     *
     * @param event go to chatbot menu when button clicked
     */
    @FXML
    public void chatBotClick(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Chatbot.fxml"));
            Parent root = fxmlLoader.load();
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("ChatBot");
            Scene scene = new Scene(root);
            popup.setScene(scene);
            popup.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickButtonEnter(MouseEvent event) {
        clickButton.setStyle("-fx-background-color: green");
    }

    @FXML
    public void clickButtonExit(MouseEvent event) {
        clickButton.setStyle("-fx-background-color: transparent");
    }

    /**
     * search book by button.
     *
     * @param event search books when button is clicked
     */
    @FXML
    public void clickButtonClick(MouseEvent event) {
        Search();
    }

    private void Loading(boolean isLoading) {
        loadingIndicator.setVisible(isLoading);
        if (isLoading) {
            loadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        } else {
            loadingIndicator.setProgress(0); // Đặt lại nếu không còn loading
        }
        //anchorPaneLoad.setDisable(isLoading);
        //  anchorPaneLoad.setOpacity(isLoading ? 0.5 : 1);
    }
}