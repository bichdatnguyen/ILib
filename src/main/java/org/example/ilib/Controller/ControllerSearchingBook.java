package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerSearchingBook  {

    @FXML
    private Button Back;
    @FXML
    private GridPane gridPaneBook;
    @FXML
    private Label searchLabel;


    private List<Book> booksearching = new ArrayList();

    public void setLabel(String label){
        searchLabel.setText(label);
    }
    public void addBook(Book book) {
       booksearching.add(book);

    }
    @FXML
    void BackToMenu(MouseEvent event) throws IOException {
            Stage stage = (Stage) Back.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/Menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
    }

    void show(){
        try
        {
            int column =0 , row =0;

           for (Book book : booksearching) {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ilib/book.fxml"));

               HBox hbox = (HBox) fxmlLoader.load();
               ControllerBook controllerBook = (ControllerBook) fxmlLoader.getController();
             //  System.out.println(book.getAuthor() + " " + book.getTitle()+ " "+ book.getImage());
               controllerBook.setBook(book);
               if(column == 2){
                   column = 0;
                   row++;
               }
               gridPaneBook.add(hbox,column++,row);

           }
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
