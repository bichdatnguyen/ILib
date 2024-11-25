package org.example.ilib.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.ilib.Processor.Account;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerAccount {
    @FXML
    public Label phone;

    @FXML
    public Label password;

    @FXML
    public Label email;

    @FXML
    public Label fullName;

    @FXML
    public Text updateAccount;

    @FXML
    private Button backButton;

    @FXML
    private ImageView avatar;

    @FXML
    public void initialize() {
        if (Account.getInstance().getPhone() == null) {
            setPropertiesFromDatabase();
        }
        loadProperties();
    }

    public void loadProperties() {
        String avatarPath = Account.getInstance().getAvatarPath();
        String phoneNumber = Account.getInstance().getPhone();
        String fullname = Account.getInstance().getFullName();
        String emailTemp = Account.getInstance().getEmail();
        if (avatarPath == null) {
            Image avatarImage = new Image("D:/GitHub/Ilib/ilib/src/main/resources/org/assets/user-3296.png");
            avatar.setImage(avatarImage);
        } else {
            Image avatarImage = new Image(avatarPath);
            avatar.setImage(avatarImage);
        }

        if (phoneNumber != null) {
            phone.setText(phoneNumber);
        } else {
            System.out.println("phone number is null");
        }

        if (fullname != null) {
            fullName.setText(fullname);
        } else {
            System.out.println("fullname is null");
        }

        if (emailTemp != null) {
            email.setText(emailTemp);
        } else {
            System.out.println("email is null");
        }

        if (password != null) {
            int size = Account.getInstance().getPassword().length();
            String p = new String();
            for (int i = 0; i < size; i++) {
                p += "*";
            }
            password.setText(p);
        } else {
            System.out.println("password is null");
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

        if (selectedFile != null) {
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

    private void setPropertiesFromDatabase() {
        String query = "SELECT avatarPath,phoneNumber,fullName FROM user WHERE email = ? and password = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, Account.getInstance().getPassword());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Account.getInstance().setAvatarPath(resultSet.getString("avatarPath"));
                Account.getInstance().setPhone(resultSet.getString("phoneNumber"));
                Account.getInstance().setFullName(resultSet.getString("fullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccount(MouseEvent mouseEvent) throws IOException{
        Stage stage = (Stage)updateAccount.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/updateAccount.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}

