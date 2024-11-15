package org.example.Model;

import java.sql.Timestamp;

public class Waste {
    private int id;
    private String description;
    private String generatorName;
    private Timestamp createdAt;
    private String imagePath;
    private double quantity;
    private double remainingQuantity;

    public Waste(int id, String description, String generatorName, 
                Timestamp createdAt,  String imagePath,
                double quantity, double remainingQuantity) {
        this.id = id;
        this.description = description;
        this.generatorName = generatorName;
        this.createdAt = createdAt;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.remainingQuantity = remainingQuantity;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    

    // Add getters and setters
} 