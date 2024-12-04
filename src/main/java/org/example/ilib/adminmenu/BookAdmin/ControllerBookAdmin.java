package org.example.ilib.adminmenu.BookAdmin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ilib.book.Book;
import org.example.ilib.booklist.Booklist;
import org.example.ilib.controller.DBConnection;
import org.example.ilib.controller.showErrAndEx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerBookAdmin implements Initializable {

    @FXML
    private Button EditButton;

    @FXML
    private TextField searchText;
    @FXML
    private Button RefreshButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button AddButton;
    @FXML
    private TableView<Book> BookTable;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, Integer> quanityCol;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableColumn<Book, String> bookIDCol;

    @FXML
    private TableColumn<Book, Integer> priceCol;
    private ObservableList<Book> BookList;

    @FXML
    private VBox searchVbox;
    @FXML
    private ScrollPane searchScroll;


    @FXML
    void AddButtonClick(MouseEvent event) {
           try{
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/AddEditBook.fxml"));
               Parent root = fxmlLoader.load();
               ControllerAddEdit controllerAddEdit =fxmlLoader.getController();
               controllerAddEdit.setTextLabel("Hãy thêm sách và nhấn cập nhập");
               controllerAddEdit.setStatus(ControllerAddEdit.ADD);
               Stage popup = new Stage();
               popup.initModality(Modality.APPLICATION_MODAL);
               popup.setTitle("Add Book");
               Scene scene = new Scene(root);
               popup.setScene(scene);
               popup.showAndWait();


           } catch (IOException e) {
               e.printStackTrace();
           }
    }


    @FXML
    void EditButtonClick(MouseEvent event) {
        if(BookTable.getSelectionModel().getSelectedItem() == null){
            showErrAndEx.showAlert("Vui lòng chon sách cần chỉnh sửa");
            return;
        }
        Book book = BookTable.getSelectionModel().getSelectedItem();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/AddEditBook.fxml"));
            Parent root = fxmlLoader.load();
            ControllerAddEdit controllerAddEdit =fxmlLoader.getController();
            controllerAddEdit.setStatus(ControllerAddEdit.EDIT);
            controllerAddEdit.setTextLabel("Chỉnh sửa thông tin và nhấn cập nhập");
            controllerAddEdit.setAuthorText(book.getAuthor());
            controllerAddEdit.setTitleText(book.getTitle());
            controllerAddEdit.setBookIdText(book.getId());
            controllerAddEdit.setQuantityText(String.valueOf(book.getQuantity()));
            controllerAddEdit.setDescriptionText(book.getDescription());
            controllerAddEdit.setPriceText(String.valueOf(book.getPrice()));
            controllerAddEdit.setBook(book);
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Edit Book");
            Scene scene = new Scene(root);
            popup.setScene(scene);
            popup.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteButtonClick(MouseEvent event) {
            if(BookTable.getSelectionModel().getSelectedItem() == null){
                showErrAndEx.showAlert("Vui lòng chọn sách cần xóa");
            } else{
                Alert alert = showErrAndEx.showAlert("Bạn chắc chắn muốn xóa chứ");
                if(alert.getResult() == ButtonType.OK){
                    deleteBook(BookTable.getSelectionModel().getSelectedItem().getId());
                    BookList.remove(BookTable.getSelectionModel().getSelectedItem());
                    BookTable.refresh();

                }
            }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            getBooksfromDB();
            bookIDCol.setCellValueFactory(new PropertyValueFactory<Book,String>("id"));
            titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
            authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
            priceCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("price"));
            quanityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
            BookTable.setItems(BookList);
            anchorPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (!BookTable.isHover() &&
                        !(EditButton.isHover() || AddButton.isHover() || DeleteButton.isHover())) {
                    BookTable.getSelectionModel().clearSelection();
                }
            });

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void getBooksfromDB(){
        String query = "select *, authorName from books natural join author";
        List<Book> books = new ArrayList<>();
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)){
                try(ResultSet rs = stmt.executeQuery()){
                    while(rs.next()){
                        String bookID = rs.getString("bookID");
                        String title = rs.getString("title");
                        String author = rs.getString("authorName");
                        int quantity = rs.getInt("quantityInStock");
                        int price = rs.getInt("bookPrice");
                        String description = rs.getString("description");
                        Book book = new Book(bookID, title, author, quantity, price,description);
                        books.add(book);
                    }
                    this.BookList = FXCollections.observableArrayList(books);
                }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void searchInfofromDB(String search) {
        String query = "SELECT title FROM books natural join author WHERE title LIKE ? OR bookID LIKE ? OR description LIKE ? OR authorName LIKE ?";
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String queryPattern = "%" + search + "%";
            preparedStatement.setString(1, queryPattern);
            preparedStatement.setString(2, queryPattern);
            preparedStatement.setString(3, queryPattern);
            preparedStatement.setString(4, queryPattern);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String title = resultSet.getString("title");
                    Text text = new Text(title);
                    text.setOnMouseClicked(mouseEvent -> {
                        searchText.clear();
                        searchText.setText(text.getText());
                    });
                    text.setOnMouseEntered(mouseEvent -> {
                        text.setFill(javafx.scene.paint.Color.BLUE);
                    });
                    text.setOnMouseExited(mouseEvent -> {
                        text.setFill(javafx.scene.paint.Color.BLACK);
                    });
                    searchVbox.getChildren().add(text);
                }
                if (!searchVbox.getChildren().isEmpty()) {
                    searchScroll.setVisible(true);
                } else {
                    searchScroll.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void searchKeyBoard(KeyEvent event) {
        if(!searchText.getText().equals("")){
            searchVbox.getChildren().clear();
            searchInfofromDB(searchText.getText());
        } else{
            searchVbox.getChildren().clear();
            searchScroll.setVisible(false);
        }
    }
    @FXML
    public void searchEnter(KeyEvent event) {
        if(!searchText.getText().equals("")){
            FilteredList<Book> filteredBooks = new FilteredList<>(BookList, b -> true);
            BookTable.setItems(filteredBooks);
            searchText.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredBooks.setPredicate(book -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
        }
    }
//rating , shelf, payment , borrow,history,categories,cart,author,books
    public void deleteBook(String bookID) {
        String query = "delete from books where bookID = ?";
        Booklist.getInstance().deleteBook(bookID);
        try(Connection connection =DBConnection.getInstance().getConnection();){

            try( PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, bookID);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void RefreshButtonClick(MouseEvent event) {
        getBooksfromDB();
        BookTable.setItems(BookList);
        BookTable.refresh();
    }
}
