package org.example.GUI;

import org.example.Util.GUITheme;

import javax.swing.*;
import java.awt.*;

public class BiogasCompanyDashboard {
    private int userId;
    private JFrame dashboardFrame;

    public BiogasCompanyDashboard(int id) {
        this.userId = id;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        dashboardFrame = new JFrame("Biogas Company Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(800, 600);
        GUITheme.styleFrame(dashboardFrame);

        // Main panel with padding
        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Biogas Company Dashboard");
        titleLabel.setFont(GUITheme.HEADING_FONT);
        titleLabel.setForeground(GUITheme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setOpaque(false);

        // Create buttons with icons (you'll need to add icon files)
        JButton btnViewWasteList = createDashboardButton("View Available Waste", "📦");
        JButton btnViewWasteOrders = createDashboardButton("View Waste Orders", "📋");
        JButton btnProfile = createDashboardButton("My Profile", "👤");
        JButton btnLogout = createDashboardButton("Logout", "🚪");

        // Add buttons to panel
        buttonsPanel.add(btnViewWasteList);
        buttonsPanel.add(btnViewWasteOrders);
        buttonsPanel.add(btnProfile);
        buttonsPanel.add(btnLogout);

        // Add action listeners
        btnViewWasteList.addActionListener(e -> new ViewBookingsB(userId));
        btnViewWasteOrders.addActionListener(e -> new ViewWasteOrders(userId));
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

