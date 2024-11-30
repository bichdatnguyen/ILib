package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;
import org.example.ilib.Processor.Book;
import org.example.ilib.Processor.Comment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerBookDetail {

    @FXML
    private Button BackButton;

    @FXML
    private ToggleGroup Group;

    @FXML
    private TextField VolumeTextField;

    @FXML
    private Button addCartButton;

    @FXML
    private Text authorText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text idText;

    @FXML
    private Button judgeButton;

    @FXML
    private Text quantityText;

    @FXML
    private ImageView thumbnail;

    @FXML
    private Text titleText;

    @FXML
    private HBox optionInBook;

    private static final int Buy = 2;
    private static final int Borrow = 1;
    private static int status = 0;
    private String email = Account.getInstance().getEmail();

    private Scene Forwardsceen;

    public void saveForwardScene(Scene scene) {
        this.Forwardsceen = scene;
    }

    public void showInformation(Book book) {
        thumbnail.setImage(new Image(book.getImage()));
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        String description = book.getDescription();
        if (description.length() < 250) {
            descriptionText.setText(description);
        } else {
            descriptionText.setText(book.getDescription().substring(0, 250));
        }
        idText.setText(book.getId());
        quantityText.setText(String.valueOf(book.getQuantity()));
    }

    public void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();

        Scene scene = Forwardsceen;
        stage.setScene(scene);
    }

    public void addBookToCart(String email, String bookId, int quantity, int status) throws SQLException {

        System.out.println(email);
        String queryCheck = "SELECT quantity, type FROM cart WHERE email = ? AND bookId = ?";
        String queryInsert = "INSERT INTO cart (bookID, email, date, quantity,type) VALUES (?, ?, CURRENT_DATE, ?,?)";

        try (Connection connection =DBConnection.getInstance().getConnection()) {
            // Kiểm tra nếu sách đã có trong giỏ hàng
            try (PreparedStatement stmtCheck = connection.prepareStatement(queryCheck);
               ) {
                stmtCheck.setString(1, bookId);
                stmtCheck.setString(2, email);

                String statusBook = (status == Borrow) ? "BORROW" :"BUY";
                try(  ResultSet rs = stmtCheck.executeQuery()){
                    if (rs.next()) {
                        String currentStatus = rs.getString("type");
                        // Kiểm tra nếu trạng thái khác, có thể xử lý logic tùy ý

                        // Nếu sách đã có, cập nhật số lượng và trạng thái
                        if(currentStatus.equals(statusBook)) {
                            showErrAndEx.showAlert("Sách đã được thêm vào giỏ hàng");
                        } else {
                            try (PreparedStatement stmtInsert = connection.prepareStatement(queryInsert)) {
                                stmtInsert.setString(1, bookId);
                                stmtInsert.setString(2, email);

                                stmtInsert.setInt(3, quantity);
                                stmtInsert.setString(4, statusBook);
                                stmtInsert.executeUpdate();
                            }
                        }
                    } else {
                        // Nếu sách chưa có, thêm mới
                        try (PreparedStatement stmtInsert = connection.prepareStatement(queryInsert)) {
                            stmtInsert.setString(1, bookId);
                            stmtInsert.setString(2, email);
                            stmtInsert.setInt(3, quantity);
                            stmtInsert.setString(4, statusBook);
                            stmtInsert.executeUpdate();
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addCart(MouseEvent event) {
            String num = VolumeTextField.getText().trim();
            if (num.isEmpty()) {
                showErrAndEx.showAlert("Vui lòng nhập số lượng!");
                return;
            }
            if(status == 0) {
                showErrAndEx.showAlert("Vui lòng kiểm tra trang thái");
                return;
            }
            int quantity = Integer.parseInt(VolumeTextField.getText());
            int volBook = Integer.parseInt(quantityText.getText().trim());

         if( quantity > volBook){
             showErrAndEx.showAlert("Vượt quá số lượng có sẵn");
             return;
         }
            if(quantity > 0){
              //int bookId = Integer.parseInt(Bookid.getText());
              String bookId = idText.getText();
              try{
                  System.out.println(quantity);
                  addBookToCart(email,bookId,quantity,status);
                  showErrAndEx.showAlert("Đã thêm sách vào giỏ hàng");
              } catch (SQLException e) {
                  e.printStackTrace();
              }

            }
    }
    @FXML
    void BorrowClick(MouseEvent event) {
            status = Borrow;
    }

    @FXML
    void PurchaseClick(MouseEvent event) {
        status = Buy;
    }

    public void setOptionInBook(Book book) throws SQLException {
        if (book.getQuantity() == 0) {
            return;
        } else {
            DBConnection db = DBConnection.getInstance();//try with

            Button moveToCmt = new Button("Bình luận");
            Button saveToShelf = new Button("Lưu");
            Button deleteBookInShelf = new Button("Xóa");
            optionInBook.getChildren().addAll(moveToCmt, saveToShelf, deleteBookInShelf);

            moveToCmt.setOnMouseClicked(_ -> {
                Stage stage = (Stage) moveToCmt.getScene().getWindow();

                FXMLLoader fx = new FXMLLoader(getClass().getResource("/org/example/ilib/BookComment.fxml"));
                Parent root = null;
                try {
                    root = fx.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ControllerBookComment controllerBkCmt = fx.getController();
                controllerBkCmt.setBookID(book.getId());
                controllerBkCmt.saveFowardScene(moveToCmt.getScene());
                controllerBkCmt.showAllCmtInBook();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            saveToShelf.setOnMouseClicked(_ -> {
                try {
                    if (!db.existInShelf(book.getId())) {
                        db.saveBookToShelf(book.getId());
                        alert.setContentText("Sách lưu thành công!");
                    } else {
                        alert.setContentText("Sách đã được lưu");
                    }
                    alert.show();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            deleteBookInShelf.setOnMouseClicked(_ -> {
                try {
                    if (db.existInShelf(book.getId())) {
                        db.deleteBookFromShelf(book.getId());
                        alert.setContentText("Xóa thành công");
                    } else {
                        alert.setContentText("Sách không tồn tại trong giá");
                    }
                    alert.show();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}