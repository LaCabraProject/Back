package org.lacabra.store.client.windows;

import org.lacabra.store.client.Controller.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowShoppingCart extends JFrame {
    private JComboBox<String> comboBoxMetodoEnvio;
    private JList<String> listaProductos;
    private DefaultListModel<String> modeloListaProductos;
    private JTextField campoCostoTotal;
    private JButton botonEliminarProducto;
    private JButton botonAplicarCupon;
    private JButton botonRealizarPago;
    private MainController mc;

    public WindowShoppingCart(MainController mc) {
        this.mc = mc;
        setTitle("Carrito de Compra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        UIManager.put("ComboBox.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);

        setTitle("Carrito de Compra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 10, 0));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelIzquierdoSuperior = new JPanel(new GridLayout(2, 1));
        JLabel etiquetaMetodoEnvio = new JLabel("Método de Envío:");
        comboBoxMetodoEnvio = new JComboBox<>(new String[]{"Estándar", "Express", "Entrega al día siguiente"});
        panelIzquierdoSuperior.add(etiquetaMetodoEnvio);
        panelIzquierdoSuperior.add(comboBoxMetodoEnvio);
        panelSuperior.add(panelIzquierdoSuperior);

        JPanel panelDerechoSuperior = new JPanel(new GridLayout(1, 2));
        JLabel etiquetaCostoTotal = new JLabel("Costo Total:");
        campoCostoTotal = new JTextField();
        campoCostoTotal.setEditable(false);
        campoCostoTotal.setColumns(10);
        panelDerechoSuperior.add(etiquetaCostoTotal);
        panelDerechoSuperior.add(campoCostoTotal);
        panelSuperior.add(panelDerechoSuperior);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        modeloListaProductos = new DefaultListModel<>();
        listaProductos = new JList<>(modeloListaProductos);
        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane panelDesplazableProductos = new JScrollPane(listaProductos);
        panelCentral.add(panelDesplazableProductos, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new GridLayout(1, 3, 10, 0));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        botonEliminarProducto = new JButton("Eliminar Producto");
        botonAplicarCupon = new JButton("Aplicar Cupón");
        botonRealizarPago = new JButton("Realizar Pago");

        panelInferior.add(botonEliminarProducto);
        panelInferior.add(botonAplicarCupon);
        panelInferior.add(botonRealizarPago);

        add(panelInferior, BorderLayout.SOUTH);

        comboBoxMetodoEnvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        listaProductos.addListSelectionListener(e -> {

        });

        botonEliminarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeloListaProductos.remove(listaProductos.getSelectedIndex());
            }
        });

        botonAplicarCupon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        botonRealizarPago.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campoCostoTotal.setText("$0");
                modeloListaProductos.clear();
            }
        });

        modeloListaProductos.addElement("Producto 1");
        modeloListaProductos.addElement("Producto 2");
        modeloListaProductos.addElement("Producto 3");

        campoCostoTotal.setText("$100.00");
    }

    public static void main(String[] args) {
        WindowShoppingCart app = new WindowShoppingCart(new MainController());
        app.ejecutar();
    }

    public void ejecutar() {
        setVisible(true);
    }

    private double calcularCostoTotal() {
        double costoTotal = 0.0;
        return costoTotal;
    }
}