package org.lacabra.store.client.windows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WindowItemDetail extends JFrame {
    public WindowItemDetail() {
        super("Detalle del Artículo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setLayout(new GridLayout(1, 3));
        barraNavegacion.setBackground(Color.LIGHT_GRAY);

        JButton botonInicio = new JButton("Inicio");
        botonInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonInicio);

        JButton botonCategoria = new JButton("Categoría");
        botonCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonCategoria);

        JButton botonCarrito = new JButton("Carrito");
        botonCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        barraNavegacion.add(botonCarrito);

        panelPrincipal.add(barraNavegacion, BorderLayout.NORTH);

        ImageIcon iconoImagen = new ImageIcon("ruta/a/imagen.jpg");
        JLabel etiquetaImagen = new JLabel(iconoImagen);
        etiquetaImagen.setPreferredSize(new Dimension(300, 300));
        panelPrincipal.add(etiquetaImagen, BorderLayout.WEST);

        JTabbedPane panelPestanas = new JTabbedPane();
        panelPrincipal.add(panelPestanas, BorderLayout.CENTER);

        JPanel panelDesc = new JPanel();
        panelDesc.setLayout(new BorderLayout());
        panelDesc.add(new JLabel("Descripción del Producto"), BorderLayout.NORTH);
        panelDesc.add(new JLabel("Este es un sombrero compacto y ligero perfecto para la playa, la montaña o la ciudad. Hecho de algodón 100% transpirable con agujeros de ventilación, cuenta con una parte superior plana y un ala de anchura media para protegerte del sol. La corona es sin estructura y mide 8 cm de profundidad. Fácil de mantener: simplemente lávalo y sécalo a la sombra"), BorderLayout.CENTER);
        panelPestanas.addTab("Descripción", panelDesc);


        JPanel panelEspecificaciones = new JPanel();
        panelEspecificaciones.setLayout(new GridLayout(6, 2));
        panelEspecificaciones.add(new JLabel("Color:"));
        panelEspecificaciones.add(new JLabel("Negro"));
        panelEspecificaciones.add(new JLabel("Añadir al carrito:"));
        panelEspecificaciones.add(new JButton("Añadir"));
        panelEspecificaciones.add(new JLabel("Entrega:"));
        panelEspecificaciones.add(new JLabel("Urgente: 7 de mayo, Ordinaria: 7-10 de mayo"));
        panelEspecificaciones.add(new JLabel("Devoluciones fáciles:"));
        panelEspecificaciones.add(new JLabel("Sí"));
        panelEspecificaciones.add(new JLabel("Valoración:"));
        panelEspecificaciones.add(new JLabel("4.20 (40 reseñas)"));
        panelPestanas.addTab("Especificaciones", panelEspecificaciones);

        JPanel piePagina = new JPanel();
        piePagina.setLayout(new GridLayout(1, 5));
        piePagina.setBackground(Color.LIGHT_GRAY);

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

        JLabel etiquetaRedSocial1 = new JLabel(new ImageIcon("ruta/a/facebook.png"));
        piePagina.add(etiquetaRedSocial1);

        JLabel etiquetaRedSocial2 = new JLabel(new ImageIcon("ruta/a/twitter.png"));
        piePagina.add(etiquetaRedSocial2);

        JLabel etiquetaRedSocial3 = new JLabel(new ImageIcon("ruta/a/instagram.png"));
        piePagina.add(etiquetaRedSocial3);

        panelPrincipal.add(piePagina, BorderLayout.SOUTH);

        add(panelPrincipal);

        setVisible(true);
    }
}
