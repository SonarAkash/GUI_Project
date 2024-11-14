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
    public boolean uploadWasteWithImages(String description, List<File> images, int userId) {
        // Step 1: Insert waste entry into the 'waste' table
        String wasteQuery = "INSERT INTO waste (description, generator_id, status) VALUES (?, ?, 'AVAILABLE')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(wasteQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, description);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int wasteId = generatedKeys.getInt(1);  // Get the generated waste_id

                    // Step 2: Store images and save paths in 'waste_images' table
                    for (File image : images) {
                        String imagePath = saveImageToFileSystem(image);  // Save image and get path
                        saveImagePathToDB(connection, wasteId, imagePath);  // Save the image path in DB
                    }
                    return true;
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
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

