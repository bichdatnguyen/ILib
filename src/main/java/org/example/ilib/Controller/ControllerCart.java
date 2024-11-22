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
    private TextField VouluneText;
    private ObservableList<Cart> CartList;





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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Bạn có muốn xoá mục này không ?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            CartList.remove(CartTable.getSelectionModel().getSelectedItem());
        }
       // CartList.remove(CartTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void BuyQRclick(MouseEvent event) {
        QRCode.setImage(new Image(getClass().getResourceAsStream("/org/assets/qrcode.png")));
    }

    @FXML
    void totalMoney(MouseEvent event) {

    }

    @FXML
    void addVoume(ActionEvent event) {

    }

    @FXML
    void substractVoume(ActionEvent event) {

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
