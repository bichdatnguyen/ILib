package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControllerAddEdit {

    @FXML
    private TextField bookIdText;

    @FXML
    private Button UpdateButton;

    @FXML
    private TextField titleText;

    @FXML
    private TextField descriptionText;

    @FXML
    private TextField authorText;

    @FXML
    private TextField quantityText;
    @FXML
    private VBox updateVbox;

    @FXML
    private TextField priceText;
    private boolean checkCondition = false;

    @FXML
    private Label TextLabel;
    private int status =0;
    public static final int ADD = 1;
    public static final int EDIT = 2;
    private Book book = new Book();
    public void setStatus(int status) {
        this.status = status;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    @FXML
    void UpdateButtonClick(MouseEvent event) {
            if(status == ADD) {
                if(bookIdText.getText()==null||bookIdText.getText().equals("")) {
                    showErrAndEx.showAlert("Vui lòng nhập ID sách");
                    return;
                }
                if(titleText.getText()==null||titleText.getText().equals("")) {
                    showErrAndEx.showAlert("Vui lòng nhập tiêu đề sách");
                    return;
                }
                Book book = new Book();
                book.setId(bookIdText.getText());
                book.setPrice(Integer.parseInt(priceText.getText()));
                book.setTitle(titleText.getText());
                book.setDescription(descriptionText.getText());
                book.setAuthor(authorText.getText());
                book.setQuantity(Integer.parseInt(quantityText.getText()));
                addBook(book);


            } else if(status == EDIT) {
                if(bookIdText.getText()== null||bookIdText.getText().equals("")) {
                    showErrAndEx.showAlert("Vui lòng nhập id sách");
                    return;
                }
                if(titleText.getText()==null||titleText.getText().equals("")) {
                    showErrAndEx.showAlert("Vui lòng nhập tiêu đề sách");
                    return;
                }

                Book book1 = new Book();
                book1.setId(bookIdText.getText());
                book1.setPrice(Integer.parseInt(priceText.getText()));
                book1.setTitle(titleText.getText());
                book1.setDescription(descriptionText.getText());
                book1.setAuthor(authorText.getText());
                book1.setQuantity(Integer.parseInt(quantityText.getText()));
                updateBook(book1,book.getId());

            }
        if(checkCondition){
            Stage stage = (Stage) updateVbox.getScene().getWindow();
            stage.close();
        }

    }
    public void setBookIdText(String bookId) {
        bookIdText.setText(bookId);
    }
    public void setTitleText(String title) {
        titleText.setText(title);
    }
    public void setDescriptionText(String description) {
        descriptionText.setText(description);
    }
    public void setAuthorText(String author) {
        authorText.setText(author);
    }
    public void setQuantityText(String quantity) {
        quantityText.setText(quantity);
    }
    public void setPriceText(String price) {
        priceText.setText(price);
    }
    public void setTextLabel(String text) {
        TextLabel.setText(text);
    }

    public void addBook(Book book) {
        String query = "insert into books(bookID,thumbnail,description,title,bookPrice,quantityInStock) values(?,'/org/assets/noImage.png',?,?,?,?)";
        String query2 = "insert into author(bookID,authorName) values(?,?)";
        try{
            if(DBConnection.getInstance().bookExist(book.getId())){
                showErrAndEx.showAlert("Sách này có mã ID trùng với sách khác\n Vui lòng nhập lại");
                checkCondition = false;
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(Connection connection = DBConnection.getInstance().getConnection();) {
            try( PreparedStatement stmt  = connection.prepareStatement(query)){
                stmt.setString(1, book.getId());
                stmt.setString(2, book.getDescription());
                stmt.setString(3, book.getTitle());
                stmt.setInt(4, book.getPrice());
                stmt.setInt(5, book.getQuantity());
                stmt.executeUpdate();
            } catch(SQLException e){
                e.printStackTrace();
            }
            try(PreparedStatement stmt2  = connection.prepareStatement(query2)){
                stmt2.setString(1, book.getId());
                stmt2.setString(2, book.getAuthor());
                stmt2.executeUpdate();
            } catch(SQLException e){
                e.printStackTrace();
            }
            checkCondition = true;
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void updateBook(Book book, String bookID) {
        Booklist.getInstance().updateBook(bookID,book);
        String query1 = "update books set bookID = ?, title = ?, bookPrice = ?, quantityInStock =? where bookID = ?";
        String query2 = "update books set bookID = ?, description = ?, title = ?, bookPrice = ?, quantityInStock =? where bookID = ?";
        System.out.println(!bookID.equals(book.getId().trim()));
      //  System.out.println(book.getId());

        try{
            if(DBConnection.getInstance().bookExist(book.getId()) && !bookID.equals(book.getId())){
                showErrAndEx.showAlert("bạn đã thiết lập id sách trùng với 1 sách khác \n Vui lòng nhập lại");
                checkCondition = false;
                return;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(descriptionText.getText() ==null || descriptionText.getText().equals("")) {
            try(Connection connection =DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query1) ){
                stmt.setString(1, book.getId());
                stmt.setString(2, book.getTitle());
                stmt.setInt(3, book.getPrice());
                stmt.setInt(4, book.getQuantity());
                stmt.setString(5, bookID);
                stmt.executeUpdate();
                checkCondition = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else{
            try(Connection connection =DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query2) ){
                stmt.setString(1, book.getId());
                stmt.setString(2, book.getDescription());
                stmt.setString(3, book.getTitle());
                stmt.setInt(4, book.getPrice());
                stmt.setInt(5, book.getQuantity());
                stmt.setString(6, bookID);
                stmt.executeUpdate();
                checkCondition = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }




}
