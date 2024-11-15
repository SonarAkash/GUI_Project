package org.example.DAO;

import org.example.Model.Notification;
import org.example.Util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    
    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = """
            SELECT n.*, w.description as waste_description 
            FROM notifications n
            JOIN waste w ON n.waste_id = w.id
            WHERE n.user_id = ? AND n.is_read = false
            ORDER BY n.created_at DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Notification notification = new Notification(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("waste_id"),
                    rs.getString("message"),
                    rs.getTimestamp("created_at"),
                    rs.getBoolean("is_read")
                );
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    public void markAsRead(int notificationId) {
        String query = "UPDATE notifications SET is_read = true WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}