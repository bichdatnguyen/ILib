package org.example.ilib.Controller;

import java.sql.*;
import java.util.List;

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

    public void addAuthor(String bookID, List<String> authors) throws SQLException {
        for (String author : authors) {
            String sql = "INSERT INTO author (bookID, authorName) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, bookID);
            stmt.setString(2, author);
            stmt.executeUpdate();
        }
    }

    public void addBook(String bookID, String title, String authors, int bookPrice,
                        String description, String quantityInStock) throws SQLException {
        String sql = "INSERT INTO books (bookID, title, authors, bookPrice, description, quantityInStock) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, bookID);
        stmt.setString(2, title);
        stmt.setString(3, authors);
        stmt.setInt(4, bookPrice);
        stmt.setString(5, description);
        stmt.setString(6, quantityInStock);
        stmt.executeUpdate();
    }
}