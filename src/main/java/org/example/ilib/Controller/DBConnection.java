package org.example.ilib.Controller;

import java.sql.*;

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
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO user (Email, phoneNumber, fullName, password) "
                + "VALUES ('" + email + "','" + phoneNumber + "','"
                + fullName + "','" + password + "')");
        stmt.executeUpdate("INSERT INTO Voucher (Email, discountPercentage) " +
                "VALUES ('" + email + "', 50)");
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");
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
}