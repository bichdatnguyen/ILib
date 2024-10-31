package org.example.ilib;

import java.sql.*;

public class DBConnection {

    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String identityNumber;
    private String discountPercentage;
    private String endDiscount;

    public DBConnection(String email, String password
            , String fullName, String phoneNumber, String identiyNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.identityNumber = identiyNumber;
    }

    public void createAccount() throws SQLException {
        Connection connection
                = DriverManager.getConnection("jdbc:mysql://localhost:3306/ilib",
                "root",
                "12345678");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Voucher (email, discount_percentage, end_discount_date) " +
                "VALUES ('" + email + "', 50, '2024-11-02')");
        stmt.executeUpdate("INSERT INTO user (email, password, fullName, phoneNumber, identityNumber) " +
                "VALUES ('" + this.email + "','" + this.password + "','"
                + this.fullName + "','" + this.phoneNumber + "','" + this.identityNumber + "')");
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");
    }
}
