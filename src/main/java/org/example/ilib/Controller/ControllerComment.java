package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;

import java.beans.EventHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ControllerComment {
    @FXML
    private Label cmt;

    @FXML
    private Label emailCmt;

    @FXML
    private Label timeCmt;

    public void showCmt(String email, String cmt, Timestamp now) throws SQLException {
        this.emailCmt.setText(email);
        this.cmt.setText(cmt);
        this.timeCmt.setText(String.valueOf(now));
    }
}
