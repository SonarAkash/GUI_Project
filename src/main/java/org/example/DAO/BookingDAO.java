package org.example.DAO;

import org.example.Model.Booking;
import org.example.Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public List<Booking> getBookingsByBiogasCompany(int biogasCompanyId) {
        List<Booking> bookings = new ArrayList<>();
        String query = """
            SELECT b.*, w.description, u.email as generator_email 
            FROM Bookings b
            JOIN Waste w ON b.waste_id = w.id 
            JOIN Users u ON w.generator_id = u.id 
            WHERE b.biogas_company_id = ?
            ORDER BY b.created_at DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, biogasCompanyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("waste_id"),
                    rs.getString("description"),
                    rs.getString("generator_email"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("completed_at")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean completeBooking(int bookingId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // 1. Update booking status
            String updateBookingQuery = """
                UPDATE Bookings 
                SET status = 'COMPLETED', completed_at = CURRENT_TIMESTAMP 
                WHERE id = ?
            """;
            
            try (PreparedStatement stmt = conn.prepareStatement(updateBookingQuery)) {
                stmt.setInt(1, bookingId);
                stmt.executeUpdate();
            }

            // 2. Update waste status
            String updateWasteQuery = """
                UPDATE Waste w
                JOIN Bookings b ON w.id = b.waste_id
                SET w.status = 'COMPLETED'
                WHERE b.id = ?
            """;
            
            try (PreparedStatement stmt = conn.prepareStatement(updateWasteQuery)) {
                stmt.setInt(1, bookingId);
                stmt.executeUpdate();
            }

            conn.commit();  // Commit transaction
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();  // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean bookWaste(int wasteId, int biogasCompanyId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // 1. Insert booking
            String insertBookingQuery = """
                INSERT INTO Bookings (waste_id, biogas_company_id) 
                VALUES (?, ?)
            """;
            
            try (PreparedStatement stmt = conn.prepareStatement(insertBookingQuery)) {
                stmt.setInt(1, wasteId);
                stmt.setInt(2, biogasCompanyId);
                stmt.executeUpdate();
            }

            // 2. Update waste status
            String updateWasteQuery = """
                UPDATE Waste 
                SET status = 'BOOKED' 
                WHERE id = ?
            """;
            
            try (PreparedStatement stmt = conn.prepareStatement(updateWasteQuery)) {
                stmt.setInt(1, wasteId);
                stmt.executeUpdate();
            }

            conn.commit();  // Commit transaction
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();  // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Booking> getBookingsByGenerator(int generatorId) {
        List<Booking> bookings = new ArrayList<>();
        String query = """
            SELECT b.*, w.description, u.email as biogas_company_email 
            FROM Bookings b
            JOIN Waste w ON b.waste_id = w.id 
            JOIN Users u ON b.biogas_company_id = u.id 
            WHERE w.generator_id = ?
            ORDER BY b.created_at DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, generatorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("waste_id"),
                    rs.getString("description"),
                    rs.getString("biogas_company_email"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("completed_at")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 