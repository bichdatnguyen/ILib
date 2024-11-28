package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;
import org.example.ilib.Processor.Comment;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerBookComment {
    @FXML
    private Button backToBookDetail;

    @FXML
    private TextArea cmt;

    @FXML
    private VBox cmtBox;

    @FXML
    private Button postCmt;

    private String bookID;
    private Scene FowardScene;

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void saveFowardScene(Scene scene) {
        this.FowardScene = scene;
    }

    @FXML
    public void back(MouseEvent event) {
        Stage stage = (Stage) backToBookDetail.getScene().getWindow();

        Scene scene = FowardScene;
        stage.setScene(scene);
    }

    @FXML
    public void postCmtInBook(MouseEvent event) {
        if (cmt == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Bạn chưa nhập comment");
            alert.show();
        } else {
            FXMLLoader fx = new FXMLLoader();
            fx.setLocation(getClass().getResource("/org/example/ilib/Comment.fxml"));
            VBox commentBox = null;
            try {
                commentBox = (VBox) fx.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ControllerComment controllerCmt = fx.getController();
            String email = Account.getInstance().getEmail();
            String cmtText = cmt.getText();
            LocalDateTime now = LocalDateTime.now();
            try {
                controllerCmt.showCmt(email, cmtText, Timestamp.valueOf(now));
                DBConnection.getInstance().saveCmt(email, bookID, cmtText, Timestamp.valueOf(now));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            cmtBox.getChildren().add(commentBox);
            cmt.clear();
        }
    }

    public void showAllCmtInBook() {
        cmtBox.getChildren().clear();
        List<Comment> comments;
        try {
            comments = DBConnection.getInstance().allBookCmt(bookID);
            System.out.println("Book id: " + bookID + "have " + comments.size() + " comments");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Comment comment: comments) {
            FXMLLoader fx = new FXMLLoader();
            fx.setLocation(getClass().getResource("/org/example/ilib/Comment.fxml"));
            VBox commentBox = null;
            try {
                commentBox = (VBox) fx.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ControllerComment controllerCmt = fx.getController();
            try {
                controllerCmt.showCmt(comment.getEmail(), comment.getComment(), comment.getDate());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cmtBox.getChildren().add(commentBox);
        }
    }
}

