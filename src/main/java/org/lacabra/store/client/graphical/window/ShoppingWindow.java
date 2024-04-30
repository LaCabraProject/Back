package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.type.tuple.Pair;
import org.lacabra.store.server.api.type.item.ItemType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.MalformedURLException;

public final class ShoppingWindow extends DispatchedWindow {
    public final String TITLE = "Buscar artículos";
    public final Dimension SIZE = new Dimension(800, 600);

    public ShoppingWindow() {
        this(null);
    }

    public ShoppingWindow(final WindowDispatcher wd) {
        super(wd);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        super.setDispatcher(wd, new Signal<ItemDTO>());

        final var controller = this.controller();
        if (controller == null)
            return;

        controller.auth().thenAccept((auth) -> {
            if (!auth) {
                controller.unauth();
                this.replace(AuthWindow.class);

                return;
            }

            this.setTitle(TITLE);

            this.setSize(SIZE);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setLocationRelativeTo(null);

            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            this.setLayout(new BorderLayout());

            {
                final var p = new JPanel();

                {
                    final var c = new JComboBox<>(ItemType.values());
                    p.add(c);
                }

                {
                    final var t = new JTextField(20);

                    p.add(t);
                }

                {
                    final var b = new JButton("Buscar");

                    p.add(b);
                }

                this.add(p, BorderLayout.NORTH);
            }

            {
                final var p = new JPanel();

                {
                    final var l = new JLabel("Lista de artículos en venta:");
                    l.setHorizontalAlignment(SwingConstants.CENTER);

                    p.add(l, BorderLayout.NORTH);
                }

                {
                    JScrollPane s;

                    {
                        JTable t;

                        {
                            final var tm = new DefaultTableModel();

                            final Pair<?, ?>[] cols = {
                                    new Pair<String, Class<?>>("Nombre", String.class),
                                    new Pair<String, Class<?>>("Descripción", String.class)
                            };

                            t = new JTable(tm) {
                                @Override
                                public Class<?> getColumnClass(final int col) {
                                    return (Class<?>) cols[col].y();
                                }
                            };
                        }

                        t.setRowHeight(100);
                        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                        s = new JScrollPane(t);
                    }

                    p.add(s);
                }

                {
                    final var p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

                    {
                        final var b = new JButton("Volver al inicio");
                        b.addActionListener(e -> this.replace(HomeWindow.class));

                        p2.add(b);
                    }

                    p.add(p2, BorderLayout.SOUTH);
                }

                this.add(p, BorderLayout.CENTER);
            }

            this.setVisible(true);
        });
    }

    public static void main(final String[] args) throws MalformedURLException {
        WindowDispatcher.fromArgs(args).dispatch(ShoppingWindow.class);
    }
}
