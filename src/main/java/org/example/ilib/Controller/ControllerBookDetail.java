package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerBookDetail {

    @FXML
    private ToggleGroup Group;

    @FXML
    private ImageView thumbnail;

    @FXML
    private Text titleText1;

    @FXML
    private Button BackButton;

    @FXML
    private Text descriptionText;


    @FXML
    private Text titleText11;

    @FXML
    private Button addCartButton;

    @FXML
    private TextField VolumeTextField;

    @FXML
    private Text titleText;

    @FXML
    private Button judgeButton;

    @FXML
    private Text authorText;
    @FXML
    private Text idText;
    @FXML
    private Label Bookid;

    private static final int Buy = 2;
    private static final int Borrow = 1;
    private static  int status = 0;
    private String email = "23021524@vnu.edu.vn";

    private Scene Forwardsceen;

    public void saveForwardScene(Scene scene) {
        this.Forwardsceen = scene;
    }

    public void setAuthorText(String author) {
        authorText.setText(author);
    }

    public void setDescriptionText(String description) {
        descriptionText.setText(description);
    }

    public void setTitleText(String title) {
        titleText.setText(title);
    }

    public void setThumbnail(Image image) {
        thumbnail.setImage(image);
    }



    public void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage)BackButton.getScene().getWindow();

        Scene scene = Forwardsceen;
        stage.setScene(scene);
    }

    public void setInformation(String searchText) throws IOException {
        GoogleBooksAPI gg = new GoogleBooksAPI();
        JsonArray items = gg.getInformation(searchText, 4);
        //JsonArray items = null;

        if (items == null || items.isEmpty()) {
            System.err.println("No items found in the API response.");
            authorText.setText("No author");
            descriptionText.setText("No description available.");
            titleText.setText("No title available.");
            return;
        }

        JsonObject item = items.get(0).getAsJsonObject();
        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

        if(volumeInfo.has("imageLinks")) {
            String thumbnailLink = volumeInfo.getAsJsonObject("imageLinks").get("smallThumbnail").getAsString();
            System.err.println("ThumbnailLink" + thumbnailLink);
            thumbnail.setImage(new Image(thumbnailLink)); // thay lai = thumbnailLink
        } else {
            // thay thế lại absolute path để chạy được
            thumbnail.setImage(new Image("/org/assets/noImage.png"));
        }

        if (volumeInfo.has("authors")) {
            JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
            StringBuilder authorsBuilder = new StringBuilder();
            for (int j = 0; j < authorsArray.size(); j++) {
                authorsBuilder.append(authorsArray.get(j).getAsString());
                if (j < authorsArray.size() - 1) {
                    authorsBuilder.append(", ");
                }
            }
            authorText.setText(authorsBuilder.toString());
        } else {
            authorText.setText("No author");
        }

        if (volumeInfo.has("description")) {
            String description = volumeInfo.get("description").getAsString();
            if (description.length() <= 120) {
                descriptionText.setText(volumeInfo.get("description").getAsString());
            } else {
                descriptionText.setText(description.substring(0, 119) + "...");
            }
        } else {
            descriptionText.setText("No description available.");
        }

        if (volumeInfo.has("title")) {
            titleText.setText(volumeInfo.get("title").getAsString());
        } else {
            titleText.setText("No title available.");
        }
    }

    public void addBookToCart(String email, String bookId, int quantity, int status) throws SQLException {
        String queryCheck = "SELECT quantity, type FROM cart WHERE email = ? AND bookId = ?";
        String queryInsert = "INSERT INTO cart (bookID, email, date, quantity,type) VALUES (?, ?, CURRENT_DATE, ?,?)";
        DBConnection dbConnection = DBConnection.getInstance();
        try (Connection connection = dbConnection.getConnection()) {
            // Kiểm tra nếu sách đã có trong giỏ hàng
            try (PreparedStatement stmtCheck = connection.prepareStatement(queryCheck)) {
                stmtCheck.setString(1, bookId);
                stmtCheck.setString(2, email);
                ResultSet rs = stmtCheck.executeQuery();
                String statusBook = (status == Borrow) ? "BORROW" :"BUY";
                if (rs.next()) {
                    String currentStatus = rs.getString("type");
                    // Kiểm tra nếu trạng thái khác, có thể xử lý logic tùy ý

                    // Nếu sách đã có, cập nhật số lượng và trạng thái
                    if(currentStatus.equals(statusBook)) {
                        showErrAndEx.showAlert("Sách đã được thêm vào giỏ hàng");
                    } else {
                        try (PreparedStatement stmtInsert = connection.prepareStatement(queryInsert)) {
                            stmtInsert.setString(1, bookId);
                            stmtInsert.setString(2, email);

                            stmtInsert.setInt(3, quantity);
                            stmtInsert.setString(4, statusBook);
                            stmtInsert.executeUpdate();
                        }
                    }
                } else {
                    // Nếu sách chưa có, thêm mới
                    try (PreparedStatement stmtInsert = connection.prepareStatement(queryInsert)) {
                        stmtInsert.setString(1, bookId);
                        stmtInsert.setString(2, email);
                        stmtInsert.setInt(3, quantity);
                        stmtInsert.setString(4, statusBook);
                        stmtInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addCart(MouseEvent event) {
            String num = VolumeTextField.getText().trim();
            if (num.isEmpty()) {
                showErrAndEx.showAlert("Vui lòng nhập số lượng!");
                return;
            }
            if(status == 0) {
                showErrAndEx.showAlert("Vui lòng kiểm tra trang thái");
                return;
            }
            int quantity = Integer.parseInt(VolumeTextField.getText());
            if(quantity > 0){
              //int bookId = Integer.parseInt(Bookid.getText());
              String bookId = "12";
              try{
                  System.out.println(quantity);
                  addBookToCart(email,bookId,quantity,status);
                  showErrAndEx.showAlert("Đã thêm sách vào giỏ hàng");
              } catch (SQLException e) {
                  e.printStackTrace();
              }

            }
    }
    @FXML
    void BorrowClick(MouseEvent event) {
            status = Borrow;
    }

    @FXML
    void PurchaseClick(MouseEvent event) {
        status = Buy;
    }



}