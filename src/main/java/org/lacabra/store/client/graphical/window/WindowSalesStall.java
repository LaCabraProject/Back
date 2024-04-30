package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WindowSalesStall extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private List<Item> lista = new ArrayList<>();

    public WindowSalesStall(User usuario, MainController mc) {
        initUI(usuario, mc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WindowSalesStall(null, new MainController()));
    }

    private void initUI(User usuario, MainController mc) {
        setTitle("Artículos a la venta");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        List<ItemDTO> itemDTOs = null;
        for (int i = 0; i < 5; i++) {
            String[] words = {"cabra", "goat", "beast"};
            Collection<String> keywords = new ArrayList<>(Arrays.asList(words));
            Item item = new Item(ObjectId.from(i + 220), ItemType.Decoration, "chair" + i, "a goated chair", keywords
                    , 20, 0, new BigInteger("2"), new User("mikel"));
            lista.add(item);
        }
//        itemDTOs=MainController.ReceiveItems();
        if (itemDTOs != null) {
            for (ItemDTO i : itemDTOs) {
                Item item = new Item(i.type(), i.name(), i.description(), i.keywords(), i.price(), i.discount(),
                        i.stock(), new User(i.parent()));
                lista.add(item);
            }
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Lista de Artículos para Vender:");
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

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Agregar los datos de los objetos Item a la tabla
        for (Item item : lista) {
            Object[] rowData = {item.id(), item.type(), item.name(), item.description(), item.keywords(),
                    item.price(), item.discount() + "%", item.stock(), "mikel"};
            tableModel.addRow(rowData);
        }

        JPanel bottomPanel = new JPanel(new GridLayout(9, 1));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField nameField = new JTextField(20);
        namePanel.add(new JLabel("Nombre:"));
        namePanel.add(nameField);
        bottomPanel.add(namePanel);

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

        JPanel panelPrecio = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelPrecio = new JLabel("Coste del producto:");
        JTextField precioField = new JTextField(20);
        panelPrecio.add(labelPrecio);
        panelPrecio.add(precioField);
        bottomPanel.add(panelPrecio);

        JPanel panelCantidad = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelCantidad = new JLabel("Cantidad del producto:");
        JTextField cantidadField = new JTextField(20);
        panelCantidad.add(labelCantidad);
        panelCantidad.add(cantidadField);
        bottomPanel.add(panelCantidad);

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelTipo = new JLabel("Tipo de producto:");
        JComboBox tipoField = new JComboBox(ItemType.values());
        panelTipo.add(labelTipo);
        panelTipo.add(tipoField);
        bottomPanel.add(panelTipo);

        JPanel panelCrearProducto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCrearProducto.add(btnAddItem);
        bottomPanel.add(panelCrearProducto);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton modificar = new JButton("Modificar seleccionado");
        JButton btnRemoveItem = new JButton("Eliminar artículo seleccionado");
        JButton btnClearList = new JButton("Borrar lista");
        JButton btnBack = new JButton("Volver al inicio");
        controlPanel.add(modificar);
        controlPanel.add(btnRemoveItem);
        controlPanel.add(btnClearList);
        controlPanel.add(btnBack);
        bottomPanel.add(controlPanel);

        modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex != -1) {
                    System.out.println(selectedRowIndex);
                    String itemName = addItemField.getText();
                    String[] words = itemName.split("\\s*,\\s*");
                    Collection<String> keywords = new ArrayList<>(Arrays.asList(words));
                    String itemDescription = descriptionField.getText();
                    String itemPhotoPath = photoField.getText();
                    ObjectId objId = ObjectId.from(itemName);
                    int numero = Integer.parseInt(precioField.getText());
                    BigInteger cantidad = new BigInteger(cantidadField.getText());
                    Item item = new Item(ObjectId.from(nameField.getText()), (ItemType) tipoField.getSelectedItem(),
                            nameField.getText(), itemDescription, keywords, numero, 0, cantidad, usuario);
                    //mc.PutItem(item);
                    List<Item> list = new ArrayList<>();
                    for (Item t : lista) {
                        if (t.equals(lista.get(selectedRowIndex))) {
                            list.add(item);
                        }
                        list.add(t);
                    }
                    lista = new ArrayList<>(list);
                    Object[] rowData = {item.id(), item.type(), item.name(), item.description(), item.keywords(),
                            item.price(), item.discount() + "%", item.stock(), usuario.id().get()};
                    tableModel.removeRow(selectedRowIndex);
                    tableModel.insertRow(selectedRowIndex, rowData);

                }
            }
        });

        btnAddItem.addActionListener(e -> {
            String itemName = addItemField.getText();
            String[] words = itemName.split("\\s*,\\s*");
            Collection<String> keywords = new ArrayList<>(Arrays.asList(words));
            String itemDescription = descriptionField.getText();
            String itemPhotoPath = photoField.getText();
            ObjectId objId = ObjectId.from(itemName);
            int numero = Integer.parseInt(precioField.getText());
            BigInteger cantidad = new BigInteger(cantidadField.getText());
            Item item = new Item(ObjectId.from(nameField.getText()), (ItemType) tipoField.getSelectedItem(),
                    nameField.getText(), itemDescription, keywords, numero, 0, cantidad, usuario);
            //mc.PutItem(item);
            lista.add(item);
            if (!itemName.isEmpty() && cantidadField.getText() != "" && precioField.getText() != "") {
                Item item1 = lista.get(lista.size() - 1);
                Object[] rowData = {item1.id(), item1.type(), item1.name(), item1.description(), item1.keywords(),
                        item1.price(), item1.discount() + "%", item1.stock(), usuario.id().get()};
                tableModel.addRow(rowData);
                addItemField.setText("");
                descriptionField.setText("");
                photoField.setText("");
                nameField.setText("");
                cantidadField.setText("");
                precioField.setText("");
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
                lista.remove(selectedRowIndex - 1);
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new HomeWindow(usuario, mc);
        });

        btnClearList.addActionListener(e -> tableModel.setRowCount(0));
        lista = new ArrayList<>();
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

