package org.example.ilib;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleBooksAPI {

    private String query;

    GoogleBooksAPI(String query) {
        this.query = query;
    }

    public void information() {
        String apiKey = "AIzaSyDEVPzzznqYZIZHikDUXZh20NutLJswsVo";

        try {
            String encodedQuery = URLEncoder.encode(this.query, StandardCharsets.UTF_8);
            String urlString = "https://www.googleapis.com/books/v1/volumes?q="
                    + encodedQuery + "&maxResults=40&key=" + apiKey;
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                conn.disconnect();

                // Parse JSON response with Gson
                JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonArray("items");

                if (items != null && items.size() > 0) {
                    for (int i=0; i<items.size(); i++) {
                        JsonObject firstBook = items.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");

                        String title = firstBook.has("title")
                                ? firstBook.get("title").getAsString()
                                : "No Title Available";
                        String authors = firstBook.has("authors")
                                ? firstBook.getAsJsonArray("authors").toString()
                                : "No Authors Available";
                        String description = firstBook.has("description")
                                ? firstBook.get("description").getAsString()
                                : "No Description Available";

                        System.out.println("Title: " + title);
                        System.out.println("Authors: " + authors);
                        System.out.println("Description: " + description);
                        System.out.println(" ");
                    }
                } else {
                    System.out.println("No books found.");
                }
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}