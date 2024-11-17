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

    private DBConnection(String email, String password,
                         String fullName, String phoneNumber, String identiyNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.identityNumber = identiyNumber;
    }

    public static DBConnection getInstance(String email, String password,
                                           String fullName, String phoneNumber, String identiyNumber) {
        DBConnection result = instance;
        if (result == null) {
            synchronized (DBConnection.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DBConnection(email, password, fullName, phoneNumber, identiyNumber);
                }
            }
        }
        return result;
    }

    public void createAccount() throws SQLException {
        Connection connection
                = DriverManager.getConnection("jdbc:mysql://localhost:3306/ilib",
                getenv("userName"),
                getenv("userPassword"));
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO user (email, password, fullName, phoneNumber, identityNumber) " +
                "VALUES ('" + this.email + "','" + this.password + "','"
                + this.fullName + "','" + this.phoneNumber + "','" + this.identityNumber + "')");
        stmt.executeUpdate("INSERT INTO Voucher (email, discount_percentage, end_discount_date) " +
                "VALUES ('" + email + "', 50, '2024-11-02')");
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");
    }
}