package org.example.ilib.book.BookDetail;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.sql.Timestamp;

public class ControllerComment {
    @FXML
    private Label cmt;

    @FXML
    private Label emailCmt;

    @FXML
    private Label timeCmt;

    /**
     * show comment.
     *
     * @param email email information
     * @param cmt   comment
     * @param now   real time
     * @throws SQLException prevent sql exception
     */
    public void showCmt(String email, String cmt, Timestamp now) throws SQLException {
        this.emailCmt.setText(email);
        this.cmt.setText(cmt);
        this.timeCmt.setText(String.valueOf(now));
    }
}
