package org.example.ilib;

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
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getenv;

public class GoogleBooksAPI {

    private String query;

    GoogleBooksAPI(String query) {
        this.query = query;
    }

    public JsonArray getInformation() throws IOException {
        String apiKey = getenv("APIKey");

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

            JsonObject jsonResponse = JsonParser.parseString(content.toString()).getAsJsonObject();

            return jsonResponse.getAsJsonArray("items");
        } else {
            System.out.println("No books found.");
            return null;
        }
    }
}