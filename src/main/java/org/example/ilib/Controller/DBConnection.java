package org.example.ilib.Controller;

import javafx.collections.ObservableList;
import org.example.ilib.Processor.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static java.lang.System.getenv;

// Singleton design pattern
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/ilib";
    private String userName = getenv("userName");
    private String userPassword = getenv("userPassword");

    private DBConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, userName, userPassword);
    }
    public Connection getConnection() {
        return connection;
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

    public void createAccount(String email, String phoneNumber, String fullName,
                              String password) throws SQLException {
        String sql = "INSERT INTO user (email, phoneNumber, fullName, password) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, phoneNumber);
        stmt.setString(3, fullName);
        stmt.setString(4, password);
        stmt.executeUpdate();
    }

    public void createVoucher(String email, int discountPercentage) throws SQLException {
        String sql = "INSERT INTO Voucher (Email, discountPercentage) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
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
        String sql = "SELECT COUNT(*) FROM books WHERE bookID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, bookID);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        } else {
            return false;
        }
    }

    public boolean bookAndAuthorExist(String bookID, String author) throws SQLException {
        String sql = "SELECT COUNT(*) FROM author WHERE bookID = ? AND authorName = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, author);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        } else {
            return false;
        }
    }

    public boolean bookAndCategoryExist(String category, String bookID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE bookID = ? AND Category = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, category);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        } else {
            return false;
        }
    }

    public void addAuthor(String bookID, String author) throws SQLException {
        if (bookAndAuthorExist(bookID, author)) {
            return;
        }
        String sql = "INSERT INTO author (bookID, authorName) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, author);
        stmt.executeUpdate();
    }

    public void addBook(String bookID, String title, int bookPrice, int quantityInStock) throws SQLException {
        if (bookExist(bookID)) {
            return;
        }
        String sql = "INSERT INTO books (bookID, title, bookPrice, quantityInStock) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, title);
        stmt.setInt(3, bookPrice);
        stmt.setInt(4, quantityInStock);
        stmt.executeUpdate();
    }

    public void addCategories(String category, String bookID) throws SQLException {
        if (bookAndCategoryExist(category, bookID)) {
            return;
        }
        String sql = "INSERT INTO categories (category, bookID) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, category);
        stmt.setString(2, bookID);
        stmt.executeUpdate();
    }


}