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
                if(bookIdText.getText().equals("")) {
                    showErrAndEx.showAlert("Vui lòng nhập ID sách");
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
                Stage stage = (Stage) updateVbox.getScene().getWindow();
                stage.close();

            } else if(status == EDIT) {
                Book book = new Book();
                book.setId(bookIdText.getText());
                book.setPrice(Integer.parseInt(priceText.getText()));
                book.setTitle(titleText.getText());
                book.setDescription(descriptionText.getText());
                book.setAuthor(authorText.getText());
                book.setQuantity(Integer.parseInt(quantityText.getText()));
                updateBook(book,book.getId());
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
        String query = "insert into books(bookID,description,title,bookPrice,quantityInStock) values(?,?,?,?,?)";
        String query2 = "insert into author(bookID,authorName) values(?,?)";
        try{
            if(DBConnection.getInstance().bookExist(book.getId())){
                showErrAndEx.showAlert("Sách này có mã ID trùng với sách khác\n Vui lòng nhập lại");
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

        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void updateBook(Book book, String bookID) {
        String query = "update books set bookID = ?, description = ?, title = ?, bookPrice = ?, quantityInStock =? where bookID = ?";
        try(Connection connection =DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query) ){
            stmt.setString(1, book.getId());
            stmt.setString(2, book.getDescription());
            stmt.setString(3, book.getTitle());
            stmt.setInt(4, book.getPrice());
            stmt.setInt(5, book.getQuantity());
            stmt.setString(6, bookID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}