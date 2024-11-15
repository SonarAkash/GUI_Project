package org.example.Service;

import org.example.Util.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUploadService {

    // Method to upload waste details and images
    public boolean uploadWasteWithImages(String description, double quantity, List<File> images, int userId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);  // Start transaction

            // Step 1: Insert waste entry with quantity
            String wasteQuery = """
                INSERT INTO waste (description, generator_id, status, quantity, remaining_quantity) 
                VALUES (?, ?, 'AVAILABLE', ?, ?)
            """;

            int wasteId;
            try (PreparedStatement stmt = connection.prepareStatement(wasteQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, description);
                stmt.setInt(2, userId);
                stmt.setDouble(3, quantity);
                stmt.setDouble(4, quantity);  // Initially remaining = total
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        wasteId = generatedKeys.getInt(1);

                        // Step 2: Store images and save paths
                        for (File image : images) {
                            String imagePath = saveImageToFileSystem(image);
                            saveImagePathToDB(connection, wasteId, imagePath);
                        }
                        
                        connection.commit();
                        return true;
                    }
                }
            }
            
            connection.rollback();
            return false;

        } catch (SQLException | IOException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to save image to the file system
    private String saveImageToFileSystem(File image) throws IOException {
        Path directoryPath = Paths.get("D:/gui_images/");
        Files.createDirectories(directoryPath);  // Creates directory if doesn't exist

        Path destPath = directoryPath.resolve(image.getName());
        Files.copy(image.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        return destPath.toString();
    }

    // Method to save image path to the database
    private void saveImagePathToDB(Connection connection, int wasteId, String imagePath) throws SQLException {
        String imageQuery = "INSERT INTO waste_images (waste_id, image_path) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(imageQuery)) {
            stmt.setInt(1, wasteId);
            stmt.setString(2, imagePath);
            stmt.executeUpdate();
        }
    }

}

