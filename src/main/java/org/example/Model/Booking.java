package org.example.Model;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private int waste_id;
    private String waste_description;
    private String generator_email;
    private String biogas_company_email;
    private String status;
    private Timestamp created_at;
    private Timestamp completed_at;
    private double booked_quantity;
    private double remaining_quantity;
    private double total_quantity;

    // Constructor for getBookingsByBiogasCompany
    public Booking(int id, int waste_id, String waste_description, 
                  String generator_email, String status, 
                  Timestamp created_at, Timestamp completed_at,
                  double booked_quantity) {
        this.id = id;
        this.waste_id = waste_id;
        this.waste_description = waste_description;
        this.generator_email = generator_email;
        this.status = status;
        this.created_at = created_at;
        this.completed_at = completed_at;
        this.booked_quantity = booked_quantity;
    }

    // Constructor for getBookingsByGenerator
    public Booking(int id, String waste_description, 
                  String biogas_company_email, String status,
                  Timestamp created_at, Timestamp completed_at,
                  double booked_quantity) {
        this.id = id;
        this.waste_description = waste_description;
        this.biogas_company_email = biogas_company_email;
        this.status = status;
        this.created_at = created_at;
        this.completed_at = completed_at;
        this.booked_quantity = booked_quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWasteId() {
        return waste_id;
    }

    public void setWasteId(int waste_id) {
        this.waste_id = waste_id;
    }

    public String getWasteDescription() {
        return waste_description;
    }

    public void setWasteDescription(String waste_description) {
        this.waste_description = waste_description;
    }

    public String getGeneratorEmail() {
        return generator_email;
    }

    public void setGeneratorEmail(String generator_email) {
        this.generator_email = generator_email;
    }

    public String getBiogasCompanyEmail() {
        return biogas_company_email;
    }

    public void setBiogasCompanyEmail(String biogas_company_email) {
        this.biogas_company_email = biogas_company_email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getCompletedAt() {
        return completed_at;
    }

    public void setCompletedAt(Timestamp completed_at) {
        this.completed_at = completed_at;
    }

    public double getBookedQuantity() {
        return booked_quantity;
    }

    public void setBookedQuantity(double booked_quantity) {
        this.booked_quantity = booked_quantity;
    }

    public double getRemainingQuantity() {
        return remaining_quantity;
    }

    public void setRemainingQuantity(double remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
    }

    public double getTotalQuantity() {
        return total_quantity;
    }

    public void setTotalQuantity(double total_quantity) {
        this.total_quantity = total_quantity;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", waste_id=" + waste_id +
                ", waste_description='" + waste_description + '\'' +
                ", generator_email='" + generator_email + '\'' +
                ", biogas_company_email='" + biogas_company_email + '\'' +
                ", status='" + status + '\'' +
                ", created_at=" + created_at +
                ", completed_at=" + completed_at +
                ", booked_quantity=" + booked_quantity +
                ", remaining_quantity=" + remaining_quantity +
                ", total_quantity=" + total_quantity +
                '}';
    }
} 