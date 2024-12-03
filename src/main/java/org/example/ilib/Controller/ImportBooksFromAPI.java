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
import java.util.Objects;

public class ImportBooksFromAPI {
    private static String[] subjects
            = {"Philosophy", "Psychology", "Sociology", "Mathematics", "Physics"};

    /** get book ID.
     * @param item item which contain book ID
     * @return book ID
     */
    public static String getID(JsonElement item) {
        return item.getAsJsonObject().get("id").getAsString();
    }

    /** get title
     * @param volumeInfo volumeInfo which contain book's title
     * @return book's title
     */
    public static String getTitle(JsonObject volumeInfo) {
        if (volumeInfo.has("title")) {
            return volumeInfo.get("title").getAsString();
        } else {
            return "N/A";
        }
    }

    /** get a list of authors.
     * @param volumeInfo volumeInfo which contain book's title
     * @return list of book's authors
     */
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

    /** get book price.
     * @param saleInfo saleInfo which contain book's price
     * @return book's price
     */
    public static int getBookPrice(JsonObject saleInfo) {
        String saleability = saleInfo.get("saleability").getAsString();
        if (!saleability.equals("FOR_SALE")) {
            return 10000;
        }

        if (saleInfo.has("listPrice")) {
            return saleInfo.getAsJsonObject("listPrice").get("amount").getAsInt();
        } else {
            return 10000;
        }
    }

    /** get book's description
     * @param volumeInfo volumeInfo which contain book's title
     * @return book's description
     */
    public static String getDescription(JsonObject volumeInfo) {
        if (volumeInfo.has("description")) {
            return volumeInfo.get("description").getAsString();
        } else {
            return "N/A";
        }
    }

    /** get real time date
     * @return real time
     */
    public static Timestamp getDate() {
        LocalDateTime now = LocalDateTime.now();
        return Timestamp.valueOf(now);
    }

    /** get book's rating.
     * @param volumeInfo volumeInfo which contain book's title
     * @return book's average rating
     */
    public static double getAverageRating(JsonObject volumeInfo) {
        if (volumeInfo.has("averageRating")) {
            return volumeInfo.get("averageRating").getAsDouble();
        } else {
            return 0.0;
        }
    }

    /** list of book's categories
     * @param volumeInfo volumeInfo which contain book's title
     * @return book's categories
     */
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

    /** book's thumbnail.
     * @param volumeInfo volumeInfo which contain book's title
     * @return book's thumbnal url
     */
    public static String getThumbnail(JsonObject volumeInfo) {
        if (volumeInfo.has("imageLinks")) {
            JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
            if (imageLinks.has("thumbnail")) {
                return imageLinks.get("thumbnail").getAsString();
            }
        }
        return "/org/assets/noImage.png";
    }

    /** take books from API to test.
     * @param args args
     * @throws SQLException prevent SQL exception
     * @throws IOException prevent IO exception
     */
    public static void main(String[] args) throws SQLException, IOException {
        GoogleBooksAPI gg = new GoogleBooksAPI();
        DBConnection db = DBConnection.getInstance();

        // Chuẩn bị batch để thêm sách, tác giả và danh mục
        String insertBookQuery = "INSERT INTO books "
                + "(bookID, thumbnail, description, title, bookPrice, averageRating, quantityInStock, addDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertAuthorQuery = "INSERT INTO author (bookID, authorName) VALUES (?, ?)";
        String insertCategoryQuery = "INSERT INTO categories (Category, bookID) VALUES (?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement bookStmt = connection.prepareStatement(insertBookQuery);
             PreparedStatement authorStmt = connection.prepareStatement(insertAuthorQuery);
             PreparedStatement categoryStmt = connection.prepareStatement(insertCategoryQuery)) {

            // Bắt đầu xử lý API
            for (String subject : subjects) {
                JsonArray items;
                if (subject.equals("Philosophy")) {
                    items = gg.getBooksBySubject(subject, 20);
                } else {
                    items = gg.getBooksBySubject(subject, 5);
                }

                for (JsonElement item : items) {
                    JsonObject saleInfo = item.getAsJsonObject().get("saleInfo").getAsJsonObject();
                    int bookPrice = getBookPrice(saleInfo);

                    String bookID = getID(item);
                    // Thêm sách vào batch
                    if (db.bookExist(bookID) || bookID.equals("9GK_Fhz5RDUC") || bookID.equals("N4zH86WogYwC")) {
                        continue;
                    }
                    JsonObject volumeInfo = item.getAsJsonObject().get("volumeInfo").getAsJsonObject();
                    String title = getTitle(volumeInfo);
                    List<String> authors = getAuthors(volumeInfo);
                    Timestamp addDate = getDate();
                    double averageRating = getAverageRating(volumeInfo);
                    List<String> categories = getCategory(volumeInfo);
                    String thumbnail = getThumbnail(volumeInfo);
                    String description = getDescription(volumeInfo);

                    bookStmt.setString(1, bookID);
                    bookStmt.setString(2, thumbnail);
                    bookStmt.setString(3, description);
                    bookStmt.setString(4, title);
                    bookStmt.setInt(5, bookPrice);
                    bookStmt.setDouble(6, averageRating);
                    bookStmt.setInt(7, 50); // Số lượng mặc định
                    bookStmt.setTimestamp(8, addDate);
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