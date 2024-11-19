package org.example.ilib.Controller;

import java.sql.*;

import static java.lang.System.getenv;

// Singleton design pattern
public class DBConnection {
    private static DBConnection instance;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String identityNumber;

    public String getEmail() {
        return email;
    }
    private DBConnection(String email, String password,
                         String fullName, String phoneNumber, String identiyNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.identityNumber = identiyNumber;
    }
    public DBConnection() {}

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/ilib", getenv("userName"),
                    getenv("userPassword"));
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            return null;
        }
    }

    public void Initialize(String email, String password,String fullName, String phoneNumber, String identiyNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.identityNumber = identiyNumber;
    }
    public void createAccount() throws SQLException {
        Connection connection
                = getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO user (email, password, fullName, phoneNumber, identityNumber) " +
                "VALUES ('" + this.email + "','" + this.password + "','"
                + this.fullName + "','" + this.phoneNumber + "','" + this.identityNumber + "')");
        stmt.executeUpdate("INSERT INTO Voucher (email, discount_percentage, end_discount_date) " +
                "VALUES ('" + email + "', 50, '2024-11-02')");
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");
    }
    public boolean checkDataExit(String email_){
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email_); // Gán giá trị cho dấu ? trong câu lệnh SQL
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Lấy giá trị COUNT từ kết quả
                return count > 0; // Nếu COUNT > 0 nghĩa là dữ liệu đã tồn tại
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
        }
        return false;
    }
}