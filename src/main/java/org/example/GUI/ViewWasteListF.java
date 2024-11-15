package org.example.GUI;

import org.example.DAO.WasteDAO;
import org.example.Service.ImageRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewWasteListF {
    private final int userId;
    private final JFrame frame;
    private final DefaultTableModel model;
    private final JTable wasteTable;

    public ViewWasteListF(int userId) {
        this.userId = userId;
        
        // Create and setup the main frame
        frame = new JFrame("My Uploaded Waste List");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("My Uploaded Waste Items");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create refresh button
        JButton refreshButton = createStyledButton("Refresh List");
        refreshButton.addActionListener(e -> refreshTable());
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        // Setup table
        String[] columns = {"Waste ID", "Description", "Total Quantity", "Remaining", "Images"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        wasteTable = new JTable(model);
        setupTable();
        
        // Create scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(wasteTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add status panel at the bottom
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Double-click on images to view in full size");
        statusLabel.setForeground(new Color(127, 140, 141));
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        
        // Add main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true);
        
        // Load initial data
        refreshTable();
    }

    private void setupTable() {
        // Set row height for images
        wasteTable.setRowHeight(120);
        
        // Style table header
        JTableHeader header = wasteTable.getTableHeader();
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Set column widths
        wasteTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        wasteTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        wasteTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        wasteTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        wasteTable.getColumnModel().getColumn(4).setPreferredWidth(230);
        
        // Set custom renderer for images column
        wasteTable.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
        
        // Style table
        wasteTable.setFont(new Font("Arial", Font.PLAIN, 12));
        wasteTable.setGridColor(new Color(189, 195, 199));
        wasteTable.setShowVerticalLines(true);
        wasteTable.setShowHorizontalLines(true);
        
        // Add selection styling
        wasteTable.setSelectionBackground(new Color(52, 152, 219, 50));
        wasteTable.setSelectionForeground(Color.BLACK);
        
        // Add mouse listener for image preview
        wasteTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = wasteTable.rowAtPoint(e.getPoint());
                    int col = wasteTable.columnAtPoint(e.getPoint());
                    if (col == 4) {
                        String imagePaths = (String) wasteTable.getValueAt(row, col);
                        showImagePreviewDialog(imagePaths);
                    }
                }
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void refreshTable() {
        model.setRowCount(0);
        WasteDAO wasteDAO = new WasteDAO();
        wasteDAO.fetchWasteData(userId, model);
    }

    private void showImagePreviewDialog(String imagePaths) {
        if (imagePaths == null || imagePaths.equals("No images") || imagePaths.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No images available for this waste item.");
            return;
        }
        
        JDialog dialog = new JDialog(frame, "Image Preview", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel imagePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] paths = imagePaths.split(", ");
        boolean imagesLoaded = false;
        
        for (String path : paths) {
            if (path != null && !path.trim().isEmpty()) {
                try {
                    ImageIcon originalIcon = new ImageIcon(path.trim());
                    if (originalIcon.getIconWidth() <= 0) {
                        System.out.println("Failed to load image: " + path);
                        continue;
                    }
                    
                    Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(300, -1, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    imagePanel.add(imageLabel);
                    imagesLoaded = true;
                } catch (Exception e) {
                    System.out.println("Error loading image: " + path);
                    e.printStackTrace();
                }
            }
        }
        
        if (!imagesLoaded) {
            JOptionPane.showMessageDialog(frame, "Failed to load images.");
            return;
        }
        
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setSize(650, 500);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

}
