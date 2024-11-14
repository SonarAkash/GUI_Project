package org.example;

import org.example.GUI.MainWindow;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainWindow.createAndShowGUI();
        });
    }
}
