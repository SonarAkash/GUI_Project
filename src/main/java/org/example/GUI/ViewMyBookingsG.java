package org.example.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.example.DAO.BookingDAO;
import org.example.Model.Booking;
import org.example.Util.GUITheme;

public class ViewMyBookingsG {
    private final JFrame frame;
    private final JTable bookingsTable;
    private final DefaultTableModel tableModel;
    private final int generatorId;

    public ViewMyBookingsG(int generatorId) {
        this.generatorId = generatorId;
        
        // Initialize frame
        frame = new JFrame("My Waste Bookings");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GUITheme.styleFrame(frame);

        // Create main panel
        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Add header
        JLabel headerLabel = new JLabel("My Waste Bookings", SwingConstants.CENTER);
        headerLabel.setFont(GUITheme.HEADING_FONT);
        headerLabel.setForeground(GUITheme.TEXT_PRIMARY);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Create table
        String[] columns = {
            "Booking ID",
            "Waste Description",
            "Biogas Company",
            "Booked Quantity",
            "Status",
            "Booking Date",
            "Action"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Changed from 5 to 6 to match the Action column index
            }
        };
        
        bookingsTable = new JTable(tableModel);
        GUITheme.styleTable(bookingsTable);
        
        // Configure table properties
        bookingsTable.setRowHeight(40);
        bookingsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bookingsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        bookingsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        bookingsTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Status
        bookingsTable.getColumnModel().getColumn(5).setPreferredWidth(150);  // Booking Date
        bookingsTable.getColumnModel().getColumn(6).setPreferredWidth(100);  // Action

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setOpaque(false);
        
        JButton refreshButton = new JButton("Refresh List");
        GUITheme.styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadBookings());
        
        controlPanel.add(refreshButton);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Load data and setup action column
        loadBookings();
        setupActionColumn();
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        BookingDAO bookingDAO = new BookingDAO();
        java.util.List<Booking> bookings = bookingDAO.getBookingsByGenerator(generatorId);
        
        for (Booking booking : bookings) {
            String actionButtonText = "-";
            if (booking.getStatus().equals("BOOKED")) {
                actionButtonText = "Complete";
            }
            
            tableModel.addRow(new Object[]{
                booking.getId(),
                booking.getWasteDescription(),
                booking.getBiogasCompanyEmail(),
                booking.getBookedQuantity() + " kg",
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getStatus().equals("BOOKED") ? "Complete" : "-"  // Simplified action text logic
            });
        }
    }

    private void setupActionColumn() {
        new ButtonColumn(bookingsTable, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = Integer.parseInt(e.getActionCommand());
                String status = (String) bookingsTable.getValueAt(row, 4);
                int bookingId = (int) bookingsTable.getValueAt(row, 0);
                
                if (status.equals("BOOKED")) {
                    handleCompleteBooking(bookingId);
                }
            }
        }, 6);
    }

    private void handleCompleteBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Mark this booking as completed?",
            "Confirm Completion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            BookingDAO bookingDAO = new BookingDAO();
            if (bookingDAO.completeBooking(bookingId)) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Booking marked as completed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadBookings(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(
                    frame,
                    "Failed to complete booking. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
} 