package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerAccount {
    @FXML
    private TextField fullName;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private TextField address;

    @FXML
    private Button backButton;

    @FXML
    private ImageView avatar;

    @FXML
    public void initialize() {
        if(Account.getInstance().getAvatarPath() == null) {
            Account.getInstance().setAvatarPath(getAvatarPathFromDatabase());
        }
        loadAvatar();
        //phone.setText(Account.getInstance().getPhone());
        //email.setText(Account.getInstance().getEmail());
        //fullName.setText(Account.getInstance().getFullName());

    }

    public void loadAvatar() {
        String avatarPath = Account.getInstance().getAvatarPath();
        if (avatarPath == null) {
            // Hiển thị avatar từ đường dẫn lưu trong cơ sở dữ liệu
            Image avatarImage = new Image("D:/GitHub/Ilib/Ilib/src/main/resources/org/assets/user-3296.png");
            avatar.setImage(avatarImage);
        } else {
            Image avatarImage = new Image(avatarPath);
            avatar.setImage(avatarImage);
        }
    }

    public void Back(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
    }

    public void setAvatar(javafx.scene.input.MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh làm avatar");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Tất cả tệp", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(avatar.getScene().getWindow());

        if(selectedFile!=null) {
            try {
                Image avatarView = new Image(selectedFile.toURI().toString());
                avatar.setImage(avatarView);
                String avatarP = selectedFile.getAbsolutePath();
                updateAvatarInDatabase(avatarP);
                Account.getInstance().setAvatarPath(avatarP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAvatarInDatabase(String avatarPath) {
        String updateQuery = "UPDATE user SET avatarPath = ? where Email = ? and password = ?";
        try {
            //DBConnection conn = DBConnection.getInstance().getConnection().prepareStatement(updateQuery)
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(updateQuery);
            stmt.setString(1, avatarPath);
            stmt.setString(2, Account.getInstance().getEmail());
            stmt.setString(3, Account.getInstance().getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAvatarPathFromDatabase() {
        String avatarPath = null;
        String query = "SELECT avatarPath FROM user WHERE email = ? and password = ?";

        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, Account.getInstance().getPassword());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                avatarPath = resultSet.getString("avatarPath");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avatarPath;
    }
}

