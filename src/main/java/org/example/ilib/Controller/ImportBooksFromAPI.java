package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
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

    public static int getBookPrice(JsonObject saleInfo) {
        if (saleInfo.has("listPrice")) {
            return saleInfo.getAsJsonObject("listPrice").get("amount").getAsInt();
        } else {
            return 0;
        }
    }

    public static String getDescription(JsonObject volumeInfo) {
        if (volumeInfo.has("description")) {
            return volumeInfo.get("description").getAsString();
        } else {
            return "N/A";
        }
    }

    public static Timestamp getDate() {
        LocalDateTime now = LocalDateTime.now();
        return Timestamp.valueOf(now);
    }

    public static double getAverageRating(JsonObject volumeInfo) {
        if (volumeInfo.has("averageRating")) {
            return volumeInfo.get("averageRating").getAsDouble();
        } else {
            return 0.0;
        }
    }

    public static List<String> getCategory(JsonObject volumeInfo) {
        List<String> categories = new ArrayList<>();
        if (volumeInfo.has("categories")) {
            JsonArray categoryArray = volumeInfo.getAsJsonArray("categories");
            for (int j = 0; j < categoryArray.size(); j++) {
                categories.add(categoryArray.get(j).getAsString());
            }
        }
        return categories;
    }

    public static void main(String[] args) throws SQLException, IOException {
        GoogleBooksAPI gg = new GoogleBooksAPI();
        DBConnection db = DBConnection.getInstance();

        // Chuẩn bị batch để thêm sách, tác giả và danh mục
        String insertBookQuery = "INSERT INTO books "
                + "(bookID, title, bookPrice, quantityInStock, addDate, averageRating) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String insertAuthorQuery = "INSERT INTO author (bookID, authorName) VALUES (?, ?)";
        String insertCategoryQuery = "INSERT INTO categories (Category, bookID) VALUES (?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement bookStmt = connection.prepareStatement(insertBookQuery);
             PreparedStatement authorStmt = connection.prepareStatement(insertAuthorQuery);
             PreparedStatement categoryStmt = connection.prepareStatement(insertCategoryQuery)) {

            // Bắt đầu xử lý API
            for (String subject : subjects) {
                JsonArray items = gg.getBooksBySubject(subject, 20);

                for (JsonElement item : items) {
                    JsonObject saleInfo = item.getAsJsonObject().get("saleInfo").getAsJsonObject();
                    String saleability = saleInfo.get("saleability").getAsString();
                    if (!saleability.equals("FOR_SALE")) {
                        continue; // Bỏ qua sách không có giá
                    }

                    String bookID = getID(item);
                    // Thêm sách vào batch
                    if (db.bookExist(bookID)) {
                        continue;
                    }
                    JsonObject volumeInfo = item.getAsJsonObject().get("volumeInfo").getAsJsonObject();
                    String title = getTitle(volumeInfo);
                    List<String> authors = getAuthors(volumeInfo);
                    int bookPrice = getBookPrice(saleInfo);
                    Timestamp addDate = getDate();
                    double averageRating = getAverageRating(volumeInfo);
                    List<String> categories = getCategory(volumeInfo);

                    bookStmt.setString(1, bookID);
                    bookStmt.setString(2, title);
                    bookStmt.setInt(3, bookPrice);
                    bookStmt.setInt(4, 50); // Số lượng mặc định
                    bookStmt.setTimestamp(5, addDate);
                    bookStmt.setDouble(6, averageRating);
                    bookStmt.addBatch();

                    // Thêm tác giả vào batch
                    for (String author : authors) {
                        authorStmt.setString(1, bookID);
                        authorStmt.setString(2, author);
                        authorStmt.addBatch();
                    }

                    // Thêm danh mục vào batch
                    for (String category : categories) {
                        categoryStmt.setString(1, category);
                        categoryStmt.setString(2, bookID);
                        categoryStmt.addBatch();
                    }
                }
            }

            try {
                // Thực hiện thêm sách
                bookStmt.executeBatch();

                // Thực hiện thêm tác giả
                authorStmt.executeBatch();

                // Thực hiện thêm danh mục
                categoryStmt.executeBatch();

                System.out.println("Thêm sách, tác giả và danh mục thành công!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Thêm sách từ API thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}