package org.example.GUI;

import org.example.DAO.BookingDAO;
import org.example.DAO.WasteDAO;
import org.example.Model.Waste;
import org.example.Service.ImageRenderer;
import org.example.Util.GUITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
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
        String[] columns = {"ID", "Description", "Generator", "Quantity", "Images", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;  // Only allow editing of the Action column
            }
        };
        
        wasteTable = new JTable(tableModel);
        GUITheme.styleTable(wasteTable);
        
        // Configure table properties
        wasteTable.setRowHeight(50);
        wasteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        wasteTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        wasteTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        wasteTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        wasteTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        wasteTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        // Set custom renderer for images column
        wasteTable.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());

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
            tableModel.addRow(new Object[]{
                waste.getId(),
                waste.getDescription(),
                waste.getGeneratorName(),
                waste.getRemainingQuantity() + " kg",
                waste.getImagePath() != null ? waste.getImagePath() : "No images",
                "Select Quantity"
            });
        }

        // Add action button renderer and editor
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.parseInt(e.getActionCommand());
                int wasteId = (int) table.getModel().getValueAt(modelRow, 0);
                String quantityStr = ((String) table.getModel().getValueAt(modelRow, 3))
                    .replace(" kg", "");
                double availableQuantity = Double.parseDouble(quantityStr);
                handleQuantitySelection(modelRow, wasteId, availableQuantity);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(wasteTable, action, 5);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
    }

    private void setupTableListeners() {
        wasteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = wasteTable.rowAtPoint(evt.getPoint());
                int col = wasteTable.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && col == 4) {
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

    private void handleQuantitySelection(int row, int wasteId, double availableQuantity) {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JLabel availableLabel = new JLabel("Available Quantity: " + availableQuantity + " kg");
        JLabel quantityLabel = new JLabel("Enter quantity to book (kg):");
        JTextField quantityField = new JTextField(10);
        
        panel.add(availableLabel);
        panel.add(quantityLabel);
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Select Quantity",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                double requestedQuantity = Double.parseDouble(quantityField.getText().trim());
                
                // Validate quantity
                if (requestedQuantity <= 0) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Quantity must be greater than 0",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                if (requestedQuantity > availableQuantity) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Requested quantity cannot exceed available quantity",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Show confirmation dialog with Book button
                showBookingConfirmation(wasteId, requestedQuantity);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Please enter a valid number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void showBookingConfirmation(int wasteId, double requestedQuantity) {
        JPanel confirmPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        confirmPanel.add(new JLabel("Selected Quantity: " + requestedQuantity + " kg"));
        confirmPanel.add(new JLabel("Do you want to proceed with booking?"));

        int confirm = JOptionPane.showConfirmDialog(
            frame,
            confirmPanel,
            "Confirm Booking",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Proceed with booking
            BookingDAO bookingDAO = new BookingDAO();
            if (bookingDAO.bookWaste(wasteId, biogasCompanyId, requestedQuantity)) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Successfully booked " + requestedQuantity + " kg of waste!",
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

    // Add this ButtonColumn inner class if you don't have it already
    public class ButtonColumn extends AbstractCellEditor
            implements TableCellRenderer, TableCellEditor, ActionListener {
        private JButton renderButton;
        private JButton editButton;
        private String text;
        private Action action;

        public ButtonColumn(JTable table, Action action, int column) {
            this.action = action;
            renderButton = new JButton();
            editButton = new JButton();
            editButton.setFocusPainted(false);
            editButton.addActionListener(this);

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer(this);
            columnModel.getColumn(column).setCellEditor(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (hasFocus) {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            } else if (isSelected) {
                renderButton.setForeground(table.getSelectionForeground());
                renderButton.setBackground(table.getSelectionBackground());
            } else {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }

            renderButton.setText((value == null) ? "" : value.toString());
            return renderButton;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            text = (value == null) ? "" : value.toString();
            editButton.setText(text);
            return editButton;
        }

        @Override
        public Object getCellEditorValue() {
            return text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            ActionEvent event = new ActionEvent(
                wasteTable,
                ActionEvent.ACTION_PERFORMED,
                "" + wasteTable.getSelectedRow()
            );
            action.actionPerformed(event);
        }

        public void setMnemonic(int key) {
            renderButton.setMnemonic(key);
            editButton.setMnemonic(key);
        }
    }
} 