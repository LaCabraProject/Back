package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class HomeWindow extends DispatchedWindow {
    public final String TITLE = "GOAT";
    public final Dimension SIZE = new Dimension(800, 600);

    public HomeWindow() {
        this(null);
    }

    public HomeWindow(WindowDispatcher wd) {
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
                this.close();

                controller.unauth();
                this.dispatch(AuthWindow.class);

                return;
            }

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setLocationRelativeTo(null);

            {
                final var bar = new JMenuBar();
                this.setJMenuBar(bar);

                {
                    final var search = new JMenu();

                    {
                        final var item = new JMenuItem("Buscar");
                        item.addActionListener(e -> {
                            this.close();
                            this.dispatch(WindowShopping.class);
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
            }

            {
                final var banner = this.banner();

                if (banner != null)
                    this.add(banner, BorderLayout.NORTH);
            }

            {
                JScrollPane scroll;

                {
                    final var carousel = new JPanel();
                    carousel.setLayout(new GridLayout(1, 1));
                    carousel.setPreferredSize(new Dimension(200, 200));

                    for (int i = 1; i <= 5; i++) {
                        final var label = new JLabel("Ítem " + i);
                        label.setHorizontalAlignment(JLabel.CENTER);

                        carousel.add(label);
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

            {
                final var footer = this.footer();

                if (footer != null)
                    this.add(footer, BorderLayout.SOUTH);
            }

            this.setVisible(true);
        });
    }
}