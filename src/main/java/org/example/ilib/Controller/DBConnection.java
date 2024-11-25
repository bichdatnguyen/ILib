package org.example.ilib.Controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.collections.ObservableList;
import org.example.ilib.Processor.CartItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static java.lang.System.getenv;

// Singleton design pattern
public class DBConnection {
    private static DBConnection instance;
    private HikariDataSource dataSource;
    private final String url = "jdbc:mysql://localhost:3306/ilib?autoReconnect=true&useSSL=false";
    private final String userName = System.getenv("userName");
    private final String userPassword = System.getenv("userPassword");

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private DBConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(userPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }
    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public static PreparedStatement createStatement(String sql) throws SQLException {
        return DBConnection.getInstance().getConnection().prepareStatement(sql);
    }

    public void createAccount(String email, String phoneNumber, String fullName,
                              String password, String avatarPath, String role) throws SQLException {
        String sql = "INSERT INTO user (email, phoneNumber, fullName, password, avatarPath, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, phoneNumber);
        stmt.setString(3, fullName);
        stmt.setString(4, password);
        stmt.setString(5, avatarPath);
        stmt.setString(6, role);
        stmt.executeUpdate();
    }

    public void createVoucher(String email, int discountPercentage) throws SQLException {
        String sql = "INSERT INTO Voucher (Email, discountPercentage) VALUES (?, ?)";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, email);
        stmt.setInt(2, discountPercentage);
        stmt.executeUpdate();
    }

    public boolean checkDataExit(String email_)  {
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";

        try (Connection connection1 = DriverManager.getConnection(url, userName, userPassword);
             PreparedStatement preparedStatement = connection1.prepareStatement(query)) {

            preparedStatement.setString(1, email_); // Gán giá trị cho dấu ? trong câu lệnh SQL

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Lấy giá trị COUNT từ kết quả
                return count > 0;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
        }
        return false;
    }


    public boolean checkDataExit(String email_, String password_) {
        String query = "SELECT COUNT(*) FROM User WHERE email = ? AND password = ?";

        try (Connection connection1 = DriverManager.getConnection(url, userName, userPassword);
             PreparedStatement preparedStatement = connection1.prepareStatement(query)) {

            preparedStatement.setString(1, email_); // Gán giá trị cho dấu ? trong câu lệnh SQL
            preparedStatement.setString(2, password_);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Lấy giá trị COUNT từ kết quả
                return count > 0;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
        }
        return false;
    }

    public boolean bookExist(String bookID) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM books WHERE bookID = ?)";
        try (PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean authorPrimaryKeyExist(String bookID, String author) throws SQLException {
        String sql = "SELECT COUNT(*) FROM author WHERE bookID = ? AND authorName = ?";

        try (PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, bookID);
            stmt.setString(2, author);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean categoryPrimaryKeyExist(String category, String bookID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE bookID = ? AND Category = ?";

        try (PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, bookID);
            stmt.setString(2, category);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void addAuthor(String bookID, String author) throws SQLException {
        if (authorPrimaryKeyExist(bookID, author)) {
            return;
        }
        String sql = "INSERT INTO author (bookID, authorName) VALUES (?, ?)";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, author);
        stmt.executeUpdate();
    }

    public void addBook(String bookID, String title, int bookPrice, int quantityInStock,
                        Timestamp addDate, double averageRating) throws SQLException {
        if (bookExist(bookID)) {
            return;
        }
        String sql = "INSERT INTO books (bookID, title, bookPrice, quantityInStock, addDate, averageRating) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, title);
        stmt.setInt(3, bookPrice);
        stmt.setInt(4, quantityInStock);
        stmt.setTimestamp(5, addDate);
        stmt.setDouble(6, averageRating);
        stmt.executeUpdate();
    }

    public void addCategories(String category, String bookID) throws SQLException {
        if (categoryPrimaryKeyExist(category, bookID)) {
            return;
        }
        String sql = "INSERT INTO categories (category, bookID) VALUES (?, ?)";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, category);
        stmt.setString(2, bookID);
        stmt.executeUpdate();
    }

    public int getQuantity(String bookID) throws SQLException {
        String sql = "SELECT quantityInStock FROM books WHERE bookID = ?";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, bookID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int quantity = rs.getInt(1);
            return quantity;
        } else {
            return 0;
        }
    }

    public List<String> getTopBooks(int number) throws SQLException {
        String sql = "SELECT bookID FROM books ORDER BY averageRating DESC LIMIT ?";
        PreparedStatement stmt = createStatement(sql);
        stmt.setInt(1, number);

        ResultSet rs = stmt.executeQuery();
        List<String> ids = new ArrayList<>();

        while (rs.next()) {
            String bookID = rs.getString(1);
            ids.add(rs.getString(1));
        }
        return ids;
    }

    public List<String> getRecentlyBooks(int number) throws SQLException {
        String sql = "SELECT bookID FROM books ORDER BY addDate DESC LIMIT ?";
        PreparedStatement stmt = createStatement(sql);
        stmt.setInt(1, number);

        ResultSet rs = stmt.executeQuery();
        List<String> ids = new ArrayList<>();

        while (rs.next()) {
            String bookID = rs.getString(1);
            ids.add(bookID);
        }
        return ids;
    }
}