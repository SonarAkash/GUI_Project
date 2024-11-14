package org.example.GUI;

import org.example.DAO.UserDAO;
import org.example.Util.GUITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterForm {

    private JTextField tfName, tfEmail, tfPhone;
    private JPasswordField tfPassword;
    private JTextArea tfAddress;
    private JComboBox<String> cbRole;

    public RegisterForm() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(500, 700);
        GUITheme.styleFrame(registerFrame);

        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        // Title
        JLabel titleLabel = new JLabel("Register New Account", SwingConstants.CENTER);
        titleLabel.setFont(GUITheme.HEADING_FONT);
        titleLabel.setForeground(GUITheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // Form fields
        gbc.gridwidth = 1;
        int row = 1;

        // Name
        tfName = new JTextField();
        addFormField(mainPanel, "Name:", tfName, gbc, row++);
        
        // Email
        tfEmail = new JTextField();
        addFormField(mainPanel, "Email:", tfEmail, gbc, row++);
        
        // Password
        tfPassword = new JPasswordField();
        addFormField(mainPanel, "Password:", tfPassword, gbc, row++);
        
        // Phone
        tfPhone = new JTextField();
        addFormField(mainPanel, "Phone:", tfPhone, gbc, row++);

        // Address
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(GUITheme.LABEL_FONT);
        tfAddress = new JTextArea(3, 20);
        styleTextArea(tfAddress);
        gbc.gridy = row++;
        mainPanel.add(lblAddress, gbc);
        gbc.gridy = row++;
        mainPanel.add(new JScrollPane(tfAddress), gbc);

        // Role
        JLabel lblRole = new JLabel("Role:");
        lblRole.setFont(GUITheme.LABEL_FONT);
        String[] roles = {"generator", "biogas_company"};
        cbRole = new JComboBox<>(roles);
        styleComboBox(cbRole);
        gbc.gridy = row++;
        mainPanel.add(lblRole, gbc);
        gbc.gridy = row++;
        mainPanel.add(cbRole, gbc);

        // Register button
        JButton btnRegister = new JButton("Register");
        GUITheme.styleButton(btnRegister);
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        mainPanel.add(btnRegister, gbc);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                String email = tfEmail.getText();
                String password = new String(tfPassword.getPassword());
                String phone = tfPhone.getText();
                String address = tfAddress.getText();
                String role = (String) cbRole.getSelectedItem();

                // Register the user using the UserDAO class
                UserDAO userDAO = new UserDAO();
                boolean isRegistered = userDAO.registerUser(name, email, password, role, phone, address);

                if (isRegistered) {
                    JOptionPane.showMessageDialog(registerFrame, "Registration Successful!");

                    // Create and show the MainWindow on the EDT
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new MainWindow();  // Create and show the main window
                        }
                    });

                    registerFrame.dispose();  // Close the registration window
                } else {
                    JOptionPane.showMessageDialog(registerFrame, "Registration Failed. Please try again.");
                }
            }
        });

        registerFrame.add(new JScrollPane(mainPanel));
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setVisible(true);
    }

    private void addFormField(JPanel panel, String label, JTextField field, 
            GridBagConstraints gbc, int row) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(GUITheme.LABEL_FONT);
        styleTextField(field);
        
        gbc.gridy = row;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridx = 0;
    }

    private void styleTextField(JTextField field) {
        field.setFont(GUITheme.LABEL_FONT);
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GUITheme.PRIMARY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleTextArea(JTextArea area) {
        area.setFont(GUITheme.LABEL_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GUITheme.PRIMARY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(GUITheme.LABEL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(GUITheme.PRIMARY));
        ((JComponent) comboBox.getRenderer()).setPreferredSize(new Dimension(200, 30));
    }
}

