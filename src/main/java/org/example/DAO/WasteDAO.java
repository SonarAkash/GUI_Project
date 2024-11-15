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
        String sql = """
            SELECT w.id AS waste_id, w.description, w.quantity, w.remaining_quantity,
                   GROUP_CONCAT(wi.image_path SEPARATOR ', ') AS image_paths 
            FROM waste w 
            LEFT JOIN waste_images wi ON w.id = wi.waste_id 
            JOIN users u ON w.generator_id = u.id
            WHERE w.generator_id = ? 
            AND u.role = 'generator'
            GROUP BY w.id, w.description, w.quantity, w.remaining_quantity
            ORDER BY w.id DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("waste_id"),
                    rs.getString("description"),
                    rs.getDouble("quantity") + " kg",
                    rs.getDouble("remaining_quantity") + " kg",
                    rs.getString("image_paths") != null ? rs.getString("image_paths") : "No images"
                });
            }
        } catch (SQLException e) {
            System.out.println("Error in fetchWasteData: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Waste> getAvailableWaste() {
        List<Waste> wasteList = new ArrayList<>();
        String query = """
            SELECT w.*, u.email as generator_name,
               GROUP_CONCAT(wi.image_path SEPARATOR ', ') as image_paths
            FROM waste w 
            JOIN users u ON w.generator_id = u.id 
            LEFT JOIN waste_images wi ON w.id = wi.waste_id
            WHERE w.remaining_quantity > 0
            AND u.role = 'generator'
            GROUP BY w.id
            ORDER BY w.created_at DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Waste waste = new Waste(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("generator_name"),
                    rs.getTimestamp("created_at"),
                    rs.getString("image_paths"),
                    rs.getDouble("quantity"),
                    rs.getDouble("remaining_quantity")
                );
                wasteList.add(waste);
                System.out.println("Found waste: ID=" + waste.getId() + 
                                 ", Desc=" + waste.getDescription() + 
                                 ", Quantity=" + waste.getQuantity() + 
                                 ", Generator=" + waste.getGeneratorName());
            }
        } catch (SQLException e) {
            System.out.println("Error in getAvailableWaste: " + e.getMessage());
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

    public boolean updateRemainingQuantity(int wasteId, double newRemainingQuantity) {
        String sql = "UPDATE waste SET remaining_quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, newRemainingQuantity);
            stmt.setInt(2, wasteId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
