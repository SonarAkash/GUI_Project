package org.example.GUI;

import org.example.Util.GUITheme;

import javax.swing.*;
import java.awt.*;

public class GeneratorDashboard {
    private int userId;
    private JFrame dashboardFrame;

    public GeneratorDashboard(int userId) {
        this.userId = userId;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        dashboardFrame = new JFrame("Food Waste Generator Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(800, 600);
        GUITheme.styleFrame(dashboardFrame);

        // Main panel with padding
        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Food Waste Generator Dashboard");
        titleLabel.setFont(GUITheme.HEADING_FONT);
        titleLabel.setForeground(GUITheme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Stats Panel (can be enhanced with actual statistics)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatsLabel("Total Uploads", "15"));
        statsPanel.add(createStatsLabel("Active Bookings", "3"));
        statsPanel.add(createStatsLabel("Completed", "12"));
        headerPanel.add(statsPanel, BorderLayout.SOUTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setOpaque(false);

        // Create buttons with icons
        JButton btnUploadWaste = createDashboardButton("Upload Waste", "⬆️");
        JButton btnViewWaste = createDashboardButton("View My Waste", "📦");
        JButton btnViewBookings = createDashboardButton("My Bookings", "📋");
        JButton btnLogout = createDashboardButton("Logout", "🚪");

        // Add buttons to panel
        buttonsPanel.add(btnUploadWaste);
        buttonsPanel.add(btnViewWaste);
        buttonsPanel.add(btnViewBookings);
        buttonsPanel.add(btnLogout);

        // Add action listeners
        btnUploadWaste.addActionListener(e -> new UploadWasteForm(userId));
        btnViewWaste.addActionListener(e -> new ViewWasteListF(userId));
        btnViewBookings.addActionListener(e -> new ViewMyBookingsG(userId));
        btnLogout.addActionListener(e -> {
            dashboardFrame.dispose();
            MainWindow.createAndShowGUI();
        });

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        dashboardFrame.add(mainPanel);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
    }

    private JLabel createStatsLabel(String title, String value) {
        JLabel label = new JLabel("<html><center>" + value + "<br><small>" + title + "</small></center></html>");
        label.setFont(GUITheme.LABEL_FONT);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GUITheme.PRIMARY),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return label;
    }

    private JButton createDashboardButton(String text, String emoji) {
        JButton button = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
        button.setFont(GUITheme.BUTTON_FONT);
        button.setBackground(GUITheme.PRIMARY);
        button.setForeground(GUITheme.TEXT_LIGHT);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(GUITheme.PRIMARY.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(GUITheme.PRIMARY);
            }
        });

        return button;
    }
}
