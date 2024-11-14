package org.example.Util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class GUITheme {
    // Colors
    public static final Color PRIMARY = new Color(41, 128, 185);    // Blue
    public static final Color SECONDARY = new Color(44, 62, 80);    // Dark Blue
    public static final Color BACKGROUND = new Color(236, 240, 241); // Light Gray
    public static final Color ACCENT = new Color(46, 204, 113);     // Green
    public static final Color WARNING = new Color(231, 76, 60);     // Red
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80); // Dark Blue
    public static final Color TEXT_LIGHT = Color.WHITE;

    // Fonts
    public static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);

    // Styling methods
    public static void styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY);
            }
        });
    }

    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND);
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public static void styleTable(JTable table) {
        table.setFont(LABEL_FONT);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(PRIMARY.brighter());
        table.setSelectionForeground(TEXT_PRIMARY);
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setBackground(SECONDARY);
        header.setForeground(TEXT_LIGHT);
        header.setFont(BUTTON_FONT);
    }

    public static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }
} 