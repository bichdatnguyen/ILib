package org.example.ilib.Processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.getenv;

public class BookAPI {
    public static void main(String[] args) {
        try {
            String query = "Learn_C++_in_24_Hours"; // Từ khóa tìm kiếm
            String apiKey = getenv("APIKey");

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String urlString = "https://www.googleapis.com/books/v1/volumes?q="
                    + encodedQuery + "&maxResults=1&key=" + apiKey;

            // Gửi yêu cầu HTTP GET đến Google Books API
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Đọc phản hồi từ API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Phân tích dữ liệu JSON bằng Gson
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            System.out.println("Total Items: " + jsonResponse.get("totalItems").getAsInt());

            // Lấy thông tin sách (tối đa 10 cuốn)
            if (jsonResponse.get("totalItems").getAsInt() > 0) {
                JsonArray items = jsonResponse.getAsJsonArray("items");
                for (int i = 0; i < items.size(); i++) {
                    JsonObject book = items.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");
                    System.out.println("Title: " + book.get("title").getAsString());
                    System.out.println("Authors: " + book.getAsJsonArray("authors").toString());
                    System.out.println("Description: " + book.get("description").getAsString());

                    // Lấy đường dẫn ảnh bìa (thumbnail)
                    if (book.has("imageLinks")) {
                        JsonObject imageLinks = book.getAsJsonObject("imageLinks");
                        String thumbnailUrl = imageLinks.has("thumbnail") ? imageLinks.get("thumbnail").getAsString() : "No thumbnail available";
                        System.out.println("Thumbnail URL: " + thumbnailUrl);
                    }
                    System.out.println("------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // In ra lỗi nếu xảy ra
        }
    }
}
