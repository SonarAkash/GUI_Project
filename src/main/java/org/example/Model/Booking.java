package org.example.Model;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private int wasteId;
    private String wasteDescription;
    private String generatorEmail;
    private String status;
    private Timestamp createdAt;
    private Timestamp completedAt;
    private String biogasCompanyEmail;

    // Constructor
    public Booking(int id, int wasteId, String wasteDescription, 
                  String generatorEmail, String status, 
                  Timestamp createdAt, Timestamp completedAt) {
        this.id = id;
        this.wasteId = wasteId;
        this.wasteDescription = wasteDescription;
        this.generatorEmail = generatorEmail;
        this.status = status;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWasteId() {
        return wasteId;
    }

    public void setWasteId(int wasteId) {
        this.wasteId = wasteId;
    }

    public String getWasteDescription() {
        return wasteDescription;
    }

    public void setWasteDescription(String wasteDescription) {
        this.wasteDescription = wasteDescription;
    }

    public String getGeneratorEmail() {
        return generatorEmail;
    }

    public void setGeneratorEmail(String generatorEmail) {
        this.generatorEmail = generatorEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public String getBiogasCompanyEmail() {
        return biogasCompanyEmail;
    }

    public void setBiogasCompanyEmail(String biogasCompanyEmail) {
        this.biogasCompanyEmail = biogasCompanyEmail;
    }

    
    // Getters and setters
    // ... add all necessary getters and setters ...
} 