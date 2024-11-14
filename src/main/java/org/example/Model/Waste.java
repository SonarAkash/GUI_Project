package org.example.Model;

import java.sql.Timestamp;

public class Waste {
    private int id;
    private String description;
    private String generatorName;
    private Timestamp createdAt;
    private String status;
    private String imagePath;

    public Waste(int id, String description, String generatorName, 
                Timestamp createdAt, String status, String imagePath) {
        this.id = id;
        this.description = description;
        this.generatorName = generatorName;
        this.createdAt = createdAt;
        this.status = status;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public void setGeneratorName(String generatorName) {
        this.generatorName = generatorName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    

    // Add getters and setters
} 