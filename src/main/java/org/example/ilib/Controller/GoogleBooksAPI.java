package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import org.example.ilib.Processor.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getenv;

public class GoogleBooksAPI { 
    private static final String apiKey = getenv("APIKey");

    public JsonArray checkConnectionAndGetBooks(String urlString) throws IOException {
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

            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();

            return jsonResponse.getAsJsonArray("items");
        } else {
            System.out.println("No books found.");
            return null;
        }
    }

    public JsonObject checkConnectionAndGetBookByID(String urlString) throws IOException {
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

            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();

            return jsonResponse;
        } else {
            System.out.println("No books found.");
            return null;
        }
    }

    public JsonArray getInformation(String query, int maxresult) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/books/v1/volumes?q="
                + encodedQuery + "&maxResults=" + maxresult + "&key=" + apiKey;
        return checkConnectionAndGetBooks(urlString);
    }

    public JsonArray getBooksBySubject(String subject, int maxresult) throws IOException {
        String encodedQuery = URLEncoder.encode(subject, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=subject:"
                + encodedQuery + "&maxResults=" + maxresult + "&key=" + apiKey;

        return checkConnectionAndGetBooks(urlString);
    }

    public static String getTitle(JsonObject volumeInfo) {
        if (volumeInfo.has("title")) {
            return volumeInfo.get("title").getAsString();
        } else {
            return "N/A";
        }
    }

    public static List<String> getAuthorInList(JsonObject volumeInfo) {
        List<String> authors = new ArrayList<>();
        if (volumeInfo.has("authors")) {
            JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
            for (int j = 0; j < authorsArray.size(); j++) {
                authors.add(authorsArray.get(j).getAsString());
            }
        }
        return authors;
    }

    public static String getAuthors(List<String> authors) {
        StringBuilder authorsString = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            authorsString.append(authors.get(i));
            if (i < authors.size() - 1) {
                authorsString.append(", ");
            }
        }
        return authorsString.toString();
    }

    public static String getDescription(JsonObject volumeInfo) {
        if (volumeInfo.has("description")) {
            String description = volumeInfo.get("description").getAsString();
            if (description.length() <= 450) {
                return description;
            }
            return description.substring(0, 447) + "...";
        } else {
            return "N/A";
        }
    }

    public static String getImage(JsonObject volumeInfo) {
        if (volumeInfo.has("imageLinks")) {
            return volumeInfo.getAsJsonObject("imageLinks").get("smallThumbnail").getAsString();
        } else {
            return "/org/assets/noImage.png";
        }
    }

    public Book getBooksByID(String id) throws IOException {
        String urlString = "https://www.googleapis.com/books/v1/volumes/"
                + id + "?key=" + apiKey;

        JsonObject item = checkConnectionAndGetBookByID(urlString);
        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

        String image = getImage(volumeInfo);
        String title = getTitle(volumeInfo);
        String author = getAuthors(getAuthorInList(volumeInfo));
        String description = getDescription(volumeInfo);
        return new Book(image, title, author, description, id);
    }
}