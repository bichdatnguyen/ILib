package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerBookDetail {
    @FXML
    private Button BackButton;

    @FXML
    private Text authorText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text titleText;

    @FXML
    private ImageView thumbnail;



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

    public static JsonArray bookDetails = new JsonArray();

    public void Back(MouseEvent event) throws IOException {
        Stage stage = (Stage)BackButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    public void setInformation(String searchText) throws IOException {
        GoogleBooksAPI gg = new GoogleBooksAPI();
        JsonArray items = gg.getInformation(searchText);
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
            thumbnail.setImage(new Image("D:\\GitHub\\Ilib\\Ilib\\src\\main\\resources\\org\\assets\\noImage.png"));
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



}