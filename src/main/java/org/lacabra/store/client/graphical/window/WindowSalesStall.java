package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
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
import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WindowSalesStall extends DispatchedWindow {
    @Serial
    private final static long serialVersionUID = 1L;

    private DefaultTableModel tableModel;
    private JTable table;
    private List<Item> lista = new ArrayList<>();
    private MainController mc;
    private JScrollPane scrollPane;
    private JPanel panel = new JPanel();
    private JPanel bottomPanel, namePanel, addPanel, descriptionPanel, photoPanel, panelPrecio, panelCantidad,
            panelTipo, panelCrearProducto, controlPanel;
    private JTextField nameField, addItemField, descriptionField, photoField, precioField, cantidadField;
    private JButton btnAddItem, btnAddPhoto, modificar, btnRemoveItem, btnClearList, btnBack;
    private JLabel labelPrecio, labelCantidad, labelTipo, label;
    private JComboBox<ItemType> tipoField;
    private List<ItemDTO> itemDTOs;

    public WindowSalesStall(final WindowDispatcher wd) {
        super(wd);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        super.setDispatcher(wd);

        mc = wd.controller();
        if (mc == null)
            return;

        mc.auth().thenAccept((auth) -> {
            if (!auth) {
                this.close();
                mc.unauth();
                this.dispatch(AuthWindow.class);
                return;
            }
        });

        //ajustes iniciales
        {
            setTitle("Artículos a la venta");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            itemDTOs = null;
        }

        //carga de objetos
        {
            itemDTOs = mc.GET.Item.all().join();
            if (itemDTOs != null) {
                for (ItemDTO i : itemDTOs) {
                    Item item = new Item(i.type(), i.name(), i.description(), i.keywords(), i.price(), i.discount(),
                            i.stock(), new User(i.parent()));
                    lista.add(item);
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    String[] words = {"cabra", "goat", "beast"};
                    Collection<String> keywords = new ArrayList<>(Arrays.asList(words));
                    Item item = new Item(ObjectId.from(i + 220), ItemType.Decoration, "chair" + i, "a goated chair",
                            keywords
                            , 20, 0, new BigInteger("2"), new User("mikel"));
                    lista.add(item);
                }
            }
        }


        panel.setLayout(new BorderLayout());
        label = new JLabel("Lista de Artículos para Vender:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        //tabla
        {
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
            scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        // Agregar los datos de los objetos Item a la tabla
        {
            for (Item item : lista) {
                Object[] rowData = {item.id(), item.type(), item.name(), item.description(), item.keywords(),
                        item.price(), item.discount(), item.stock(), item.parent().id()};
                tableModel.addRow(rowData);
            }
        }

        //campos para rellenar en un nuevo objeto y botón para crear
        {
            bottomPanel = new JPanel(new GridLayout(9, 1));
            panel.add(bottomPanel, BorderLayout.SOUTH);

            namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            nameField = new JTextField(20);
            namePanel.add(new JLabel("Nombre:"));
            namePanel.add(nameField);
            bottomPanel.add(namePanel);

            addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            addItemField = new JTextField(20);
            btnAddItem = new JButton("Agregar Artículo");
            addPanel.add(new JLabel("Palabras clave:"));
            addPanel.add(addItemField);
            bottomPanel.add(addPanel);

            descriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            descriptionField = new JTextField(20);
            descriptionPanel.add(new JLabel("Descripción:"));
            descriptionPanel.add(descriptionField);
            bottomPanel.add(descriptionPanel);

            photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            photoField = new JTextField(20);
            btnAddPhoto = new JButton("Agregar Foto");
            photoPanel.add(new JLabel("Ruta de la Foto:"));
            photoPanel.add(photoField);
            photoPanel.add(btnAddPhoto);
            bottomPanel.add(photoPanel);

            panelPrecio = new JPanel(new FlowLayout(FlowLayout.CENTER));
            labelPrecio = new JLabel("Coste del producto:");
            precioField = new JTextField(20);
            panelPrecio.add(labelPrecio);
            panelPrecio.add(precioField);
            bottomPanel.add(panelPrecio);

            panelCantidad = new JPanel(new FlowLayout(FlowLayout.CENTER));
            labelCantidad = new JLabel("Cantidad del producto:");
            cantidadField = new JTextField(20);
            panelCantidad.add(labelCantidad);
            panelCantidad.add(cantidadField);
            bottomPanel.add(panelCantidad);

            panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER));
            labelTipo = new JLabel("Tipo de producto:");
            tipoField = new JComboBox(ItemType.values());
            panelTipo.add(labelTipo);
            panelTipo.add(tipoField);
            bottomPanel.add(panelTipo);


            panelCrearProducto = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelCrearProducto.add(btnAddItem);
            bottomPanel.add(panelCrearProducto);
        }

        //Botones de parte baja de pantalla
        {
            controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            modificar = new JButton("Modificar seleccionado");
            btnRemoveItem = new JButton("Eliminar artículo seleccionado");
            btnClearList = new JButton("Borrar todo");
            btnBack = new JButton("Volver al inicio");
            controlPanel.add(modificar);
            controlPanel.add(btnRemoveItem);
            controlPanel.add(btnClearList);
            controlPanel.add(btnBack);
            bottomPanel.add(controlPanel);
        }

        //actionListeners
        {
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
                                nameField.getText(), itemDescription, keywords, numero, 0, cantidad,
                                new User(mc.getUser().id()));
                        //*linea para mandar el item(no hemos decidido)
                        List<Item> list = new ArrayList<>();
                        for (Item t : lista) {
                            if (t.equals(lista.get(selectedRowIndex))) {
                                list.add(item);
                            }
                            list.add(t);
                        }
                        lista = new ArrayList<>(list);
                        Object[] rowData = {item.id(), item.type(), item.name(), item.description(), item.keywords(),
                                item.price(), item.discount() + "%", item.stock(),
                                new User(mc.getUser().id()).id().get()};
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
                        nameField.getText(), itemDescription, keywords, numero, 0, cantidad,
                        new User(mc.getUser().id()));
                //mc.PutItem(item);
                lista.add(item);
                if (!itemName.isEmpty() && cantidadField.getText() != "" && precioField.getText() != "") {
                    Item item1 = lista.get(lista.size() - 1);
                    Object[] rowData = {item1.id(), item1.type(), item1.name(), item1.description(), item1.keywords(),
                            item1.price(), item1.discount(), item1.stock(), new User(mc.getUser().id()).id().get()};
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
                this.dispatch(HomeWindow.class);
            });

            btnClearList.addActionListener(e -> {
                tableModel.setRowCount(0);
                lista = new ArrayList<>();
            });
        }
        panel.add(this.footer(true), BorderLayout.SOUTH);

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

