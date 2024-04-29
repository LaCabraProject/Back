package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.client.graphical.DispatchedWindow;
import org.lacabra.store.client.graphical.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.user.Credentials;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class WindowLogin extends DispatchedWindow {
    public static final String TITLE = "Iniciar sesión";

    public static final Dimension SIZE = new Dimension(400, 400);
    public static final Dimension FIELD_SIZE = new Dimension(200, 30);
    public static final Dimension BACK_BUTTON_SIZE = new Dimension(120, 20);

    public static final int BORDER = 20;
    public static final int INSET = 5;

    public WindowLogin() {
        this(null);
    }

    public WindowLogin(WindowDispatcher wd) {
        super(wd);
    }

    @Override
    public void setDispatcher(WindowDispatcher dispatcher) {
        super.setDispatcher(dispatcher);

        final WindowDispatcher d = this.getDispatcher();
        if (d == null)
            return;

        final var controller = d.controller();
        if (controller == null)
            return;

        controller.auth().thenAccept((auth) -> {
            if (auth) {
                this.close();
                d.dispatch(WindowHome.class);

                return;
            }

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);
            this.getContentPane().setBackground(Color.WHITE);

            try {
                final var res = this.getClass().getClassLoader().getResource("icon.png");
                if (res != null) {
                    this.setIconImage(new ImageIcon(res.getFile()).getImage());
                }
            } catch (Exception e) {
                Logger.getLogger().warning(e);
            }

            {
                final var p = new JPanel();
                p.setLayout(new GridBagLayout());
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                final var constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.insets = new Insets(INSET, INSET, INSET, INSET);

                {
                    final var user = new JTextField();
                    final var passwd = new JPasswordField();
                    final var b = new JButton("Iniciar sesión");

                    final ActionListener validate = (ActionEvent e) -> {
                        final var creds = new Credentials(user.getText(), Arrays.toString(passwd.getPassword()));
                        if (creds.id() == null || creds.passwd() == null || creds.passwd().isEmpty()) {
                            b.setEnabled(false);
                            return;
                        }
                    };

                    {
                        final var p2 = new JPanel();
                        p2.setBorder(new EmptyBorder(0, 22, 0, 0));

                        {
                            final var label = new JLabel("Usuario");

                            p2.add(label);
                        }

                        {
                            user.setPreferredSize(FIELD_SIZE);
                            user.addActionListener(validate);

                            p2.add(user);
                        }

                        p.add(p2, constraints);
                        constraints.gridy++;
                    }

                    {
                        final var p2 = new JPanel();
                        p2.setBorder(new EmptyBorder(0, 22, 0, 0));

                        {
                            final var label = new JLabel("Contraseña");

                            p2.add(label);
                        }

                        {
                            passwd.setPreferredSize(FIELD_SIZE);
                            passwd.addActionListener(validate);

                            p2.add(passwd);
                        }

                        p.add(p2, constraints);
                        constraints.gridy++;
                    }

                    {
                        b.setPreferredSize(FIELD_SIZE);

                        b.setEnabled(false);
                        b.addActionListener(e -> {
                            controller().auth(user.getText(), Arrays.toString(passwd.getPassword())).thenAccept(r -> {
                                if (!r)
                                    return;

                                this.close();
                                this.dispatch(WindowHome.class);
                            });
                        });

                        p.add(b, constraints);
                        constraints.gridy++;
                    }
                }

                {
                    final var b = new JButton("Volver al inicio");
                    b.setPreferredSize(BACK_BUTTON_SIZE);

                    b.addActionListener(e -> {
                        this.close();
                        this.dispatch(WindowHome.class);
                    });

                    p.add(b, constraints);
                }

                this.add(p, BorderLayout.CENTER);
            }

            this.setVisible(true);
        });
    }
}