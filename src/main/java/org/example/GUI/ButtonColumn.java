package org.example.GUI;

import javax.swing.*;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JButton renderButton;
    private JButton editButton;
    private Action action;
    private String text;
    private JTable table;

    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;
        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        table.getColumnModel().getColumn(column).setCellRenderer(this);
        table.getColumnModel().getColumn(column).setCellEditor(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        renderButton.setText(value == null ? "" : value.toString());
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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
        ActionEvent event = new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, "" + table.getSelectedRow());
        action.actionPerformed(event);
    }
} 