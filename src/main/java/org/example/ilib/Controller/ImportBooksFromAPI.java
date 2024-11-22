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
            = {"Philosophy", "Psychology", "Sociology", "Mathematics", "Physics", "Chemistry",
            "Biology", "Technology & Engineering", "Medicine", "Law", "Arts & Photography",
            "Language Arts & Disciplines"};

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
        for (int i = 0; i < authors.size(); i++) {
            String author = authors.get(i);
            authorInfo.append(author);
            if (i < authors.size() - 1) {
                authorInfo.append(", ");
            }
        }
        return authorInfo.toString();
    }

    public static int getBookPrice(JsonObject saleInfo) {
        return saleInfo.getAsJsonObject("listPrice").get("amount").getAsInt();
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
            JsonArray items = gg.getBooksBySubject(subject, 40);

            for (JsonElement item : items) {
                JsonObject saleInfo = item.getAsJsonObject().get("saleInfo").getAsJsonObject();
                String saleability = saleInfo.get("saleability").getAsString();
                if (saleability.equals("FOR_SALE")) {
                    String bookID = getID(item);
                    JsonObject volumeInfo = item.getAsJsonObject().get("volumeInfo").getAsJsonObject();
                    String title = getTitle(volumeInfo);
                    List<String> authors = getAuthors(volumeInfo);
                    String authorInfo = getAuthorInfo(authors);
                    String description = getDescription(volumeInfo);
                    int bookPrice = getBookPrice(saleInfo);

                    db.addBook(bookID, title, authorInfo, bookPrice, description, 50);
                    for (String author : authors) {
                        db.addAuthor(bookID, author);
                    }
                    db.addCategories(subject, bookID);
                } else {
                    continue;
                }
            }
        }
    }
}
