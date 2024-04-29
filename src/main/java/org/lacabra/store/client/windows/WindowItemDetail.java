package org.lacabra.store.client.windows;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class WindowItemDetail extends JFrame {

    private List<String> reseñas;

    public WindowItemDetail() {
        super("Detalle del Artículo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel barraNavegacion = crearBarraNavegacion();
        panelPrincipal.add(barraNavegacion, BorderLayout.NORTH);

        ImageIcon iconoImagen = new ImageIcon("ruta/a/imagen.jpg");
        JLabel etiquetaImagen = new JLabel(iconoImagen);
        etiquetaImagen.setPreferredSize(new Dimension(300, 300));
        panelPrincipal.add(etiquetaImagen, BorderLayout.WEST);

        JTabbedPane panelPestanas = crearPanelPestanas();
        panelPrincipal.add(panelPestanas, BorderLayout.CENTER);

        JPanel piePagina = crearPiePagina();
        panelPrincipal.add(piePagina, BorderLayout.SOUTH);

        add(panelPrincipal);

        setVisible(true);
    }

    private JPanel crearBarraNavegacion() {
        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setLayout(new GridLayout(1, 4));
        barraNavegacion.setBackground(Color.LIGHT_GRAY);
        barraNavegacion.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton botonInicio = new JButton("Inicio", new ImageIcon("ruta/a/home.png"));
        botonInicio.setHorizontalTextPosition(SwingConstants.CENTER);
        botonInicio.setVerticalTextPosition(SwingConstants.BOTTOM);
        botonInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonInicio);

        JButton botonCategoria = new JButton("Categoría", new ImageIcon("ruta/a/category.png"));
        botonCategoria.setHorizontalTextPosition(SwingConstants.CENTER);
        botonCategoria.setVerticalTextPosition(SwingConstants.BOTTOM);
        botonCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonCategoria);

        JButton botonCarrito = new JButton("Carrito", new ImageIcon("ruta/a/cart.png"));
        botonCarrito.setHorizontalTextPosition(SwingConstants.CENTER);
        botonCarrito.setVerticalTextPosition(SwingConstants.BOTTOM);
        botonCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonCarrito);

        JButton botonBuscar = new JButton("Buscar", new ImageIcon("ruta/a/search.png"));
        botonBuscar.setHorizontalTextPosition(SwingConstants.CENTER);
        botonBuscar.setVerticalTextPosition(SwingConstants.BOTTOM);
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonBuscar);

        return barraNavegacion;
    }

    private JTabbedPane crearPanelPestanas() {
        JTabbedPane panelPestanas = new JTabbedPane();

        JPanel panelResenas = new JPanel();
        panelResenas.setLayout(new BorderLayout());
        panelResenas.setBorder(new CompoundBorder(new TitledBorder("Reseñas del Producto"), new EmptyBorder(10, 10, 10, 10)));

        reseñas = new ArrayList<>();
        reseñas.add("¡Excelente producto!");
        reseñas.add("Me encantó, muy recomendado.");
        reseñas.add("Buena calidad y buen precio.");

        JTextArea areaResenas = new JTextArea();
        areaResenas.setEditable(false);
        actualizarResenas(areaResenas);
        JScrollPane scrollPane = new JScrollPane(areaResenas);
        panelResenas.add(scrollPane, BorderLayout.CENTER);

        JButton botonAgregarResena = new JButton("Agregar Reseña");
        botonAgregarResena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevaResena = JOptionPane.showInputDialog("Escribe tu reseña:");
                if (nuevaResena != null && !nuevaResena.isEmpty()) {
                    reseñas.add(nuevaResena);
                    actualizarResenas(areaResenas);
                }
            }
        });
        panelResenas.add(botonAgregarResena, BorderLayout.SOUTH);

        panelPestanas.addTab("Reseñas", panelResenas);

        return panelPestanas;
    }

    private void actualizarResenas(JTextArea areaResenas) {
        areaResenas.setText("");
        for (String resena : reseñas) {
            areaResenas.append("- " + resena + "\n");
        }
    }

    private JPanel crearPiePagina() {
        JPanel piePagina = new JPanel();
        piePagina.setLayout(new GridLayout(1, 5));
        piePagina.setBackground(Color.LIGHT_GRAY);
        piePagina.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel etiquetaEnlace1 = new JLabel("Política de Privacidad");
        etiquetaEnlace1.setForeground(Color.BLUE);
        etiquetaEnlace1.addMouseListener((MouseListener) new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        piePagina.add(etiquetaEnlace1);

        JLabel etiquetaEnlace2 = new JLabel("Términos de Uso");
        etiquetaEnlace2.setForeground(Color.BLUE);
        etiquetaEnlace2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        piePagina.add(etiquetaEnlace2);

        JButton botonFacebook = new JButton(new ImageIcon("ruta/a/facebook.png"));
        botonFacebook.setPreferredSize(new Dimension(32, 32));
        botonFacebook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        piePagina.add(botonFacebook);

        JButton botonTwitter = new JButton(new ImageIcon("ruta/a/twitter.png"));
        botonTwitter.setPreferredSize(new Dimension(32, 32));
        botonTwitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        piePagina.add(botonTwitter);

        JButton botonInstagram = new JButton(new ImageIcon("ruta/a/instagram.png"));
        botonInstagram.setPreferredSize(new Dimension(32, 32));
        botonInstagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        piePagina.add(botonInstagram);

        return piePagina;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WindowItemDetail();
            }
        });
    }
}

