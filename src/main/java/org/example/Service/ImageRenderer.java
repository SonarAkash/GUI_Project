package org.example.Service;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.TableCellRenderer;
import java.io.File;

public class ImageRenderer extends JLabel implements TableCellRenderer {
    private static final int THUMBNAIL_SIZE = 100;
    private static final int SPACING = 5;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (value == null) {
            setText("No images");
            return this;
        }

        // Create panel for images
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, SPACING, SPACING));
        
        // Set background colors based on selection
        if (isSelected) {
            imagePanel.setBackground(table.getSelectionBackground());
        } else {
            imagePanel.setBackground(table.getBackground());
        }

        // Split the comma-separated image paths
        String[] paths = value.toString().split(", ");
        
        // Add images to panel
        for (String path : paths) {
            if (path != null && !path.trim().isEmpty()) {
                File imageFile = new File(path);
                System.out.println(path + " :: " + imageFile + " : ");
                if (imageFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(path);
                    Image scaledImage = originalIcon.getImage()
                            .getScaledInstance(THUMBNAIL_SIZE, THUMBNAIL_SIZE, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    imagePanel.add(imageLabel);
                }
            }
        }

        // If no valid images were added
        if (imagePanel.getComponentCount() == 0) {
            setText("No valid images");
            return this;
        }

        return imagePanel;
    }
}
