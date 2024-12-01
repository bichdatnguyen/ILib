package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Button clickButton;

    @FXML
    private TextArea questionText;


    public void handleQuestion() throws SQLException {
        QnA.getChildren().clear();

        if (questionText == null) {
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
            String qText = questionText.getText();
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

            questionText.clear();
        }
    }
    @FXML
    public void clickButtonClick(MouseEvent event) {
        try{
            handleQuestion();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void questionTextRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try{
                handleQuestion();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public void initialize() {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/org/assets/click-2399.png")));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        clickButton.setGraphic(imageView);
    }
    @FXML
    public void clickButtonEnter(MouseEvent event) {
        clickButton.setStyle("-fx-background-color: white;");
    }
    @FXML
    public void clickButtonExit(MouseEvent event) {
        clickButton.setStyle("-fx-background-color: transparent;");
    }

}