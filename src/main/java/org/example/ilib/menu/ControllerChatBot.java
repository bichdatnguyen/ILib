package org.example.ilib.menu;

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
import org.example.ilib.book.bookdetail.ControllerComment;
import org.example.ilib.controller.ShowErrAndEx;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ControllerChatBot {
    @FXML
    private VBox QnA;

    @FXML
    private Button clickButton;

    @FXML
    private TextArea questionText;


    /**
     * Answer question.
     *
     * @throws SQLException prevent sql exception
     */
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
                userBox = user.load();
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
                chatBox = chat.load();
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

    /**
     * post question.
     *
     * @param event post question when button is clicked
     */
    @FXML
    public void clickButtonClick(MouseEvent event) {
        if (!questionText.getText().trim().equals("")) {
            try {
                handleQuestion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            ShowErrAndEx.showAlert("Vui lòng nhập câu hỏi");
        }
    }

    /**
     * post question.
     *
     * @param event post question when key pressed
     */
    @FXML
    public void questionTextRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!questionText.getText().trim().equals("")) {
                try {
                    handleQuestion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                ShowErrAndEx.showAlert("Vui lòng nhập câu hỏi cần giải đáp");
            }
        }
    }

    /**
     * initialize chat bot.
     */
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