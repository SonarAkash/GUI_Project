package org.example.DAO;

import org.example.Util.DatabaseConnection;
import org.example.Model.Waste;


import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WasteDAO {

    // Method to fetch waste data including images for a generator
    public void fetchWasteData(int userId, DefaultTableModel model) {
        model.setRowCount(0);
        
        String sql = """
            SELECT w.id AS waste_id, w.description, 
                   GROUP_CONCAT(wi.image_path SEPARATOR ', ') AS image_paths 
            FROM waste w 
            LEFT JOIN waste_images wi ON w.id = wi.waste_id 
            WHERE w.generator_id = ? 
            AND w.status != 'COMPLETED'
            GROUP BY w.id, w.description 
            ORDER BY w.id DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int wasteId = rs.getInt("waste_id");
                String description = rs.getString("description");
                String imagePaths = rs.getString("image_paths");
                
                model.addRow(new Object[]{
                    wasteId,
                    description,
                    imagePaths != null ? imagePaths : "No images"
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Waste> getAvailableWaste() {
        List<Waste> wasteList = new ArrayList<>();
        String query = """
            SELECT w.*, u.email as generator_name,
               GROUP_CONCAT(wi.image_path SEPARATOR ', ') as image_paths
        FROM Waste w 
        JOIN Users u ON w.generator_id = u.id 
        LEFT JOIN waste_images wi ON w.id = wi.waste_id
        WHERE w.status = 'AVAILABLE'
        GROUP BY w.id, w.description, u.email, w.created_at, w.status
        ORDER BY w.created_at DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Waste waste = new Waste(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("generator_name"),
                    rs.getTimestamp("created_at"),
                    rs.getString("status"),
                    rs.getString("image_paths")
                );
                wasteList.add(waste);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wasteList;
    }

    public String getWasteImagePath(int wasteId) {
        String query = """
            SELECT GROUP_CONCAT(image_path) as image_paths
            FROM waste_images 
            WHERE waste_id = ?
            GROUP BY waste_id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, wasteId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("image_paths");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no images found
    }
}
