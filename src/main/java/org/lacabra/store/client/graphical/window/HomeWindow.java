package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.server.api.type.user.Authority;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public final class HomeWindow extends DispatchedWindow {
    @Serial
    private final static long serialVersionUID = 1L;

    public static final String TITLE = "GOAT";
    public static final Dimension SIZE = new Dimension(800, 600);
    public static final int CAROUSEL_INTERVAL = 3000;

    public HomeWindow(final WindowDispatcher wd) {
        super(wd);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        super.setDispatcher(wd);

        final var controller = this.controller();
        if (controller == null) return;

        this.auth(() -> {
            final var user = controller.getUser();
            assert (user != null);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setLocationRelativeTo(null);

            {
                final var bar = new JMenuBar();
                this.setJMenuBar(bar);

                {
                    final var search = new JMenu("Buscar");

                    {
                        final var item = new JMenuItem("Todo");
                        item.addActionListener(e -> this.replace(ShoppingWindow.class));

                        search.add(item);
                    }

                    {
                        final var clothing = new JMenuItem("Ropa");

                        search.add(clothing);
                    }

                    {
                        final var home = new JMenuItem("Home");

                        search.add(home);
                    }

                    {
                        final var office = new JMenuItem("Oficina");

                        search.add(office);
                    }

                    {
                        final var decoration = new JMenuItem("Decoración");

                        search.add(decoration);
                    }

                    bar.add(search);
                }

                if (user.authorities().contains(Authority.Artist)) {
                    bar.add(Box.createHorizontalGlue());

                    final var puesto = new JMenuItem("Mis productos");
                    puesto.addActionListener(e -> this.replace(WindowSalesStall.class));
                    puesto.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                    bar.add(puesto);
                }

                if (user.authorities().contains(Authority.Client)) {
                    bar.add(Box.createHorizontalGlue());

                    final var carrito = new JMenuItem("Mi carrito");
                    carrito.addActionListener(e -> this.replace(ShoppingCartWindow.class));
                    carrito.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                    bar.add(carrito);
                }
            }

            {
                final var banner = this.banner();

                if (banner != null) this.add(banner, BorderLayout.NORTH);
            }

            {
                JScrollPane scroll;

                {
                    final var carousel = new JPanel();
                    carousel.setLayout(new GridLayout(1, 1));
                    carousel.setPreferredSize(new Dimension(200, 200));

                    {
                        for (int i = 1; i <= 5; i++) {
                            String texto = "";
                            if (i == 1) {
                                texto = "¡No te pierdas las ofertas de primavera en la web de GOAT!" + " Descuentos " + "irresistibles en moda, decoración y más. ¡Renueva tu estilo al " + "mejor" + " precio!";
                            } else if (i == 2) {
                                texto = "¡Tu primera compra en la web de GOAT tendrá un descuento del 10%! No " +
                                        "pierdas" + " la " + "oportunidad de estrenarte con estilo y ahorra en tus " + "productos favoritos. " + "¡Compra ahora y disfruta de esta increíble oferta!";
                            } else if (i == 3) {
                                texto = "¿Buscas regalos originales y únicos? ¡En GOAT los tenemos! Explora nuestra " + "selección de productos diseñados por artistas independientes y encuentra el " + "regalo perfecto para esa persona especial. ¡Haz que tus regalos destaquen!";
                            } else if (i == 4) {
                                texto = "¡Si eres un artista o tienes habilidades manuales, ahora puedes poner tus " + "productos " + "a la venta fácilmente con tu propio puesto de ventas virtual en GOAT! Es una" + " forma " + "sencilla y cómoda de exhibir tus creaciones al mundo.";
                            } else if (i == 5) {
                                texto = "¡Expresa tu estilo y personalidad con productos únicos de artistas " +
                                        "independientes en " + "GOAT! Desde camisetas hasta tazas, nuestra " +
                                        "plataforma" + " ofrece una amplia " + "variedad de " + "productos para todos" +
                                        " los gustos. " + "¡Descubre tu próxima pieza favorita hoy!";
                            }

                            final var label = new JLabel(texto);
                            label.setHorizontalAlignment(JLabel.CENTER);

                            carousel.add(label);
                        }
                    }

                    scroll = new JScrollPane(carousel);

                    final int[] current = {0};
                    new Timer(CAROUSEL_INTERVAL, e -> {
                        final var parent = carousel.getParent();
                        if (parent == null) return;

                        if (parent instanceof JScrollPane s) {
                            final int n = carousel.getComponentCount();
                            current[0] = (current[0] + 1) % n;

                            s.getHorizontalScrollBar().setValue(current[0] * s.getViewport().getViewSize().width / n);
                        }
                    }).start();
                }

                this.add(scroll, BorderLayout.CENTER);
            }

            {
                final var footer = this.footer();
                if (footer != null) this.add(footer, BorderLayout.SOUTH);
            }

            this.setVisible(true);
        }, () -> this.replace(AuthWindow.class));
    }
}