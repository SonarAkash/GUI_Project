package org.example.DAO;

import org.example.Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(String name, String email, String password, String role, String phone, String address) {
        String sql = "INSERT INTO Users (name, email, password, role, phone, address) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);  // Ideally, hash password
            stmt.setString(4, role);
            stmt.setString(5, phone);
            stmt.setString(6, address);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int validateLogin(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Return the userId if credentials are valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getRoleByEmail(String email) {
        String sql = "SELECT role FROM Users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

