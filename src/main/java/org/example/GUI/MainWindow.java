package org.example.GUI;

import javax.swing.*;

import org.example.Util.GUITheme;

import java.awt.*;

public class MainWindow {

    public static void createAndShowGUI() {
    JFrame frame = new JFrame("Biogas Waste Management System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 400);
    GUITheme.styleFrame(frame);

    JPanel mainPanel = GUITheme.createStyledPanel();
    mainPanel.setLayout(new GridBagLayout());
    
    // Create title
    JLabel titleLabel = new JLabel("Biogas Waste Management");
    titleLabel.setFont(GUITheme.HEADING_FONT);
    titleLabel.setForeground(GUITheme.TEXT_PRIMARY);
    
    // Create buttons panel
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
    buttonPanel.setOpaque(false);
    
    JButton btnRegister = new JButton("Register");
    JButton btnLogin = new JButton("Login");
    
    GUITheme.styleButton(btnRegister);
    GUITheme.styleButton(btnLogin);
    
    buttonPanel.add(btnRegister);
    buttonPanel.add(btnLogin);
    
    // Layout components
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 30, 0);
    mainPanel.add(titleLabel, gbc);
    
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.ipadx = 100;
    mainPanel.add(buttonPanel, gbc);
    
    frame.add(mainPanel);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    // Add your existing action listeners here
    btnRegister.addActionListener(e -> new RegisterForm());
    btnLogin.addActionListener(e -> {
        new LoginForm();
        frame.dispose();
    });
}
}
