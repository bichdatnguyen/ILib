package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.ilib.Processor.Comment;

import java.sql.ResultSet;
import java.time.LocalDateTime;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;


public class ControllerComment {
    @FXML
    private TextArea commentText;

    @FXML
    private ImageView userView;

    @FXML
    private Label nameLabel;

    private Image loadImage(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return new Image(path, true);
        } else {
            return new Image(getClass().getResourceAsStream(path));
        }

    }


    public void setComment(Comment comment) {
        commentText.setText(comment.getComment());
       // userView.setImage(new Image(getClass().getResource(comment.getThumbnail())));
        userView.setImage(new Image(getClass().getResourceAsStream(comment.getThumbnail())));
        nameLabel.setText(comment.getName());
    }

    public void addComment(Comment comment) {

        String query = "INSERT INTO comments (Email, bookID,Comment, Time) VALUES (?, ?,?,?)";
        try(Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, comment.getEmail());
                preparedStatement.setString(2,comment.getBookID());
                preparedStatement.setString(3,comment.getComment());
                preparedStatement.setString(4, "CURRENT_TIME");
                preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editComment(Comment comment) {
        String query = "UPDATE rating SET Comment=?, Time=? WHERE Email=?";
        try(Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment.getComment());
            preparedStatement.setString(2,"CURRENT_TIME");
            preparedStatement.setString(3, comment.getEmail());
            preparedStatement.executeUpdate();
        } catch(Exception e){

        }
    }

    public void removeComment(Comment comment) {
        String query = "DELETE FROM rating WHERE Email=? AND BookID=? AND Time=?";

    }



}
