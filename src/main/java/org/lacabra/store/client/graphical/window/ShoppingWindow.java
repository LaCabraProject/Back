/**
 * @file ShoppingWindow.java
 * @brief Define la ventana de compras para la aplicación.
 */

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @class ShoppingWindow
 * @brief Implementa la interfaz gráfica para la ventana de compras.
 */
public final class ShoppingWindow extends DispatchedWindow {
    @Serial
    private final static long serialVersionUID = 1L;

    /** @brief Título de la ventana. */
    public final String TITLE = "Buscar artículos";

    /** @brief Tamaño de la ventana. */
    public final Dimension SIZE = new Dimension(800, 600);

    /** @brief Tabla para mostrar los artículos. */
    private JTable t;

    /** @brief Modelo de la tabla para los artículos. */
    private DefaultTableModel tm;

    /** @brief Futura lista de artículos recibidos del servidor. */
    private CompletableFuture<List<ItemDTO>> objetosRecibidos;

    /** @brief Señal para los artículos en el carrito. */
    private Signal<ArrayList<ItemDTO>> signal = new Signal<>();

    /** @brief Lista de artículos en el carrito. */
    private ArrayList<ItemDTO> carrito = new ArrayList<>();

    /**
     * @brief Constructor por defecto.
     */
    public ShoppingWindow() {
        this(null);
    }

    /**
     * @brief Constructor con un dispatcher de ventanas.
     * @param wd Dispatcher de ventanas.
     */
    public ShoppingWindow(final WindowDispatcher wd) {
        super(wd);
    }

    /**
     * @brief Constructor con un dispatcher de ventanas y una señal.
     * @param wd Dispatcher de ventanas.
     * @param signal Señal de artículos en el carrito.
     */
    public ShoppingWindow(final WindowDispatcher wd, final Signal<ArrayList<ItemDTO>> signal) {
        super(wd, signal);
    }

    /**
     * @brief Método principal para ejecutar la ventana de compras.
     * @param args Argumentos de la línea de comandos.
     * @throws MalformedURLException Si la URL del dispatcher es inválida.
     */
    public static void main(final String[] args) throws MalformedURLException {
        WindowDispatcher.fromArgs(args).dispatch(ShoppingWindow.class);
    }

    /**
     * @brief Configura el dispatcher de la ventana con una señal de artículos en el carrito.
     * @param wd Dispatcher de ventanas.
     * @param signal Señal de artículos en el carrito.
     */

    public void setDispatcher(final WindowDispatcher wd, final Signal<ArrayList<ItemDTO>> signal) {
        super.setDispatcher(wd, signal);
        if (signal != null) {
            carrito=signal.get();
        }

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
            objetosRecibidos=controller.GET.Item.all();

            {
                final var p = new JPanel();

                {
                    final var c = new JComboBox<>(ItemType.values());
                    c.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ItemType selectedType = (ItemType) c.getSelectedItem();
                            updateTable(selectedType);
                        }

                    });
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


                        {
                            tm = new DefaultTableModel();
                            for(ItemDTO item : objetosRecibidos.join()) {
                                tm.addRow(new Object[]{item.name(),item.id()});
                            }

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

                {
                    final var p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

                    {
                        final var b = new JButton("Meter en carrito");
                        b.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int i=t.getSelectedRow();
                                ItemDTO item = objetosRecibidos.join().get(i);
                                carrito.add(item);
                            }
                        });

                        p2.add(b);
                    }



                    p.add(p2, BorderLayout.SOUTH);
                }

                {
                    final var p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

                    {
                        final var b = new JButton("Finalizar compra");
                        b.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                close();
                                signal.effect((Consumer<ArrayList<ItemDTO>>) carrito);
                                connect(signal);
                                wd.dispatch(ShoppingCartWindow.class);
                            }
                        });

                        p2.add(b);
                    }

                    p.add(p2, BorderLayout.SOUTH);
                }


                this.add(p, BorderLayout.CENTER);
            }

            this.setVisible(true);
        });
    }

    private void updateTable(ItemType selectedType) {
        DefaultListModel<ItemDTO> items = getItemsByType(selectedType);

        tm.setRowCount(0);
        for (int i = 0; i < items.getSize(); i++) {
            tm.addRow(new Object[]{items.getElementAt(i).name(), items.getElementAt(i).description()});
        }
    }

    private DefaultListModel<ItemDTO> getItemsByType(ItemType selectedType) {
        DefaultListModel<ItemDTO> items = new DefaultListModel<>();
        for(ItemDTO item: objetosRecibidos.join()){
            if(item.type().equals(selectedType)){
                items.addElement(item);
            }
        }
        return items;
    }
}
