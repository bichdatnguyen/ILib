package org.example.ilib.Controller;

import com.google.zxing.WriterException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.ilib.Processor.CartItem;
import org.example.ilib.Processor.QRCodeAuto;
import org.sqlite.core.DB;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    private boolean isTotalCalculated = false;

    private ObservableList<CartItem> CartList;
    private static final int ADDVOLUME = 1;
    private static final int SUBSTRACTVOLUME = 2;
    private static int totalMonet =0;
    private static String email ="23021524@vnu.edu.vn";

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
                } catch (SQLException e) {
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
            try {
                String path = "The amount paid is: " + String.valueOf(totalMonet) + " USD \n Have a nice day!";
                // Tạo mã QR và lấy đường dẫn
                String qrImagePath = QRCodeAuto.taoQrCode(path);

                // Đặt hình ảnh QR vào ImageView
                QRCode.setImage(new Image(qrImagePath));
                try{
                    savePayments(email,CartList.stream().toList());
                    removeBookFromCart(email,"is not null");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (IOException | WriterException e) {
                e.printStackTrace();
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
        totalMoney.setText(String.valueOf(totalMonet));
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
                        } catch (SQLException e) {
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

       } catch(Exception e){
           e.printStackTrace();
       }

    }



    public List<CartItem> getCartsByEmail(String email) throws SQLException {
        // Câu lệnh SQL sử dụng INNER JOIN để lấy thông tin từ cả bảng Payment và Book
        String query = "SELECT cart.*, books.title, books.bookPrice " +
                "FROM cart " +
                "INNER JOIN books ON cart.bookID = books.bookID " +
                "WHERE cart.email = ?";
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection() ; // Kết nối tới cơ sở dữ liệu
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set giá trị tham số cho câu lệnh (email)
            stmt.setString(1, email);

            // Thực thi câu lệnh và lấy kết quả
            ResultSet resultSet = stmt.executeQuery();

            // Xử lý kết quả trả về
            while (resultSet.next()) {
                // Lấy dữ liệu từ bảng Payment
                String id = resultSet.getString("bookID");
                String paymentEmail = resultSet.getString("email");
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
        return cartItems;
    }

    public void removeBookFromCart(String email, String bookId) throws SQLException{
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

    public void updateBookQuantityInCart(String email, String bookId, int newQuantity) throws SQLException {
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


    public void savePayments(String email, List<CartItem> cartItems) throws SQLException {
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


}
