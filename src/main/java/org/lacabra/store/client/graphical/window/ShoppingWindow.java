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

public final class ShoppingWindow extends DispatchedWindow {
    @Serial
    private final static long serialVersionUID = 1L;

    public final String TITLE = "Buscar artículos";
    public final Dimension SIZE = new Dimension(800, 600);

    private JTable t;
    private DefaultTableModel tm;
    private CompletableFuture<List<ItemDTO>> objetosRecibidos;
    private Signal<ArrayList<ItemDTO>> signal=new Signal<>();
    private ArrayList<ItemDTO> carrito=new ArrayList<>();

    public ShoppingWindow() {
        this(null);
    }

    public ShoppingWindow(final WindowDispatcher wd) {
        super(wd);
    }

    public static void main(final String[] args) throws MalformedURLException {
        WindowDispatcher.fromArgs(args).dispatch(ShoppingWindow.class);
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
