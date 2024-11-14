package org.example.Util;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gui_project?useSSL=true&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; // Update with your MySQL username
    private static final String PASSWORD = "Akash@9113"; // Update with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static boolean isEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
