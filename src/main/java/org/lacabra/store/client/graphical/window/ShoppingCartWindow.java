package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.util.ArrayList;

public final class ShoppingCartWindow extends DispatchedWindow {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @brief Título de la ventana principal.
     */
    public static final String TITLE = "GOAT";

    /**
     * @brief Tamaño de la ventana principal.
     */
    public static final Dimension SIZE = new Dimension(800, 600);

    /**
     * @param wd Dispatcher de ventanas.
     * @brief Constructor de la ventana principal.
     */
    public ShoppingCartWindow(final WindowDispatcher wd, final Signal<ArrayList<ItemDTO[]>> carrito) {
        super(wd, carrito);
    }

    /**
     * @param wd Dispatcher de ventanas.
     * @brief Inicializa la ventana.
     */
    @Override
    public void setDispatcher(final WindowDispatcher wd, final Signal<?>... signals) {
        assert (signals != null);
        assert (signals.length == 1);
        assert (signals[0] != null);
        assert (signals[0].peek() instanceof ArrayList);

        final var carrito = (Signal<ArrayList<ItemDTO>>) signals[0];

        super.setDispatcher(wd, carrito);

        final var controller = this.controller();
        if (controller == null) return;

        this.auth(() -> {
            final var user = controller.getUser();
            assert (user != null);
            final var uid = user.id();
            assert (uid != null);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    final var wd = getDispatcher();
                    if (wd == null) {
                        close();
                        return;
                    }

                    final var w = windows();
                    if (w != null && w.values().stream().anyMatch(x -> x.getClass().equals(HomeWindow.class))) {
                        close();
                    } else {
                        replace(HomeWindow.class);
                    }
                }
            });

            final var delete = new Signal<>(false);

            {
                final var p = new JPanel(new FlowLayout(FlowLayout.LEADING));

                {
                    final var l = new JLabel("Carrito de " + uid.get());

                    l.putClientProperty("FlatLaf.styleClass", "h1");

                    p.add(l);
                }

                this.add(p, BorderLayout.NORTH);
            }

            {
                final var p = new ScrollPane();

                {
                    final var p2 = new JPanel(new GridLayout(3, 1));


                    p.add(p2);
                }

                this.add(p, BorderLayout.CENTER);
            }

            {
                final var p = new JPanel(new FlowLayout(FlowLayout.CENTER));

                {
                    final var b = new JButton("Eliminar");
                    delete.effect(v -> {
                        if (v)
                            b.setText("No eliminar");

                        else
                            b.setText("Eliminar");
                    });

                    b.addActionListener(e -> delete.set(delete.peek()));

                    p.add(b);
                }

                {
                    final var b = new JButton("Realizar pedido");

                    carrito.effect(items -> b.setEnabled(items != null && !items.isEmpty()));

                    b.addActionListener(e -> {
                        this.message("Hagamos como que has hecho un pedido :)");
                        this.close();
                    });

                    p.add(b);
                }

                this.add(p, BorderLayout.SOUTH);
            }

            this.connect(delete);
            delete.get();

            this.connect(carrito);
            carrito.get();

            this.setVisible(true);
        }, () -> this.replace(AuthWindow.class));
    }
}