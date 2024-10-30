package org.example.ilib;

import java.sql.*;

public class DBConnection {
    public static void main(String[] args) throws SQLException {
        /*Connection connection
                = DriverManager.getConnection("jdbc:mysql://localhost:3306/ilib",
                "root",
                "Dat29072005#");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from user");
        while (rs.next()) {
            String email = rs.getString("email");
            String password = rs.getString("password");
            String fullName = rs.getString("fullName");
            String phoneNumber = rs.getString("phoneNumber");
            String identityNumber = rs.getString("identityNumber");
            System.out.println("Email: " + email + " password: " + password + " fullName: " + fullName
                    + " phoneNumber: " + phoneNumber + " identityNumber: " + identityNumber);
        }*/
        System.out.println("Connecting to database...");
    }
}
