package org.example.ilib.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.ilib.book.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getenv;

public class GoogleBooksAPI { 
    private static final String apiKey = getenv("APIKey");

    /** check connection and get books
     * @param urlString url string
     * @return JsonArray of books
     * @throws IOException prevent IO exception
     */
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

    /** check connection and get book.
     * @param urlString url string
     * @return book
     * @throws IOException prevent IO exception
     */
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

            return JsonParser.parseString(content.toString()).getAsJsonObject();
        } else {
            System.out.println("No books found.");
            return null;
        }
    }

    /** get JsonArray base on key word.
     * @param query user's key word
     * @param maxresult number of books want to find (max is 40)
     * @return JsonArray of books
     * @throws IOException prevent IO exception
     */
    public JsonArray getInformation(String query, int maxresult) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/books/v1/volumes?q="
                + encodedQuery + "&maxResults=" + maxresult + "&key=" + apiKey;
        return checkConnectionAndGetBooks(urlString);
    }

    /** get books by subject.
     * @param subject subject want to find
     * @param maxresult number of books want to find (max is 40)
     * @return JsonArray of books
     * @throws IOException prevent IO exception
     */
    public JsonArray getBooksBySubject(String subject, int maxresult) throws IOException {
        String encodedQuery = URLEncoder.encode(subject, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=subject:"
                + encodedQuery + "&maxResults=" + maxresult + "&key=" + apiKey;

        return checkConnectionAndGetBooks(urlString);
    }

    /** get book's title.
     * @param volumeInfo volumeInfo in book's information
     * @return book's title
     */
    public static String getTitle(JsonObject volumeInfo) {
        if (volumeInfo.has("title")) {
            return volumeInfo.get("title").getAsString();
        } else {
            return "N/A";
        }
    }

    /** get all book's author.
     * @param volumeInfo volumeInfo in book's information
     * @return list of book's author
     */
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

    /** rewrite authors in string.
     * @param authors list of authors.
     * @return authors's name
     */
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

    /** get book description.
     * @param volumeInfo volumeInfo in book's information
     * @return book's description
     */
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

    /** get book thumbnail.
     * @param volumeInfo volumeInfo in book's information
     * @return book's thumbnail
     */
    public static String getImage(JsonObject volumeInfo) {
        if (volumeInfo.has("imageLinks")) {
            return volumeInfo.getAsJsonObject("imageLinks").get("smallThumbnail").getAsString();
        } else {
            return "/org/assets/noImage.png";
        }
    }

    /** get books by ID using API.
     * @param id id want to find
     * @return book which have this id
     * @throws IOException prevent IO exception
     * @throws SQLException prevent SQL exception
     */
    public Book getBooksByID(String id) throws IOException, SQLException {
        String urlString = "https://www.googleapis.com/books/v1/volumes/"
                + id + "?key=" + apiKey;

        JsonObject item = checkConnectionAndGetBookByID(urlString);
        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

        String image = getImage(volumeInfo);
        String title = getTitle(volumeInfo);
        String author = getAuthors(getAuthorInList(volumeInfo));
        String description = getDescription(volumeInfo);

        return new Book(image, title, author, description, id, 0);
    }
}