package org.example.ilib.Cart;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.ilib.Account.Account;
import org.example.ilib.BookList.Booklist;
import org.example.ilib.Controller.DBConnection;
import org.example.ilib.Controller.showErrAndEx;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class ControllerCartItemList implements Initializable {
    @FXML
    private Button add;

    @FXML
    private Button deleteBook;

    @FXML
    private ImageView QRCode;

    @FXML
    private Label totalMoney;

    @FXML
    private Button Back;

    @FXML
    private Button BuyQR;

    @FXML
    private Button substract;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<CartItem> CartTable;
    @FXML
    private TableColumn<CartItem, String> StatusCol;
    @FXML
    private TableColumn<CartItem, String> NameCol;
    @FXML
    private TableColumn<CartItem, String> VoucherCol;
    @FXML
    private TableColumn<CartItem, Integer> MoneyCol;
    @FXML
    private TableColumn<CartItem, Integer> VolumeCol;
    @FXML
    private TextField VoulumeText;
    @FXML
    private ProgressIndicator LoadingIndicator;
    private boolean isTotalCalculated = false;
    private boolean isBuyQR = false;

    private ObservableList<CartItem> CartList;
    private static final int ADDVOLUME = 1;
    private static final int SUBSTRACTVOLUME = 2;
    private static int totalMonet =0;
    private String email = Account.getInstance().getEmail();
    private Map<String,Integer > quantityBook = new HashMap<>() ;

    public void setCartList(List<CartItem> CartList) {
        // Chuyển List thành ObservableList
        this.CartList = FXCollections.observableArrayList(CartList);
    }

    @FXML
    void BackToMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
    }

    @FXML
    void deleteBook(MouseEvent event) {
        if(CartList.isEmpty()) {
            showErrAndEx.showAlert("Giỏ hàng hiện tại đang trống");
            return;
        }

        Alert alert = showErrAndEx.showAlert("Bạn có muốn xóa đi không");

        if (alert.getResult() == ButtonType.OK) {
            if(CartTable.getSelectionModel().getSelectedItem() != null) {
                CartItem cartItem = CartTable.getSelectionModel().getSelectedItem();
                try {
                    removeBookFromCart(email,cartItem.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CartList.remove(CartTable.getSelectionModel().getSelectedItem());
                isTotalCalculated = false;
            } else {
                showErrAndEx.showAlert("Bạn chưa chọn hàng cần xóa");
            }
        } else{
            CartTable.getSelectionModel().clearSelection();
        }

       // CartList.remove(CartTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void BuyQRclick(MouseEvent event) {
        if (!isTotalCalculated) {
            showErrAndEx.showAlert("Vui lòng nhấn tổng tiền để thanh toán");
        } else if (totalMonet == 0) {
            showErrAndEx.showAlert("Số tiền thanh toán không hợp lệ");
        } else {
            if (!isBuyQR) {
                Loading(true);
                isBuyQR = true;

                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String path = "The amount paid is: " + totalMonet + " USD \n Have a nice day!";
                            String qrImagePath = QRCodeAuto.taoQrCode(path);
                            savePayments(email, CartList.stream().toList());
                            updateQuantityInStock();
                            updateBorrowBook();
                            removeBookFromCart(email, "is not null");
                            Platform.runLater(() -> QRCode.setImage(new Image(qrImagePath)));
                            boolean sendIt = SendEmail.sendEmail(email, "Thanh toán thư viện", path);
                            if (!sendIt) {
                                throw new IOException("Chuyển email không thành công");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        Platform.runLater(() -> {
                            Loading(false);
                            showErrAndEx.showAlert("Thanh toán thành công!");
                        });
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        Platform.runLater(() -> {
                            Loading(false);
                            showErrAndEx.showAlert("Đã xảy ra lỗi!");
                        });
                    }
                };
                new Thread(task).start();
            } else {
                showErrAndEx.showAlert("Bạn đã thanh toán thành công");
            }
        }
    }


    @FXML
    void totalMoneyClick(MouseEvent event) {
        totalMonet = 0;
            for(int i = 0 ; i < CartList.size(); i++) {
                int vol = CartList.get(i).getVolume();
                int money = CartList.get(i).getMoney();
                totalMonet +=  money*vol;
            }
            isTotalCalculated = true;
            isBuyQR = false;
        totalMoney.setText(String.valueOf(totalMonet) + '$');
    }

    private void setVolume(int status){
        if (VoulumeText != null) {
            String vol = VoulumeText.getText().trim(); // Loại bỏ khoảng trắng thừa
            if (vol.isEmpty()) {
                showErrAndEx.showAlert("Vui lòng nhập số lượng!");
                return;
            }

            try {
                int volNum = Integer.parseInt(vol); // Kiểm tra và chuyển đổi chuỗi thành số nguyên
                System.out.println(volNum);
                if (volNum > 0) {
                    if (CartTable.getSelectionModel().getSelectedItem() != null) {
                        int currVol = CartTable.getSelectionModel().getSelectedItem().getVolume();
                        CartItem cartItem = CartTable.getSelectionModel().getSelectedItem();
                        int total = 0;
                        if(status == ADDVOLUME){
                             total = currVol  + volNum;
                            CartTable.getSelectionModel().getSelectedItem().setVolume(total);

                        } else if(status == SUBSTRACTVOLUME){
                            if(volNum >= currVol){
                                CartTable.getSelectionModel().getSelectedItem().setVolume(0);
                            } else {
                                total = currVol - volNum;
                                CartTable.getSelectionModel().getSelectedItem().setVolume(total);
                            }
                        }
                        try {
                            updateBookQuantityInCart(email,cartItem.getId(),total);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        CartTable.refresh();
                        System.out.println(CartTable.getSelectionModel().getSelectedItem().getVolume());
                    } else {
                        showErrAndEx.showAlert("Vui lòng chọn một mục trong bảng!");
                    }
                } else {
                    showErrAndEx.showAlert("Số lượng phải lớn hơn 0!");
                }
            } catch (NumberFormatException e) {
                showErrAndEx.showAlert("Vui lòng nhập một số hợp lệ!");
            }
        } else {
            showErrAndEx.showAlert("Vui lòng nhập số lượng!");
        }

    }
    @FXML
    void addVolume(MouseEvent event) {
        setVolume(ADDVOLUME);
        isTotalCalculated = false;
    }

    @FXML
    void substractVolume(MouseEvent event) {
        setVolume(SUBSTRACTVOLUME);
        isTotalCalculated = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       try{
           setCartList(getCartsByEmail(email));
           NameCol.setCellValueFactory(new PropertyValueFactory<CartItem,String>("name"));
           VolumeCol.setCellValueFactory(new PropertyValueFactory<CartItem, Integer>("volume"));
           MoneyCol.setCellValueFactory(new PropertyValueFactory<CartItem, Integer>("money"));
           StatusCol.setCellValueFactory(new PropertyValueFactory<CartItem, String>("status"));
           VoucherCol.setCellValueFactory(new PropertyValueFactory<CartItem, String>("voucher"));
           CartTable.setItems(CartList);
           anchorPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
               if (!CartTable.isHover() && !(deleteBook.isHover()||add.isHover()||substract.isHover()||VoulumeText.isHover())) { // Kiểm tra nếu chuột không nằm trong TableView
                   CartTable.getSelectionModel().clearSelection();
               }
           });

       } catch(Exception e){
           e.printStackTrace();
       }

    }



    public List<CartItem> getCartsByEmail(String email)  {
        // Câu lệnh SQL sử dụng INNER JOIN để lấy thông tin từ cả bảng Payment và Book
        String query = "SELECT cart.*, books.title, books.bookPrice " +
                "FROM cart " +
                "INNER JOIN books ON cart.bookID = books.bookID " +
                "WHERE cart.email = ?";
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection(); // Kết nối tới cơ sở dữ liệu
             PreparedStatement stmt = conn.prepareStatement(query);
           ) {

            // Set giá trị tham số cho câu lệnh (email)
            stmt.setString(1, email);
            // Xử lý kết quả trả về
            try(  ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()) {
                    // Lấy dữ liệu từ bảng Payment
                    String id = resultSet.getString("bookID");
                    int volume = resultSet.getInt("quantity");
                    String type = resultSet.getString("type");

                    // Lấy dữ liệu từ bảng Book
                    String bookName = resultSet.getString("title");
                    int bookPrice = resultSet.getInt("bookPrice");
                    CartItem cartItem = new CartItem(id,bookName,volume,bookPrice,type,"null");
                    cartItems.add(cartItem);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public void removeBookFromCart(String email, String bookId) {
        String queryDelete;
        if ("is not null".equals(bookId)) { // Kiểm tra điều kiện xóa tất cả
            queryDelete = "DELETE FROM cart WHERE email = ?";
        } else {
            queryDelete = "DELETE FROM cart WHERE email = ? AND bookID = ?";
        }

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(queryDelete)) {
            stmt.setString(1, email);
            if (!"is not null".equals(bookId)) {
                stmt.setString(2, bookId);
            }
            stmt.executeUpdate();
            System.out.println("Xóa thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookQuantityInCart(String email, String bookId, int newQuantity)  {
        String queryUpdate = "UPDATE cart SET quantity = ? WHERE email = ? AND bookID = ?";

        try (Connection connection = DBConnection.getInstance().getConnection() ;
             PreparedStatement stmt = connection.prepareStatement(queryUpdate)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, email);
            stmt.setString(3, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void savePayments(String email, List<CartItem> cartItems) {
        String query = "INSERT INTO payment (bookID, email, date, quantity, type) VALUES (?, ?, CURRENT_DATE, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            for (CartItem item : cartItems) {
                stmt.setString(1, item.getId()); // bookID
                stmt.setString(2, email);       // email
                stmt.setInt(3, item.getVolume()); // quantity
                stmt.setString(4, item.getStatus());        //status : mua hay mượn
                stmt.addBatch();               // Thêm vào batch
            }

            stmt.executeBatch(); //
            System.out.println("Thanh toán thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantityInStock()  {
            getQuantityInStock();
            String query = "update books set quantityInStock = ? where bookID = ?";
            try(Connection connection = DBConnection.getInstance().getConnection();PreparedStatement stmt = connection.prepareStatement(query)) {
                for(CartItem item : CartList){
                        String bookId = item.getId();
                        if(quantityBook == null){
                            showErrAndEx.showAlert("null");
                        }
                        int Oldquantity  = quantityBook.get(bookId);
                        int newquantity = Oldquantity -item.getVolume();
                        stmt.setInt(1, newquantity);
                        stmt.setString(2, bookId);
                        stmt.addBatch();
                }
                stmt.executeBatch();
            } catch(SQLException e){
                e.printStackTrace();
            }
           for(CartItem item : CartList){
               String bookId = item.getId();
               if(quantityBook == null){
                   showErrAndEx.showAlert("null");
               }
               int oldquantity = quantityBook.get(bookId);
               int newquantity = oldquantity - item.getVolume();
               Booklist.getInstance().updateBookQuantity(bookId, newquantity);
           }
    }
    public void getQuantityInStock(){
        String query = "select bookID, quantityInStock from books";
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)){
                try(ResultSet rs = stmt.executeQuery()){
                    while(rs.next()){
                        String bookID = rs.getString("bookID");
                        int quantity = rs.getInt("quantityInStock");
                        quantityBook.put(bookID, quantity);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateBorrowBook() {
        String query = "INSERT INTO borrow (Email, bookID, borrowDate, returnDate) " +
                "VALUES (?, ?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 2 WEEK))";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Duyệt qua danh sách CartList và thêm vào batch
            for (CartItem item : CartList) {
                if(item.getStatus().equals("BORROW")){
                    stmt.setString(1, email); // Thay email bằng biến chứa email hợp lệ
                    stmt.setString(2, item.getId());
                    stmt.addBatch(); // Thêm câu lệnh vào batch
                }
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Loading(boolean isLoading) {
        LoadingIndicator.setVisible(isLoading);
        if (isLoading) {
            LoadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        } else {
            LoadingIndicator.setProgress(0); // Đặt lại nếu không còn loading
        }
    }
}
