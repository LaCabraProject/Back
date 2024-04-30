package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.type.id.UserId;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDetailsWindow extends DispatchedWindow {
    public static final String TITLE = "Detalles del artículo";

    public static final Dimension SIZE = new Dimension(800, 600);
    public static final Dimension IMAGE_SIZE = new Dimension(300, 300);

    public static final int BORDER = 10;

    public static final int UPDATE_ITEM = 1000;

    public ItemDetailsWindow(final ItemDTO item) {
        this(null, item);
    }

    public ItemDetailsWindow(final WindowDispatcher wd, final ItemDTO item) {
        super(wd, item);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        this.setDispatcher(wd, (Signal<ItemDTO>) null);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd, final Signal<ItemDTO> signal) {
        super.setDispatcher(wd, signal);

        final var controller = this.controller();
        if (controller == null) return;

        controller.auth().thenAccept((auth) -> {
            if (!auth) {
                this.close();

                controller.unauth();
                this.dispatch(AuthWindow.class);

                return;
            }

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);
            this.setLayout(new BorderLayout());

            {
                final var p = new JPanel();
                p.setLayout(new BorderLayout());
                p.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    final var p2 = new JPanel();
                    p2.setLayout(new GridLayout(1, 4));
                    p2.setBackground(Color.LIGHT_GRAY);
                    p2.setBorder(new EmptyBorder(BORDER / 2, BORDER / 2, BORDER / 2, BORDER / 2));

                    for (Object[] init : new Object[][]{{"Inicio", null, null}, {"Categoría", null, null}, {"Carrito"
                            , null, null}, {"Buscar", null, null}}) {
                        final var b = new JButton((String) init[0], new ImageIcon());

                        b.setHorizontalTextPosition(SwingConstants.CENTER);
                        b.setVerticalTextPosition(SwingConstants.BOTTOM);
                        b.addActionListener((ActionListener) init[3]);

                        p2.add(b);
                    }

                    p.add(p2, BorderLayout.NORTH);
                }

                {
                    final var l = new JLabel();
                    l.setPreferredSize(IMAGE_SIZE);

                    {
                        final var i = new ImageIcon();

                        l.setIcon(i);
                    }

                    p.add(l, BorderLayout.WEST);
                }

                {
                    final var p2 = new JTabbedPane();

                    {
                        final var p3 = new JPanel();
                        p3.setLayout(new BorderLayout());
                        p3.setBorder(new CompoundBorder(new TitledBorder("Reseñas del producto"),
                                new EmptyBorder(BORDER, BORDER, BORDER, BORDER)));

                        if (signal != null)
                            signal.effect(item -> {
                                p3.removeAll();

                                {
                                    final var p4 = new JPanel();

                                    new HashMap<UserId, String>().forEach((user, review) -> {
                                        final var p5 = new JPanel();

                                        {
                                            final var t = new JTextArea(review);
                                            t.setEditable(false);

                                            p5.add(t);
                                        }

                                        p4.add(p5);
                                    });

                                    p3.add(new JScrollPane(p4), BorderLayout.CENTER);
                                }

                                {
                                    final var b = new JButton("Agregar reseña");
                                    b.addActionListener(e -> {
                                        signal.interval(0);

                                        this.input("Escribe tu reseña:");

                                        signal.interval(1000);
                                    });

                                    p3.add(b, BorderLayout.SOUTH);
                                }
                            });

                        p2.add(p3);
                    }

                    p.add(p2, BorderLayout.CENTER);
                }

                {
                    p.add(this.footer(true), BorderLayout.SOUTH);
                }

                this.add(p);
            }

            if (signal != null)
                signal.interval(UPDATE_ITEM);

            this.setVisible(true);
        });
    }

    private List<String> reseñas;

    public ItemDetailsWindow() {
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
        panelResenas.setBorder(new CompoundBorder(new TitledBorder("Reseñas del Producto"), new EmptyBorder(10, 10,
                10, 10)));

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
                new ItemDetailsWindow();
            }
        });
    }
}

