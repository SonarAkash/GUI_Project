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
                    rs.getTimestamp("completed_at"),
                    rs.getDouble("booked_quantity")
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
            conn.setAutoCommit(false);

            // Only update the booking status to COMPLETED
            String updateBookingQuery = """
                UPDATE bookings 
                SET status = 'COMPLETED', 
                    completed_at = CURRENT_TIMESTAMP 
                WHERE id = ?
            """;
            
            try (PreparedStatement stmt = conn.prepareStatement(updateBookingQuery)) {
                stmt.setInt(1, bookingId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
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

    public boolean bookWaste(int wasteId, int biogasCompanyId, double bookedQuantity) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Check remaining quantity
            String checkQuery = "SELECT remaining_quantity FROM waste WHERE id = ? FOR UPDATE";
            try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
                stmt.setInt(1, wasteId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next() || rs.getDouble("remaining_quantity") < bookedQuantity) {
                    return false;
                }
            }

            // Update waste quantity only
            String updateQuery = "UPDATE waste SET remaining_quantity = remaining_quantity - ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setDouble(1, bookedQuantity);
                stmt.setInt(2, wasteId);
                stmt.executeUpdate();
            }

            // Create booking record
            String bookingQuery = """
                INSERT INTO bookings (waste_id, biogas_company_id, booked_quantity, status) 
                VALUES (?, ?, ?, 'BOOKED')
            """;
            try (PreparedStatement stmt = conn.prepareStatement(bookingQuery)) {
                stmt.setInt(1, wasteId);
                stmt.setInt(2, biogasCompanyId);
                stmt.setDouble(3, bookedQuantity);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
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
            SELECT b.*, w.description as waste_description, 
                   w.remaining_quantity, w.quantity as total_quantity,
                   u.email as biogas_company_email
            FROM bookings b
            JOIN waste w ON b.waste_id = w.id
            JOIN users u ON b.biogas_company_id = u.id
            WHERE w.generator_id = ?
            AND b.status != 'COMPLETED' 
            ORDER BY b.created_at DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, generatorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getString("waste_description"),
                    rs.getString("biogas_company_email"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("completed_at"),
                    rs.getDouble("booked_quantity")
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