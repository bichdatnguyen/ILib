package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.ilib.Processor.Account;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerChatBot {
    @FXML
    private VBox QnA;

    @FXML
    private Button postButton;

    @FXML
    private TextArea question;

    @FXML
    public void post(MouseEvent event) throws SQLException {
        QnA.getChildren().clear();

        if (question == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Bạn chưa đặt câu hỏi");
            alert.show();
        } else {
            FXMLLoader user = new FXMLLoader();
            FXMLLoader chat = new FXMLLoader();
            user.setLocation(getClass().getResource("/org/example/ilib/Comment.fxml"));
            chat.setLocation(getClass().getResource("/org/example/ilib/Comment.fxml"));

            VBox userBox = null;
            try {
                userBox = (VBox) user.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ControllerComment you = user.getController();
            String email = "Bạn";
            String qText = question.getText();
            LocalDateTime now = LocalDateTime.now();
            you.showCmt(email, qText, Timestamp.valueOf(now));
            QnA.getChildren().add(userBox);

            VBox chatBox = null;
            try {
                chatBox = (VBox) chat.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ControllerComment bot = chat.getController();
            String emailBot = "Máy";
            String qTextBot = "Tôi không hiểu bạn nói gì";
            LocalDateTime nowBot = LocalDateTime.now();
            bot.showCmt(emailBot, qTextBot, Timestamp.valueOf(nowBot));
            QnA.getChildren().add(chatBox);

            question.clear();
        }
    }
}