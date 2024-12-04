package org.example.ilib.book.BookDetail;

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
import org.example.ilib.account.Account;
import org.example.ilib.book.Book;
import org.example.ilib.booklist.Booklist;
import org.example.ilib.controller.DBConnection;
import org.example.ilib.controller.showErrAndEx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerBookDetail {
    @FXML
    public Button returnBookButton;

    @FXML
    private Button BackButton;

    @FXML
    private ToggleGroup Group;

    @FXML
    private TextField VolumeTextField;

    @FXML
    private Button addCartButton;

    @FXML
    private Label authorText;

    @FXML
    private Label descriptionText;

    @FXML
    private Text idText;

    @FXML
    private Button judgeButton;

    @FXML
    private Text quantityText;

    @FXML
    private ImageView thumbnail;

    @FXML
    private Label titleText;

    @FXML
    private HBox optionInBook;

    private static final int Buy = 2;
    private static final int Borrow = 1;
    private static int status = 0;
    private String email = Account.getInstance().getEmail();

    private Scene Forwardsceen;


    /** set the scene before.
     * @param scene set to this.scene
     */
    public void saveForwardScene(Scene scene) {
        this.Forwardsceen = scene;
    }
    private Image loadImage(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return new Image(path, true);
        } else {
            return new Image(getClass().getResourceAsStream(path));
        }
    }

    /** show all book's information.
     * @param book book needed to show information
     */
    public void showInformation(Book book) {
        thumbnail.setImage(loadImage(book.getImage()));
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        String description = book.getDescription();
        if (description == null || description.length() < 300) {
            descriptionText.setText(description);
        } else {
            descriptionText.setText(book.getDescription().substring(0, 299));
        }
        idText.setText(book.getId());
        quantityText.setText(String.valueOf(book.getQuantity()));
        if (checkBookBorrow()) {
            returnBookButton.setVisible(true);
        }
    }

    /** back to scene before.
     * @param event when button clicked go back
     * @throws IOException prevent IO exception
     */
    public void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage) BackButton.getScene().getWindow();
        Scene scene = Forwardsceen;
        stage.setScene(scene);
    }

    /** add book to cart by sql.
     * @param email book's email
     * @param bookId book's id
     * @param quantity book's quantity
     * @param status borrow or buy
     * @throws SQLException prevent sql exception
     */
    public void addBookToCart(String email, String bookId, int quantity, int status) throws SQLException {

        System.out.println(email);
        String queryCheck = "SELECT quantity, type FROM cart WHERE email = ? AND bookId = ?";
        String queryInsert = "INSERT INTO cart (bookID, email, date, quantity,type) VALUES (?, ?, CURRENT_DATE, ?,?)";

        try (Connection connection = DBConnection.getInstance().getConnection()) {
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

    /** add book to cart.
     * @param event add book to cart when button clicked
     */
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
            int quantityInStock = Integer.parseInt(quantityText.getText().trim());
          if(quantityInStock == 0){
              showErrAndEx.showAlert("Sách hiện tại chưa có tại thư viện");
              return;
          }
         if( quantity > quantityInStock){
             showErrAndEx.showAlert("Vượt quá số lượng có sẵn");
             return;
         }
         if(quantity > 5 && status == Buy){
             showErrAndEx.showAlert("Bạn chỉ có thể  mua 5 quyển 1 lúc");
             return;
         }
         if(quantity > 1 && status == Borrow){
             showErrAndEx.showAlert("Bạn chỉ có thể mượn 1 quyển");
             return;
         }


            if(quantity > 0){
              //int bookId = Integer.parseInt(Bookid.getText());
              String bookId = idText.getText();
              try{
                  System.out.println(quantity);
                  if(status == Borrow && checkBookBorrow()){
                      showErrAndEx.showAlert("Bạn đã mượn sách này");
                      return;
                  }
                  if(checkBookInCart()){
                      showErrAndEx.showAlert("Sách đã có trong giỏ hàng");
                      return;
                  }
                  addBookToCart(email,bookId,quantity,status);
                  showErrAndEx.showAlert("Đã thêm sách vào giỏ hàng");
              } catch (SQLException e) {
                  e.printStackTrace();
              }

            }
    }

    /** choose to borrow.
     * @param event click borrow option
     */
    @FXML
    void BorrowClick(MouseEvent event) {
            status = Borrow;
    }

    /** choose to buy.
     * @param event click buy option
     */
    @FXML
    void PurchaseClick(MouseEvent event) {
        status = Buy;
    }

    /** comment book, save or delete book in shelf.
     * @param book set option in this book
     * @throws SQLException
     */
    public void setOptionInBook(Book book) throws SQLException {
        if (book.getQuantity() == 0) {
            return;
        } else {
            DBConnection db = DBConnection.getInstance();//try with

            Button moveToCmt = new Button("Comment");
            Button saveToShelf = new Button("Save");
            Button deleteBookInShelf = new Button("Delete");
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

    public boolean checkBookBorrow(){
        String query = "select count(*) from borrow where Email = ? and bookID = ?";
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query) ){
               stmt.setString(1, email);
               stmt.setString(2, idText.getText());
                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()){
                        int count = rs.getInt(1);
                        if(count == 0){
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkBookInCart(){
        String query = "select count(*) from cart where bookID = ? and type =?";
        try(Connection connection =DBConnection.getInstance().getConnection() ; PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, idText.getText());
            stmt.setString(2, status == Borrow ? "BORROW" : "BUY");
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    int count = rs.getInt(1);
                    if(count > 0){
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * delete book from borrow table in database.
     */
    private void deleteBookFromBorrow() {
        String query = "delete from borrow where email = ? and bookID = ?";
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, idText.getText());
            int rs = stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * upadte quantityInStock field of books.
     */
    private void updateBookQuantityInStock() {
        String query = "update books set quantityinstock = quantityinstock + 1 where  bookID = ?";
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, idText.getText());
            int rs = stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * a method linked with fxml file.
     * @param mouseEvent mouse clicked
     * @throws IOException io
     */
    public void returnBookToTheLibrary(MouseEvent mouseEvent) throws IOException {
        deleteBookFromBorrow();
        int oldQuantity = Integer.parseInt(quantityText.getText());
        Booklist.getInstance().updateBookQuantity(idText.getText(), oldQuantity+1);
        updateBookQuantityInStock();

        //Alert
        Alert alert = showErrAndEx.showAlert("return book to the library successfully!");
        if(alert.getResult() == ButtonType.OK){
            try{
                //returnBookButton.setVisible(false);
                Stage stage = (Stage) returnBookButton.getScene().getWindow();
                FXMLLoader fx = new FXMLLoader();
                fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
                Scene scene = new Scene(fx.load());
                stage.setScene(scene);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}