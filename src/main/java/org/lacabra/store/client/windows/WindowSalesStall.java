package org.lacabra.store.client.windows;

import org.lacabra.store.client.Controller.MainController;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class WindowSalesStall extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    public WindowSalesStall(User usuario, MainController mc) {
        initUI(usuario, mc);
    }

    private void initUI(User usuario, MainController mc) {
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

        JPanel bottomPanel = new JPanel(new GridLayout(8, 1));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField addItemField = new JTextField(20);
        JButton btnAddItem = new JButton("Agregar Artículo");
        addPanel.add(new JLabel("Palabras clave:"));
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

        JPanel panelPrecio=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelPrecio= new JLabel("Coste del producto:");
        JTextField precioField = new JTextField(20);
        panelPrecio.add(labelPrecio);
        panelPrecio.add(precioField);
        bottomPanel.add(panelPrecio);

        JPanel panelCantidad=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelCantidad= new JLabel("Cantidad del producto:");
        JTextField cantidadField = new JTextField(20);
        panelCantidad.add(labelCantidad);
        panelCantidad.add(cantidadField);
        bottomPanel.add(panelCantidad);

        JPanel panelTipo =new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelTipo= new JLabel("Tipo de producto:");
        JComboBox tipoField = new JComboBox(ItemType.values());
        panelTipo.add(labelTipo);
        panelTipo.add(tipoField);
        bottomPanel.add(panelTipo);

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
            String[] words = itemName.split("[,\\s]+");
            Collection<String> keywords = new ArrayList<>(Arrays.asList(words));
            String itemDescription = descriptionField.getText();
            String itemPhotoPath = photoField.getText();
            ObjectId objId=ObjectId.from(itemName);
            Number numero=Double.parseDouble(precioField.getText());
            BigInteger cantidad=new BigInteger(cantidadField.getText());
            Item item= new Item((ItemType) tipoField.getSelectedItem(), itemName,itemDescription,keywords,numero,0,cantidad, usuario);
            mc.PutItem(item);
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
            new WindowHome(usuario, mc);
        });

        btnClearList.addActionListener(e -> tableModel.setRowCount(0));
        add(panel);
        setVisible(true);
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
        SwingUtilities.invokeLater(() -> new WindowSalesStall(null,new MainController()));
    }
}

