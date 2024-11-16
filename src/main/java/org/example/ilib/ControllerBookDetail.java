package org.example.ilib;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

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
            thumbnail.setImage(new Image("E:\\java\\iLib\\ILib\\src\\main\\resources\\org\\assets\\noImage.png"));
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
            if (description.length() <= 100) {
                descriptionText.setText(volumeInfo.get("description").getAsString());
            } else {
                descriptionText.setText(description.substring(0, 120) + "...");
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
