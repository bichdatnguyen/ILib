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
    private Text authorText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text titleText;

    @FXML
    private ImageView thumbnail;
    @FXML
    private Button Back;


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

    public void setInformation() throws IOException {
        GoogleBooksAPI gg = new GoogleBooksAPI("Potter");
        JsonArray items = gg.getInformation();

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
            thumbnail.setImage(new Image(thumbnailLink));
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

    @FXML
    void BacktoMenu(MouseEvent event) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/org/example/ilib/Menu.fxml"));
        Scene scene = new Scene(fx.load());
        stage.setScene(scene);

    }

}