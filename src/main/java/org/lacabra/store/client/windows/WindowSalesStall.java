package org.lacabra.store.client.windows;

import org.lacabra.store.client.data.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class WindowSalesStall extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    public WindowSalesStall(User usuario) {
        initUI(usuario);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WindowSalesStall(null));
    }

    private void initUI(User usuario) {
        setTitle("Artículos a la venta");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Lista de Artículos para Vender:");
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

        JPanel bottomPanel = new JPanel(new GridLayout(5, 1));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField addItemField = new JTextField(20);
        JButton btnAddItem = new JButton("Agregar Artículo");
        addPanel.add(new JLabel("Artículo:"));
        addPanel.add(addItemField);
        bottomPanel.add(addPanel);

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField descriptionField = new JTextField(20);
        descriptionPanel.add(new JLabel("Descripción:"));
        descriptionPanel.add(descriptionField);
        bottomPanel.add(descriptionPanel);

        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField photoField = new JTextField(20);
        JButton btnAddPhoto = new JButton("Agregar Foto");
        photoPanel.add(new JLabel("Ruta de la Foto:"));
        photoPanel.add(photoField);
        photoPanel.add(btnAddPhoto);
        bottomPanel.add(photoPanel);

        JPanel panelCrearProducto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCrearProducto.add(btnAddItem);
        bottomPanel.add(panelCrearProducto);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRemoveItem = new JButton("Eliminar Artículo Seleccionado");
        JButton btnClearList = new JButton("Limpiar Lista");
        JButton btnBack = new JButton("Volver al inicio");
        controlPanel.add(btnRemoveItem);
        controlPanel.add(btnClearList);
        controlPanel.add(btnBack);
        bottomPanel.add(controlPanel);

        btnAddItem.addActionListener(e -> {
            String itemName = addItemField.getText();
            String itemDescription = descriptionField.getText();
            String itemPhotoPath = photoField.getText();

            if (!itemName.isEmpty()) {
                String[] rowData = {itemName, itemDescription, itemPhotoPath};
                tableModel.addRow(rowData);
                addItemField.setText("");
                descriptionField.setText("");
                photoField.setText("");
            }
        });

        btnAddPhoto.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                photoField.setText(filePath);
            }
        });

        btnRemoveItem.addActionListener(e -> {
            int selectedRowIndex = table.getSelectedRow();
            if (selectedRowIndex != -1) {
                tableModel.removeRow(selectedRowIndex);
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new WindowHome(usuario);
        });

        btnClearList.addActionListener(e -> tableModel.setRowCount(0));
        add(panel);
        setVisible(true);
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

