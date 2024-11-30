package org.example.ilib.Processor;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.example.ilib.Controller.showErrAndEx;

public class TableViewExample extends Application {

    @Override
    public void start(Stage stage) {
        // Tạo danh sách ObservableList để chứa dữ liệu
        ObservableList<bookEx> books = FXCollections.observableArrayList(
                new bookEx("Book 1", "Author 1", 5),
                new bookEx("Book 2", "Author 2", 10)
        );

        // Tạo TableView và các TableColumn
        TableView<bookEx> tableView = new TableView<>(books);

        // Cột Title
        TableColumn<bookEx, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Cho phép chỉnh sửa
        titleColumn.setOnEditCommit(event -> {
            event.getRowValue().setTitle(event.getNewValue()); // Cập nhật dữ liệu sau khi chỉnh sửa
        });

        // Cột Author
        TableColumn<bookEx, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setOnEditCommit(event -> {
            event.getRowValue().setAuthor(event.getNewValue());
        });

        // Cột Quantity
        TableColumn<bookEx, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            event.getRowValue().setQuantity(event.getNewValue());
        });

        // Thêm các cột vào TableView
        tableView.getColumns().addAll(titleColumn, authorColumn, quantityColumn);
        tableView.setEditable(true); // Đảm bảo TableView có thể chỉnh sửa
        // Tạo Button để thêm một hàng mới
        Button addButton =  new Button("Add New Book");
        addButton.setOnAction(event -> {
            // Tạo đối tượng mới
            bookEx newBook = new bookEx("New Book", "New Author", 1);

            // Thêm vào ObservableList
            books.add(newBook);
        });
        Button deleteButton = new Button("Delete Selected Book");
        deleteButton.setOnAction(event -> {
            // Lấy dòng đã chọn trong TableView
            bookEx selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                // Xóa dòng đã chọn
                books.remove(selectedBook);
            } else {
                // Hiển thị cảnh báo nếu không có dòng nào được chọn
                showErrAndEx.showAlert("Please select a book to delete.");
            }
        });



        // Tạo layout và cảnh
        VBox vbox = new VBox(tableView, addButton,deleteButton);
        Scene scene = new Scene(vbox);
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!tableView.isHover()) { // Kiểm tra nếu chuột không nằm trong TableView
                tableView.getSelectionModel().clearSelection();
            }
        });
        stage.setScene(scene);
        stage.setTitle("Editable TableView Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
