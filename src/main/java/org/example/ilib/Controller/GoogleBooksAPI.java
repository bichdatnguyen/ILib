package org.example.ilib.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.System.getenv;

public class GoogleBooksAPI {

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

    public JsonArray getInformation(String query) throws IOException {
        String apiKey = getenv("APIKey");

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/books/v1/volumes?q="
                + encodedQuery + "&maxResults=1&key=" + apiKey;
        return checkConnectionAndGetBooks(urlString);
    }

    public JsonArray getBooksBySubject(String subject) throws IOException {
        String encodedQuery = URLEncoder.encode(subject, StandardCharsets.UTF_8);
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=subject:"
                + encodedQuery;

        return checkConnectionAndGetBooks(urlString);
    }
}