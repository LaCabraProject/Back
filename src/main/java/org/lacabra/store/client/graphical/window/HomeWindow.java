package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public final class HomeWindow extends DispatchedWindow {
    @Serial
    private final static long serialVersionUID = 1L;

    public final String TITLE = "GOAT";
    public final Dimension SIZE = new Dimension(800, 600);

    public HomeWindow() {
        this(null);
    }

    public HomeWindow(final WindowDispatcher wd) {
        super(wd);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        super.setDispatcher(wd);

        final var controller = this.controller();
        if (controller == null)
            return;

        controller.auth().thenAccept((auth) -> {
            if (!auth) {
                controller.unauth();
                this.replace(AuthWindow.class);

                return;
            }

            //ajustes iniciales de la ventana
            {
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setTitle(TITLE);
                this.setSize(SIZE);
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                this.setLocationRelativeTo(null);
            }

            //JMenuBar
            {
                final var bar = new JMenuBar();
                this.setJMenuBar(bar);

                {
                    final var search = new JMenu();

                    {
                        final var item = new JMenuItem("Buscar");
                        item.addActionListener(e -> {
                            this.close();
                            this.dispatch(ShoppingWindow.class);
                        });

                        search.add(item);
                    }

                    try {
                        final var res = this.getClass().getClassLoader().getResource("lupa.png");

                        if (res != null) {
                            search.setIcon(new ImageIcon(ImageIO.read(new File(res.getFile())).getScaledInstance(10,
                                    10, Image.SCALE_SMOOTH)));
                        }
                    } catch (IOException e) {
                        Logger.getLogger().warning(e);
                    }

                    bar.add(search);
                }

                {
                    final var clothing = new JMenu("Ropa");

                    bar.add(clothing);
                }

                {
                    final var home = new JMenu("Home");

                    bar.add(home);
                }

                {
                    final var office = new JMenu("Oficina");

                    bar.add(office);
                }

                {
                    final var decoration = new JMenu("Decoración");

                    bar.add(decoration);
                }

                {
                    final var carrito = new JMenu("Mi carrito");
                    carrito.addActionListener(e -> {
                        this.close();
                        this.dispatch(ShoppingCartWindow.class);
                    });

                    bar.add(carrito);
                }

                {
                    final var puesto = new JMenu("Mis productos");
                    puesto.addActionListener(e -> {
                        this.close();
                        this.dispatch(WindowSalesStall.class);
                    });

                    bar.add(puesto);
                }
            }

            //banner
            {
                final var banner = this.banner();

                if (banner != null)
                    this.add(banner, BorderLayout.NORTH);
            }

            //contenidos
            {
                JScrollPane scroll;

                {
                    final var carousel = new JPanel();
                    carousel.setLayout(new GridLayout(1, 1));
                    carousel.setPreferredSize(new Dimension(200, 200));

                    //mensajes en el centro sobre la tienda
                    {
                        for (int i = 1; i <= 5; i++) {
                            String texto = "";
                            if (i == 1) {
                                texto = "¡No te pierdas las ofertas de primavera en la web de GOAT!" +
                                        " Descuentos irresistibles en moda, decoración y más. ¡Renueva tu estilo al mejor" +
                                        " precio!";
                            } else if (i == 2) {
                                texto = "¡Tu primera compra en la web de GOAT tendrá un descuento del 10%! No pierdas la " +
                                        "oportunidad de estrenarte con estilo y ahorra en tus productos favoritos. " +
                                        "¡Compra ahora y disfruta de esta increíble oferta!";
                            } else if (i == 3) {
                                texto = "¿Buscas regalos originales y únicos? ¡En GOAT los tenemos! Explora nuestra " +
                                        "selección de productos diseñados por artistas independientes y encuentra el " +
                                        "regalo perfecto para esa persona especial. ¡Haz que tus regalos destaquen!";
                            } else if (i == 4) {
                                texto = "¡Si eres un artista o tienes habilidades manuales, ahora puedes poner tus productos " +
                                        "a la venta fácilmente con tu propio puesto de ventas virtual en GOAT! Es una forma " +
                                        "sencilla y cómoda de exhibir tus creaciones al mundo.";
                            } else if (i == 5) {
                                texto = "¡Expresa tu estilo y personalidad con productos únicos de artistas independientes en " +
                                        "GOAT! Desde camisetas hasta tazas, nuestra plataforma ofrece una amplia variedad de " +
                                        "productos para todos los gustos. ¡Descubre tu próxima pieza favorita hoy!";
                            }

                            final var label = new JLabel(texto);
                            label.setHorizontalAlignment(JLabel.CENTER);

                            carousel.add(label);
                        }
                    }

                    scroll = new JScrollPane(carousel);

                    final int[] current = {0};
                    new Timer(3000, e -> {
                        final var parent = carousel.getParent();
                        if (parent == null)
                            return;

                        if (parent instanceof JScrollPane s) {
                            final int n = carousel.getComponentCount();
                            current[0] = (current[0] + 1) % n;

                            s.getHorizontalScrollBar().setValue(current[0] * s.getViewport().getViewSize().width / n);
                        }
                    }).start();
                }

                this.add(scroll, BorderLayout.CENTER);
            }

            //footer
            {
                final var footer = this.footer();

                if (footer != null)
                    this.add(footer, BorderLayout.SOUTH);
            }

            this.setVisible(true);
        });
    }
}