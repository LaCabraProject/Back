package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;

import javax.swing.*;
import java.awt.*;

public final class WindowShoppingCart extends DispatchedWindow {
    public static final String TITLE = "Carrito de compra";
    public static final Dimension SIZE = new Dimension(800, 600);

    public static final int BORDER = 10;

    public WindowShoppingCart() {
        this((WindowDispatcher) null);
    }

    public WindowShoppingCart(final WindowDispatcher wd) {
        super(wd);

        this.setDispatcher(wd);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        super.setDispatcher(wd);

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
                final var p = new JPanel(new GridLayout(1, 2, BORDER, 0));
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    final var p2 = new JPanel(new GridLayout(2, 1));

                    {
                        final var l = new JLabel("Método de envío:");

                        p2.add(l);
                    }

                    {
                        final var c = new JComboBox<>(new String[]{"Estándar", "Exprés", "Entrega al día siguiente"});

                        p2.add(c);
                    }

                    p.add(p2);
                }

                {
                    final var p2 = new JPanel(new GridLayout(2, 1));

                    {
                        final var l = new JLabel("Costo total:");

                        p2.add(l);
                    }

                    {
                        final var t = new JTextField();
                        t.setEditable(false);
                        t.setColumns(10);

                        p2.add(t);
                    }

                    p.add(p2);
                }

                this.add(p, BorderLayout.NORTH);
            }

            {
                final var p = new JPanel(new BorderLayout());
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    JScrollPane s;

                    {
                        JList<?> l;

                        {
                            final var lm = new DefaultListModel<>();

                            l = new JList<>(lm);
                        }

                        l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                        s = new JScrollPane(l);
                    }

                    p.add(s, BorderLayout.CENTER);
                }

                this.add(p, BorderLayout.CENTER);
            }

            {
                final var p = new JPanel(new GridLayout(1, 3, BORDER, 0));
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    final var b = new JButton("Eliminar producto");

                    p.add(b);
                }

                {
                    final var b = new JButton("Aplicar cupón");

                    p.add(b);
                }

                {
                    final var b = new JButton("Realizar pago");

                    p.add(b);
                }

                this.add(p, BorderLayout.SOUTH);
            }
        });
    }
}