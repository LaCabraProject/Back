package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public final class ShoppingCartWindow extends DispatchedWindow {
    public static final String TITLE = "Carrito de compra";
    public static final Dimension SIZE = new Dimension(800, 600);
    public static final int BORDER = 10;
    private JTextField t = new JTextField();
    @Serial
    private final static long serialVersionUID = 1L;

    public ShoppingCartWindow() {
        this(null);
    }

    public ShoppingCartWindow(final WindowDispatcher wd) {
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
                controller.unauth();
                this.replace(AuthWindow.class);

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

                        c.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                                JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
                                String selectedMethod = (String) comboBox.getSelectedItem();

                                if(selectedMethod.equals("Estándar")){

                                }

                                else if(selectedMethod.equals("Exprés")){

                                }

                                else if(selectedMethod.equals("Entrega al día siguiente")){

                                }
                            }
                        });

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
                        t = new JTextField();
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

                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            JList<?> productList = (JList<String>) ((JScrollPane) ((JPanel) ShoppingCartWindow.this.
                                    getContentPane().getComponent(1)).getComponent(0)).getViewport().getView();

                            DefaultListModel<?> model = (DefaultListModel<?>) productList.getModel();
                            int selectedIndex = productList.getSelectedIndex();

                            if (selectedIndex != -1) {
                                model.remove(selectedIndex);
                            }

                        }
                    });

                    p.add(b);
                }

                {
                    final var b = new JButton("Aplicar cupón");

                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            if(!controller.GET.Item.all().join().isEmpty()) {
                                String msg="El cupón de primera compra ya fue aplicado.";
                                JOptionPane.showMessageDialog(ShoppingCartWindow.this, msg, "Descuento negado", JOptionPane.INFORMATION_MESSAGE);
                                Logger.getLogger().info(msg);
                            }else{
                                double temporal=Double.parseDouble(t.getText());
                                temporal=temporal-(temporal*0.2);
                                t.setText(String.valueOf(temporal));
                            }

                        }

                    });

                    p.add(b);
                }

                {
                    final var b = new JButton("Realizar pago");

                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            JList<?> productList = (JList<String>) ((JScrollPane) ((JPanel) ShoppingCartWindow.this.
                                    getContentPane().getComponent(1)).getComponent(0)).getViewport().getView();

                            DefaultListModel<?> model = (DefaultListModel<?>) productList.getModel();
                            model.removeAllElements();
                        }
                    });

                    p.add(b);
                }

                this.add(p, BorderLayout.SOUTH);
            }
        });
    }
}