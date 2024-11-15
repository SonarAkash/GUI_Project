package org.example.GUI;

import org.example.Util.GUITheme;
import org.example.DAO.UserDAO;
import org.example.DAO.NotificationDAO;
import org.example.Model.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.util.List;


public class GeneratorDashboard {
    private int userId;
    private JFrame dashboardFrame;
    private JPanel statsPanel;
    private UserDAO userDAO;

    public GeneratorDashboard(int userId) {
        this.userId = userId;
        this.userDAO = new UserDAO();
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

        // Stats Panel with real data
        statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        
        // Create buttons panel first
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setOpaque(false);

        // Create buttons with icons
        JButton btnUploadWaste = createDashboardButton("Upload Waste", "⬆️");
        JButton btnViewWaste = createDashboardButton("View My Waste", "📦");
        JButton btnViewBookings = createDashboardButton("My Bookings", "📋");
        JButton btnLogout = createDashboardButton("Logout", "🚪");
        JButton btnNotifications = createDashboardButton("Notifications", "📋");

        // Add buttons to panel
        buttonsPanel.add(btnUploadWaste);
        buttonsPanel.add(btnViewWaste);
        buttonsPanel.add(btnViewBookings);
        buttonsPanel.add(btnLogout);
        buttonsPanel.add(btnNotifications);

        // Add action listeners with refresh mechanism
        btnUploadWaste.addActionListener(e -> {
            new UploadWasteForm(userId);
            dashboardFrame.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    refreshStats();
                }
                @Override
                public void windowLostFocus(WindowEvent e) {}
            });
        });
        
        btnViewWaste.addActionListener(e -> {
            new ViewWasteListF(userId);
            dashboardFrame.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    refreshStats();
                }
                @Override
                public void windowLostFocus(WindowEvent e) {}
            });
        });
        
        btnViewBookings.addActionListener(e -> {
            new ViewMyBookingsG(userId);
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

        btnNotifications.addActionListener(e -> showNotifications());

        // Initial statistics load
        refreshStats();

        headerPanel.add(statsPanel, BorderLayout.SOUTH);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        dashboardFrame.add(mainPanel);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
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

    private void refreshStats() {
        statsPanel.removeAll();
        
        int totalUploads = userDAO.getTotalUploads(userId);
        int activeBookings = userDAO.getActiveBookingsForGenerator(userId);
        int completedBookings = userDAO.getCompletedBookingsForGenerator(userId);
        
        statsPanel.add(createStatCard("Total Uploads", String.valueOf(totalUploads), "⬆️"));
        statsPanel.add(createStatCard("Active Bookings", String.valueOf(activeBookings), "🔄"));
        statsPanel.add(createStatCard("Completed", String.valueOf(completedBookings), "✅"));
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void showNotifications() {
        NotificationDAO notificationDAO = new NotificationDAO();
        List<Notification> notifications = notificationDAO.getUnreadNotifications(userId);
        
        if (notifications.isEmpty()) {
            JOptionPane.showMessageDialog(
                dashboardFrame,
                "No new notifications",
                "Notifications",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder message = new StringBuilder("New Notifications:\n\n");
        for (Notification notification : notifications) {
            message.append("- ").append(notification.getMessage()).append("\n");
            notificationDAO.markAsRead(notification.getId());
        }

        JOptionPane.showMessageDialog(
            dashboardFrame,
            message.toString(),
            "Notifications",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
