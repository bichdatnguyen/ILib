package org.example.ilib.Processor;

import org.example.ilib.Controller.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminApp {
    private static AdminApp instance;
    private AdminApp() {}
    public static AdminApp getInstance() {
        if (instance == null) {
            instance = new AdminApp();
        }
        return instance;
    }

    public boolean adminChecking() {
        //System.out.println("Admin checking works!");
        String query = "SELECT role FROM user WHERE email = ? and password = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query)) {
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, Account.getInstance().getPassword());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("role"));
                Account.getInstance().setRole(resultSet.getString("role"));
                return resultSet.getString("role").equals("admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}