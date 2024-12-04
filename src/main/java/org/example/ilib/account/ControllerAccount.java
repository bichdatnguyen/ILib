package org.example.ilib.account;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.ilib.controller.DBConnection;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerAccount {
    @FXML
    public ImageView background;

    @FXML
    public ImageView tinyField;

    @FXML
    private Label phone;

    @FXML
    private  Label password;

    @FXML
    private  Label email;

    @FXML
    private Label fullName;

    @FXML
    private Text updateAccount;

    @FXML
    private Button backButton;

    @FXML
    private ImageView avatar;

    private static ControllerAccount instance;

    /**
     * initialize ControllerAccount.
     */
    @FXML
    public void initialize() {
        setPropertiesFromDatabase();
        loadProperties();
    }

    /**
     * this method will be used in initialize method.
     */
    public void loadProperties() {
        String avatarPath = Account.getInstance().getAvatarPath();
        String phoneNumber = Account.getInstance().getPhone();
        String fullname = Account.getInstance().getFullName();
        String emailTemp = Account.getInstance().getEmail();
        background.setImage(new Image(getClass().getResourceAsStream("/org/assets/Background.png")));
        tinyField.setImage(new Image(getClass().getResourceAsStream("/org/assets/tinyField.png")));

        if (avatarPath == null) {
            Image avatarImage = new Image(getClass().getResourceAsStream("/org/assets/user-3296.png"));
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

    /**
     * Back to menu.
     * @param mouseEvent back when you click mouse
     * @throws IOException throw IOException
     */
    public void Back(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);
    }

    /**
     * this method will handle Changing avatar in user scene.
     * @param mouseEvent clicked text
     */
    public void setAvatar(MouseEvent mouseEvent) {
        System.out.println("mouse clicked");
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
                //update
                avatar.setImage(avatarView);
                String avatarP = selectedFile.getAbsolutePath();
                updateAvatarInDatabase(avatarP);
                Account.getInstance().setAvatarPath(avatarP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this method will insert into database avatarPath of user.
     * @param avatarPath avatar path of user
     */
    private void updateAvatarInDatabase(String avatarPath) {
        String updateQuery = "UPDATE user SET avatarPath = ? where Email = ? and password = ?";
        try( PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(updateQuery)) {
            stmt.setString(1, avatarPath);
            stmt.setString(2, Account.getInstance().getEmail());
            stmt.setString(3, Account.getInstance().getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method will be used to set properties for the scene.
     */
    private void setPropertiesFromDatabase() {
        String query = "SELECT avatarPath,phoneNumber,fullName FROM user WHERE email = ? and password = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
             ) {
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, Account.getInstance().getPassword());
            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()) {
                    Account.getInstance().setAvatarPath(resultSet.getString("avatarPath"));
                    Account.getInstance().setPhone(resultSet.getString("phoneNumber"));
                    Account.getInstance().setFullName(resultSet.getString("fullName"));
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method will orient user to updateAccount scene.
     * @param mouseEvent text clicked
     * @throws IOException io error
     */
    public void updateAccount(MouseEvent mouseEvent) throws IOException{
        Stage stage = (Stage)updateAccount.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/UpdateAccount.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}

