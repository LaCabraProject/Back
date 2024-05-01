package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.LockedWindowDispatcher;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.maven.ClassRunner;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;

public final class SignupWindow extends DispatchedWindow {
    public static final String TITLE = "Crear una cuenta";

    public static final Dimension SIZE = new Dimension(400, 300);
    public static final Dimension FIELD_SIZE = new Dimension(200, 30);
    public static final Dimension BACK_BUTTON_SIZE = new Dimension(120, 20);

    public static final int BORDER = 20;
    public static final int INSET = 5;

    public SignupWindow() {
        this(null);
    }

    public SignupWindow(WindowDispatcher wd) {
        super(wd);
    }

    public static void main(final String[] args) throws IOException, NoSuchMethodException, InterruptedException {
        if (Arrays.stream(args).noneMatch(x -> x.equals("run"))) {
            ClassRunner.run(SignupWindow.class);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                LockedWindowDispatcher.fromArgs(args).dispatch(SignupWindow.class);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void setDispatcher(WindowDispatcher dispatcher) {
        super.setDispatcher(dispatcher);

        final WindowDispatcher d = this.getDispatcher();
        if (d == null) return;

        final var controller = d.controller();
        if (controller == null) return;

        controller.auth().thenAccept((auth) -> {
            if (auth) {
                this.replace(HomeWindow.class);

                return;
            }

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(final WindowEvent e) {
                    removeWindowListener(this);
                    replace(AuthWindow.class);
                }
            });

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);

            {
                final var p = new JPanel();
                p.setLayout(new GridLayout(6, 1));
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    final var confirm = new JButton("Regístrate");

                    final Signal<Credentials> creds = new Signal<>(new Credentials());
                    creds.effect(c -> confirm.setEnabled(!(c.id() == null || c.passwd() == null || c.passwd().isEmpty() || c.authorities().isEmpty())));

                    this.connect(creds);

                    {
                        final var p2 = new JPanel();

                        {
                            final var l = new JLabel("Nombre de usuario");

                            p2.add(l);
                        }

                        {
                            final var t = new JTextField();
                            t.setPreferredSize(FIELD_SIZE);
                            t.getDocument().addDocumentListener(new DocumentListener() {
                                @Override
                                public void insertUpdate(DocumentEvent e) {
                                    this.changedUpdate(e);
                                }

                                @Override
                                public void removeUpdate(DocumentEvent e) {
                                    this.changedUpdate(e);
                                }

                                @Override
                                public void changedUpdate(DocumentEvent e) {
                                    creds.set(creds.peek().id(t.getText()));
                                }
                            });

                            p2.add(t);
                        }

                        p.add(p2);
                    }

                    {
                        final var p2 = new JPanel();

                        {
                            final var l = new JLabel("Contraseña");

                            p2.add(l);
                        }

                        {
                            final var t = new JPasswordField();

                            t.setPreferredSize(FIELD_SIZE);
                            t.getDocument().addDocumentListener(new DocumentListener() {
                                @Override
                                public void insertUpdate(DocumentEvent e) {
                                    this.changedUpdate(e);
                                }

                                @Override
                                public void removeUpdate(DocumentEvent e) {
                                    this.changedUpdate(e);
                                }

                                @Override
                                public void changedUpdate(DocumentEvent e) {
                                    creds.set(creds.peek().passwd(Arrays.toString(t.getPassword())));
                                }
                            });

                            p2.add(t);
                        }

                        p.add(p2);
                    }

                    {
                        final var p2 = new JPanel();
                        p2.setLayout(new GridBagLayout());

                        final var c = new GridBagConstraints();
                        c.insets = new Insets(INSET, INSET, INSET, INSET);

                        {
                            final JLabel label = new JLabel("Quiero:");

                            p2.add(label, c);
                            c.gridy++;
                        }

                        {
                            final var p3 = new JPanel();
                            p3.setLayout(new FlowLayout());

                            {
                                final var b = new JCheckBox("Comprar");
                                b.addActionListener(e -> {
                                    final var v = creds.peek();
                                    final var auths = new HashSet<>(v.authorities());

                                    if (b.isSelected()) auths.add(Authority.Client);

                                    else auths.remove(Authority.Client);

                                    creds.set(v.authorities(auths));
                                });

                                p3.add(b);
                            }

                            {
                                final var b = new JCheckBox("Vender");
                                b.addActionListener(e -> {
                                    final var v = creds.peek();
                                    final var auths = new HashSet<>(v.authorities());

                                    if (b.isSelected()) auths.add(Authority.Artist);

                                    else auths.remove(Authority.Artist);

                                    creds.set(v.authorities(auths));
                                });

                                p3.add(b);
                            }

                            p2.add(p3, c);
                        }

                        p.add(p2);
                    }

                    {
                        confirm.setPreferredSize(FIELD_SIZE);

                        confirm.setEnabled(false);
                        confirm.addActionListener(e -> {
                            final var r = controller.authResp(creds.get()).join();
                            final var status = r.statusCode();

                            if (status == Response.Status.OK.getStatusCode()) {
                                final var w = this.replace(HomeWindow.class);

                                if (w != null)
                                    w.message("¡Te has registrado con éxito! Recuerda que tienes un 10% " + "de " +
                                            "descuento en tu primer pedido.");

                                return;
                            }

                            final var body = r.body();
                            final var msg = String.format("%s%s",
                                    status == Response.Status.UNAUTHORIZED.getStatusCode() ? "" :
                                            String.format("%d " + ":", status), body == null ? "" : body);

                            Logger.getLogger().warning(msg);

                            this.message(msg);
                        });

                        p.add(confirm);
                    }
                }

                this.add(p, BorderLayout.CENTER);
            }

            this.setVisible(true);
        });
    }
}