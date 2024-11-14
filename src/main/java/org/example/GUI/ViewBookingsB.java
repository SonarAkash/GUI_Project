package org.example.GUI;

import org.example.DAO.BookingDAO;
import org.example.DAO.WasteDAO;
import org.example.Model.Waste;
import org.example.Service.ImageRenderer;
import org.example.Util.GUITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ViewBookingsB {
    private final JFrame frame;
    private final JTable wasteTable;
    private final DefaultTableModel tableModel;
    private final int biogasCompanyId;
    private final JPanel imagePanel;
    private static final int IMAGE_HEIGHT = 200;

    public ViewBookingsB(int biogasCompanyId) {
        this.biogasCompanyId = biogasCompanyId;
        
        // Initialize frame
        frame = new JFrame("Available Waste List");
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GUITheme.styleFrame(frame);

        // Create main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);
        splitPane.setDividerSize(5);

        // Create left panel for table
        JPanel leftPanel = GUITheme.createStyledPanel();
        leftPanel.setLayout(new BorderLayout(10, 10));

        // Add header
        JLabel headerLabel = new JLabel("Available Waste Listings", SwingConstants.CENTER);
        headerLabel.setFont(GUITheme.HEADING_FONT);
        headerLabel.setForeground(GUITheme.TEXT_PRIMARY);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        leftPanel.add(headerLabel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"ID", "Description", "Generator", "Images", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only allow editing of the Action column
            }
        };
        
        wasteTable = new JTable(tableModel);
        GUITheme.styleTable(wasteTable);
        
        // Configure table properties
        wasteTable.setRowHeight(50);
        wasteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        wasteTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        wasteTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        wasteTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        wasteTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Set custom renderer for images column
        wasteTable.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(wasteTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh List");
        GUITheme.styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadWasteData());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create right panel for image preview
        imagePanel = GUITheme.createStyledPanel();
        imagePanel.setLayout(new BorderLayout());
        JLabel previewLabel = new JLabel("Select a row to view images", SwingConstants.CENTER);
        previewLabel.setFont(GUITheme.LABEL_FONT);
        imagePanel.add(previewLabel, BorderLayout.CENTER);

        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(imagePanel);

        frame.add(splitPane);
        frame.setVisible(true);

        // Load data and setup listeners
        loadWasteData();
        setupTableListeners();
    }

    private void loadWasteData() {
        tableModel.setRowCount(0);
        WasteDAO wasteDAO = new WasteDAO();
        List<Waste> wasteList = wasteDAO.getAvailableWaste();
        
        for (Waste waste : wasteList) {
            JButton bookButton = new JButton("Book");
            GUITheme.styleButton(bookButton);
            bookButton.setBackground(GUITheme.ACCENT);
            
            tableModel.addRow(new Object[]{
                waste.getId(),
                waste.getDescription(),
                waste.getGeneratorName(),
                waste.getImagePath(),
                "Book"
            });
        }

        // Add button column
        new ButtonColumn(wasteTable, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = Integer.parseInt(e.getActionCommand());
                int wasteId = (int) wasteTable.getValueAt(row, 0);
                handleBooking(wasteId);
            }
        }, 4);
    }

    private void setupTableListeners() {
        wasteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = wasteTable.rowAtPoint(evt.getPoint());
                int col = wasteTable.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && col == 3) {
                    String imagePaths = (String) wasteTable.getValueAt(row, col);
                    showImagePreview(imagePaths);
                }
            }
        });
    }

    private void showImagePreview(String imagePaths) {
        imagePanel.removeAll();
        
        if (imagePaths == null || imagePaths.isEmpty() || imagePaths.equals("No images")) {
            JLabel noImageLabel = new JLabel("No images available", SwingConstants.CENTER);
            noImageLabel.setFont(GUITheme.LABEL_FONT);
            imagePanel.add(noImageLabel);
        } else {
            JPanel imagesPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            imagesPanel.setOpaque(false);
            
            String[] paths = imagePaths.split(", ");
            for (String path : paths) {
                if (path != null && !path.trim().isEmpty()) {
                    ImageIcon originalIcon = new ImageIcon(path);
                    Image scaledImage = originalIcon.getImage()
                            .getScaledInstance(-1, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    imageLabel.setBorder(BorderFactory.createLineBorder(GUITheme.PRIMARY, 2));
                    imagesPanel.add(imageLabel);
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(imagesPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            imagePanel.add(scrollPane);
        }
        
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private void handleBooking(int wasteId) {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to book this waste?",
            "Confirm Booking",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            BookingDAO bookingDAO = new BookingDAO();
            if (bookingDAO.bookWaste(wasteId, biogasCompanyId)) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Waste booked successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadWasteData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(
                    frame,
                    "Failed to book waste. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
} 