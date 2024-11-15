package org.example.GUI;

import org.example.DAO.BookingDAO;
import org.example.Model.Booking;
import org.example.Util.GUITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewWasteOrders {
    private final int biogasCompanyId;
    private final JFrame frame;
    private final DefaultTableModel tableModel;

    public ViewWasteOrders(int biogasCompanyId) {
        this.biogasCompanyId = biogasCompanyId;
        
        // Initialize frame
        frame = new JFrame("View Waste Orders");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        GUITheme.styleFrame(frame);

        // Create main panel
        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Add header
        JLabel headerLabel = new JLabel("Waste Orders", SwingConstants.CENTER);
        headerLabel.setFont(GUITheme.HEADING_FONT);
        headerLabel.setForeground(GUITheme.TEXT_PRIMARY);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Define table columns
        String[] columns = {"Order ID", "Generator Email", "Waste Description", "Booked Quantity", "Order Date", "Status"};
        
        // Create table model
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        GUITheme.styleTable(table);
        
        // Configure table properties
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh List");
        GUITheme.styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadOrders());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Load initial data
        loadOrders();
    }

    private void loadOrders() {
        tableModel.setRowCount(0);
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getBookingsByBiogasCompany(biogasCompanyId);
        
        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                booking.getId(),
                booking.getGeneratorEmail(),
                booking.getWasteDescription(),
                booking.getBookedQuantity() + " kg",
                booking.getCreatedAt(),
                booking.getStatus()
            });
        }
    }
}
