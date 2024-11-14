package org.example.GUI;

import org.example.DAO.UserDAO;
import org.example.Util.GUITheme;

import javax.swing.*;
import java.awt.*;

public class LoginForm {
    public LoginForm() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(400, 500);
        GUITheme.styleFrame(loginFrame);

        // Main panel with padding
        JPanel mainPanel = GUITheme.createStyledPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(GUITheme.HEADING_FONT);
        titleLabel.setForeground(GUITheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Email field
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(GUITheme.LABEL_FONT);
        JTextField tfEmail = new JTextField(20);
        styleTextField(tfEmail);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(lblEmail, gbc);
        gbc.gridy = 2;
        mainPanel.add(tfEmail, gbc);

        // Password field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(GUITheme.LABEL_FONT);
        JPasswordField tfPassword = new JPasswordField(20);
        styleTextField(tfPassword);
        
        gbc.gridy = 3;
        mainPanel.add(lblPassword, gbc);
        gbc.gridy = 4;
        mainPanel.add(tfPassword, gbc);

        // Login button
        JButton btnLogin = new JButton("Login");
        GUITheme.styleButton(btnLogin);
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 10, 10, 10);
        mainPanel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String email = tfEmail.getText();
            String password = new String(tfPassword.getPassword());

            UserDAO userDAO = new UserDAO();
            int userId = userDAO.validateLogin(email, password);

            if (userId != -1) {
                String role = userDAO.getRoleByEmail(email);
                if (role.equals("generator")) {
                    new GeneratorDashboard(userId);
                } else if (role.equals("biogas_company")) {
                    new BiogasCompanyDashboard(userId);
                }
                loginFrame.dispose();
            } else {
                showErrorMessage(loginFrame, "Invalid login credentials.");
            }
        });

        loginFrame.add(mainPanel);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(GUITheme.LABEL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GUITheme.PRIMARY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void showErrorMessage(JFrame parent, String message) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}