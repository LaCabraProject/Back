package org.lacabra.store.client.windows;

import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowShopping {
    private JFrame frame;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;

    public WindowShopping(User usuario) {
        frame = new JFrame("Buscador");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JComboBox<ItemType> searchComboBox = new JComboBox<>(ItemType.values());
        topPanel.add(searchComboBox);

        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchButton = new JButton("Buscar");
        topPanel.add(searchButton);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Lista de artículos en venta:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Artículo");
        tableModel.addColumn("Descripción");
        tableModel.addColumn("Foto");
        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? ImageIcon.class : Object.class;
            }
        };
        table.setRowHeight(100); // Altura predeterminada de las filas
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumn("Foto").setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(bottomPanel, BorderLayout.SOUTH);
        JButton btnBack = new JButton("Volver al inicio");
        bottomPanel.add(btnBack);
        frame.add(panel, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                String selectedCategory = (String) searchComboBox.getSelectedItem();
                System.out.println("Searching for: " + searchTerm + " in category: " + selectedCategory);
                // Add filtering and retrieval logic here
            }
        });
        btnBack.addActionListener(e -> {
            frame.dispose();
            new WindowHome(usuario);
        });

        frame.setVisible(true);
    }

    private static class ImageRenderer extends DefaultTableCellRenderer {
        private static final int MAX_IMAGE_WIDTH = 100;
        private static final int MAX_IMAGE_HEIGHT = 100;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            if (value != null) {
                String imagePath = (String) value;
                ImageIcon icon = new ImageIcon(imagePath);
                Image image = icon.getImage();

                int width = image.getWidth(null);
                int height = image.getHeight(null);

                double aspectRatio = (double) width / height;
                int newWidth, newHeight;
                if (width > height) {
                    newWidth = MAX_IMAGE_WIDTH;
                    newHeight = (int) (newWidth / aspectRatio);
                } else {
                    newHeight = MAX_IMAGE_HEIGHT;
                    newWidth = (int) (newHeight * aspectRatio);
                }

                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                label.setIcon(scaledIcon);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            return label;
        }
    }

    public static void main(String[] args) {
        new WindowShopping(null);
    }
}
