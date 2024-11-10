package org.example.ilib;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.getenv;

public class GoogleBooksAPI {

    private String query;

    GoogleBooksAPI(String query) {
        this.query = query;
    }

    public String information() {
        String apiKey = getenv("APIKey");

        try {
            String encodedQuery = URLEncoder.encode(this.query, StandardCharsets.UTF_8);
            String urlString = "https://www.googleapis.com/books/v1/volumes?q="
                    + encodedQuery + "&maxResults=1&key=" + apiKey;
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

                if (items != null && !items.isEmpty()) {
                    for (int i = 0; i < items.size(); i++) {
                        JsonObject item = items.get(i).getAsJsonObject();

                        // Get the volume ID
                        String bookID = item.has("id") ? item.get("id").getAsString() : "No ID";

                        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                        // Get title
                        String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No title";

                        // Get authors (array)
                        String authors = "No authors";
                        if (volumeInfo.has("authors")) {
                            JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
                            StringBuilder authorsBuilder = new StringBuilder();
                            for (int j = 0; j < authorsArray.size(); j++) {
                                authorsBuilder.append(authorsArray.get(j).getAsString());
                                if (j < authorsArray.size() - 1) {
                                    authorsBuilder.append(", ");
                                }
                            }
                            authors = authorsBuilder.toString();
                        }

                        // Get description
                        String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description";

                        // Get page count
                        String pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsString() : "No page count";

                        String smallThumbnail = "No small thumbnail";
                        String thumbnail = "No thumbnail";
                        if(volumeInfo.has("imageLinks")) {
                            JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                            smallThumbnail = imageLinks.has("smallThumbnail") ? imageLinks.get("smallThumbnail").getAsString() : "No small thumbnail";
                            thumbnail = imageLinks.has("thumbnail") ? imageLinks.get("thumbnail").getAsString() : "No thumbnail";
                        }

                        // Print details
                        System.out.println("Title: " + title);
                        System.out.println("Authors: " + authors);
                        System.out.println("Description: " + description);
                        System.out.println("Book ID: " + bookID);
                        System.out.println("Page Count: " + pageCount);
                        System.out.println("Small Thumbnail: " + smallThumbnail);
                        System.out.println("Thumbnail: " + thumbnail);
                        System.out.println(" ");
                        return smallThumbnail;
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
        return null;
    }
}