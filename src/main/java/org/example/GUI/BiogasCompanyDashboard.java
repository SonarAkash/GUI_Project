package org.example.GUI;

import org.example.Util.GUITheme;
import org.example.DAO.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class BiogasCompanyDashboard {
    private int userId;
    private JFrame dashboardFrame;
    private JPanel statsPanel;
    private UserDAO userDAO;

    public BiogasCompanyDashboard(int id) {
        this.userId = id;
        this.userDAO = new UserDAO();
        createAndShowGUI();
    }

    private void refreshStats() {
        statsPanel.removeAll();
        
        int totalBookings = userDAO.getTotalBookings(userId);
        int activeBookings = userDAO.getActiveBookings(userId);
        int completedBookings = userDAO.getCompletedBookings(userId);
        
        statsPanel.add(createStatCard("Total Bookings", String.valueOf(totalBookings), "📊"));
        statsPanel.add(createStatCard("Active Bookings", String.valueOf(activeBookings), "🔄"));
        statsPanel.add(createStatCard("Completed", String.valueOf(completedBookings), "✅"));
        
        statsPanel.revalidate();
        statsPanel.repaint();
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

        // Initialize statsPanel
        statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        
        // Add action listeners with refresh mechanism
        btnViewWasteList.addActionListener(e -> {
            new ViewBookingsB(userId);
            dashboardFrame.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    refreshStats();
                }
                @Override
                public void windowLostFocus(WindowEvent e) {}
            });
        });
        
        btnViewWasteOrders.addActionListener(e -> {
            new ViewWasteOrders(userId);
            dashboardFrame.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    refreshStats();
                }
                @Override
                public void windowLostFocus(WindowEvent e) {}
            });
        });

        btnLogout.addActionListener(e -> {
            dashboardFrame.dispose();
            MainWindow.createAndShowGUI();
        });

        // Initial statistics load
        refreshStats();

        // Update main panel layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

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

    private JPanel createStatCard(String title, String value, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBackground(GUITheme.PRIMARY);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GUITheme.PRIMARY.darker(), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Create title label
        JLabel titleLabel = new JLabel(emoji + " " + title);
        titleLabel.setForeground(GUITheme.TEXT_LIGHT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create value label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(GUITheme.TEXT_LIGHT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(GUITheme.PRIMARY.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(GUITheme.PRIMARY);
            }
        });

        return card;
    }
}

