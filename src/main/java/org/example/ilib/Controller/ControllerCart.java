package org.example.ilib.Controller;

import com.stripe.model.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.example.ilib.Processor.Cart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCart implements Initializable {
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
    private TableView<Cart> CartTable;
    @FXML
    private TableColumn<Cart, String> StatusCol;
    @FXML
    private TableColumn<Cart, String> NameCol;

    @FXML
    private TableColumn<Cart, String> VoucherCol;
    @FXML
    private TableColumn<Cart, Integer> MoneyCol;
    @FXML
    private TableColumn<Cart, Integer> VolumeCol;
    @FXML
    private TextField VoulumeText;
    private boolean isTotalCalculated = false;

    private ObservableList<Cart> CartList;
    private static final int ADDVOLUME = 1;
    private static final int SUBSTRACTVOLUME = 2;
    private static int totalMonet =0;





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
        if(!isTotalCalculated){
            showErrAndEx.showAlert("Vui lòng nhấn tổng tiền đề thanh toán");
        } else if(totalMonet == 0){
            showErrAndEx.showAlert("Số tiền thanh toán không hợp lệ");
        } else{
            QRCode.setImage(new Image(getClass().getResourceAsStream("/org/assets/qrcode.png")));
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
                        if(status == ADDVOLUME){
                            int total = currVol  + volNum;
                            CartTable.getSelectionModel().getSelectedItem().setVolume(total);
                        } else if(status == SUBSTRACTVOLUME){
                            if(volNum >= currVol){
                                CartTable.getSelectionModel().getSelectedItem().setVolume(0);
                            } else {
                                int total = currVol - volNum;
                                CartTable.getSelectionModel().getSelectedItem().setVolume(total);
                            }
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
           CartList = FXCollections.observableArrayList(
                   new Cart("Jav",12,120,"Mua","Discount 20%")
           );
           NameCol.setCellValueFactory(new PropertyValueFactory<Cart,String>("name"));
           VolumeCol.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("volume"));
           MoneyCol.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("money"));
           StatusCol.setCellValueFactory(new PropertyValueFactory<Cart, String>("status"));
           VoucherCol.setCellValueFactory(new PropertyValueFactory<Cart, String>("voucher"));
            CartTable.setItems(CartList);

       } catch(Exception e){
           e.printStackTrace();
       }

    }
}
