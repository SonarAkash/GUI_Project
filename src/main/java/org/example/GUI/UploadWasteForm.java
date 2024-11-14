package org.example.GUI;

import org.example.Service.ImageUploadService;
import org.example.Util.GUITheme;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class UploadWasteForm {
    private final JFrame frame;
    private final List<File> selectedImages;
    private final JLabel imageCountLabel;
    private final JTextArea txtDescription;
    private final JButton btnSubmit;

    public UploadWasteForm(int userId) {
        selectedImages = new ArrayList<>();
        
        // Create and setup main frame
        frame = new JFrame("Upload Waste");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        GUITheme.styleFrame(frame);

        // Main panel with padding
        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Header
        JLabel titleLabel = new JLabel("Upload Waste Details", SwingConstants.CENTER);
        titleLabel.setFont(GUITheme.HEADING_FONT);
        titleLabel.setForeground(GUITheme.TEXT_PRIMARY);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Description area
        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setFont(GUITheme.LABEL_FONT);
        txtDescription = new JTextArea(5, 30);
        txtDescription.setFont(GUITheme.LABEL_FONT);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GUITheme.PRIMARY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Image upload section
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.setOpaque(false);
        
        JButton btnUploadImages = new JButton("Choose Images");
        GUITheme.styleButton(btnUploadImages);
        imageCountLabel = new JLabel("No images selected");
        imageCountLabel.setFont(GUITheme.LABEL_FONT);
        
        imagePanel.add(btnUploadImages);
        imagePanel.add(imageCountLabel);

        // Submit button
        btnSubmit = new JButton("Upload Waste");
        GUITheme.styleButton(btnSubmit);
        btnSubmit.setBackground(GUITheme.ACCENT);

        // Add components to form panel
        formPanel.add(lblDescription, gbc);
        gbc.insets = new Insets(5, 10, 15, 10);
        formPanel.add(scrollPane, gbc);
        gbc.insets = new Insets(10, 10, 10, 10);
        formPanel.add(imagePanel, gbc);
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnSubmit, BorderLayout.SOUTH);

        // Add action listeners
        setupActionListeners(userId, btnUploadImages);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void setupActionListeners(int userId, JButton btnUploadImages) {
        btnUploadImages.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"
            ));
            
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                selectedImages.clear();
                File[] files = fileChooser.getSelectedFiles();
                for (File file : files) {
                    selectedImages.add(file);
                }
                imageCountLabel.setText(selectedImages.size() + " image(s) selected");
                imageCountLabel.setForeground(GUITheme.ACCENT);
            }
        });

        btnSubmit.addActionListener(e -> handleSubmission(userId));
    }

    private void handleSubmission(int userId) {
        if (txtDescription.getText().trim().isEmpty()) {
            showError("Please provide a description.");
            return;
        }
        if (selectedImages.isEmpty()) {
            showError("Please select at least one image.");
            return;
        }

        btnSubmit.setEnabled(false);
        btnSubmit.setText("Uploading...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                ImageUploadService uploadService = new ImageUploadService();
                return uploadService.uploadWasteWithImages(
                    txtDescription.getText().trim(),
                    selectedImages,
                    userId
                );
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(
                            frame,
                            "Waste uploaded successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        frame.dispose();
                    } else {
                        showError("Failed to upload waste. Please try again.");
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("Upload Waste");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("An error occurred during upload.");
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Upload Waste");
                }
            }
        };
        worker.execute();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            frame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
