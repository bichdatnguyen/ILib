package org.example.ilib.Controller;

import com.google.api.client.util.DateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;
import org.example.ilib.Processor.CartItem;
import org.example.ilib.Processor.Transaction;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerTransactionHistory implements Initializable {

    @FXML
    private Button Back;
    @FXML
    private TableView<Transaction> TransactionTable;
    @FXML
    private TableColumn<Transaction, Integer> paymentIDcol;
    @FXML
    private TableColumn<Transaction, String> bookCol;
    @FXML
    private TableColumn<Transaction, DateTime> dateCol;
    @FXML
    private TableColumn<Transaction, Integer> quantityCol;
    @FXML
    private TableColumn<Transaction, Integer> priceEachCol;
    @FXML
    private TableColumn<Transaction, String> typeCol;

    private ObservableList<Transaction> Transactions;
    private String email = Account.getInstance().getEmail();


    public void setTransactionList(List<Transaction> TransactionList) {
        // Chuyển List thành ObservableList
        this.Transactions = FXCollections.observableArrayList(TransactionList);
    }



    @FXML
    void BackToMenu(MouseEvent event) {
       try {
           Stage stage = (Stage) Back.getScene().getWindow();
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Menu.fxml"));
           Scene scene = new Scene( fxmlLoader.load());
           stage.setScene(scene);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            setTransactionList(getTransactionList(email));
            paymentIDcol.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("paymentID"));
            bookCol.setCellValueFactory(new PropertyValueFactory<Transaction,String>("bookID"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Transaction, DateTime>("date"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("quantity"));
            typeCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("type"));
            priceEachCol.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("priceEach"));

           TransactionTable.setItems(Transactions);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactionList(String email) throws SQLException {
        // Câu lệnh SQL sử dụng INNER JOIN để lấy thông tin từ cả bảng Payment và Book
        String query = "SELECT payment.*, books.title, books.bookPrice " +
                "FROM payment " +
                "INNER JOIN books ON payment.bookID = books.bookID " +
                "WHERE payment.email = ?";
        List<Transaction> TransactionList = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection(); // Kết nối tới cơ sở dữ liệu
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set giá trị tham số cho câu lệnh (email)
            stmt.setString(1, email);

            // Thực thi câu lệnh và lấy kết quả
          try(  ResultSet resultSet = stmt.executeQuery()){
              // Xử lý kết quả trả về
              while (resultSet.next()) {
                  // Lấy dữ liệu từ bảng Payment
                  String paymentID = resultSet.getString("paymentID");
                  String bookID = resultSet.getString("bookID");
                  String bookName = resultSet.getString("title");
                  Timestamp dateTime = resultSet.getTimestamp("date");
                  DateTime date = new DateTime(dateTime);
                  Integer quantity = resultSet.getInt("quantity");
                  // Lấy dữ liệu từ bảng Book
                  String type = resultSet.getString("type");
                  Integer priceEach = resultSet.getInt("priceEach");

                  Transaction transaction = new Transaction(paymentID,bookID,email,date,quantity,type,bookName,priceEach);
                  TransactionList.add(transaction);
              }
          } catch (SQLException e){
              e.printStackTrace();
          }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return TransactionList;
    }

}
