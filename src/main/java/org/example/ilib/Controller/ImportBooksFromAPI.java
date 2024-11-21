package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportBooksFromAPI {
    private static String[] subjects
            = {"Philosophy", "Psychology", "Sociology", "Mathematics", "Physics", "Chemistry", "Biology",
            "Technology & Engineering", "Medicine", "Law", "Political Science", "Computer Science",
            "Arts & Photography", "Language Arts & Disciplines", "Literary Criticism"};

    public static String getID(JsonElement item) {
        return item.getAsJsonObject().get("id").getAsString();
    }

    public static String getTitle(JsonObject volumeInfo) {
        if (volumeInfo.has("title")) {
            return volumeInfo.get("title").getAsString();
        } else {
            return "N/A";
        }
    }

    public static List<String> getAuthors(JsonObject volumeInfo) {
        List<String> authors = new ArrayList<>();
        if (volumeInfo.has("authors")) {
            JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
            for (int j = 0; j < authorsArray.size(); j++) {
                authors.add(authorsArray.get(j).getAsString());
            }
        }
        return authors;
    }

    public static String getAuthorInfo(List<String> authors) {
        StringBuilder authorInfo = new StringBuilder();
        for (String author : authors) {
            authorInfo.append(author).append(", ");
        }
        return authorInfo.toString();
    }

    public static String getDescription(JsonObject volumeInfo) {
        if (volumeInfo.has("description")) {
            return volumeInfo.get("description").getAsString();
        } else {
            return "N/A";
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        GoogleBooksAPI gg = new GoogleBooksAPI();
        DBConnection db = DBConnection.getInstance();

        for (String subject : subjects) {
            JsonArray items = gg.getBooksBySubject(subject, 20);

            for (JsonElement item : items) {
                String bookID = getID(item);
                JsonObject volumeInfo = item.getAsJsonObject().get("volumeInfo").getAsJsonObject();
                String title = getTitle(volumeInfo);
                List<String> authors = getAuthors(volumeInfo);
                String authorInfo = getAuthorInfo(authors);
                String description = getDescription(volumeInfo);
                JsonObject saleInfo = item.getAsJsonObject().get("saleInfo").getAsJsonObject();

            }
        }
    }
}
