package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.logging.Logger;
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

    public WindowShopping(User usuario, MainController mc) {
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
        tableModel.addColumn("ID");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Descripción");
        tableModel.addColumn("Palabras Clave");
        tableModel.addColumn("Precio");
        tableModel.addColumn("Descuento");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Propietario");
        tableModel.addColumn("Foto");
        for (ItemDTO item : MainController.ReceiveItems()) {
            Object[] rowData = {item.id(), item.type(), item.name(), item.description(), item.keywords(),
                    item.price(), item.discount(), item.stock(), item.parent()};
            tableModel.addRow(rowData);
        }
        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 9 ? ImageIcon.class : Object.class;
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
        JButton btnCarrito = new JButton("Guardar en carrito");
        bottomPanel.add(btnCarrito);
        frame.add(panel, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                for(int i = 0; i < tableModel.getRowCount(); i++){
                    tableModel.removeRow(i);
                }
                // Add filtering and retrieval logic here
                for(ItemDTO item : MainController.ReceiveItems()) {
                    if(item.type().equals(searchComboBox.getSelectedItem())){
                        if(item.name().contains(searchTerm)||searchTerm.isBlank()||searchTerm.isEmpty()){}
                        Object[] rowData = {item.id(), item.type(), item.name(), item.description(), item.keywords(),
                                item.price(), item.discount(), item.stock(), item.parent()};
                        tableModel.addRow(rowData);
                    }
                }
                tableModel.fireTableDataChanged();
            }
        });
        btnBack.addActionListener(e -> {
            frame.dispose();
            new WindowHome(usuario, mc);
        });
        btnCarrito.addActionListener(e -> {
            MainController.ReceiveItems().get(table.getSelectedRow());
            Logger.getLogger().info("Se añadio el producto con id:"+tableModel.getValueAt(
                    table.getSelectedRow(), 0).toString());
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new WindowShopping(null, new MainController());
    }

    private static class ImageRenderer extends DefaultTableCellRenderer {
        private static final int MAX_IMAGE_WIDTH = 100;
        private static final int MAX_IMAGE_HEIGHT = 100;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
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
}
