package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public final class SignupWindow extends DispatchedWindow {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Crear una cuenta";

    public static final Dimension SIZE = new Dimension(400, 300);
    public static final Dimension FIELD_SIZE = new Dimension(200, 30);
    public static final Dimension BACK_BUTTON_SIZE = new Dimension(160, 25);
    public static final Dimension FORM_LABEL_SIZE = new Dimension(120, 40);

    public static final int BORDER = 20;
    public static final int INSET = 5;

    public SignupWindow(WindowDispatcher wd) {
        super(wd);
    }

    @Override
    public void setDispatcher(WindowDispatcher dispatcher) {
        super.setDispatcher(dispatcher);

        final WindowDispatcher d = this.getDispatcher();
        if (d == null) return;

        final var controller = d.controller();
        if (controller == null) return;

        final var user = controller.getUser();
        final var uid = user != null ? user.id() : null;

        controller.unauth();

        this.auth(() -> this.replace(HomeWindow.class), () -> {
            final var closed = new WindowAdapter() {
                @Override
                public void windowClosed(final WindowEvent e) {
                    replace(AuthWindow.class);
                }
            };

            final var id = new JTextField();
            if (uid != null) {
                id.setText(uid.toString());
            }

            this.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(final WindowEvent e) {
                    id.requestFocusInWindow();
                }
            });

            this.addWindowListener(closed);

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);

            {
                final var p = new JPanel();
                p.setLayout(new FlowLayout(FlowLayout.LEFT));

                {
                    final var b = new JButton("Volver al inicio");
                    b.setPreferredSize(BACK_BUTTON_SIZE);

                    b.addActionListener((e) -> this.close());

                    p.add(b);
                }

                this.add(p, BorderLayout.NORTH);
            }

            {
                final var p = new JPanel();
                p.setLayout(new GridLayout(6, 1));
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    final var confirm = new JButton("Regístrate");

                    final var creds = new Signal<>(new Credentials());
                    creds.effect(c -> confirm.setEnabled(!(c.id() == null || c.passwd() == null || c.passwd().isEmpty() || c.authorities().isEmpty())));

                    this.connect(creds);

                    {
                        final var p2 = new JPanel();
                        p2.setLayout(new FlowLayout(FlowLayout.LEFT));

                        {
                            final var l = new JLabel("Nombre de usuario", SwingConstants.LEFT);

                            l.setPreferredSize(FORM_LABEL_SIZE);
                            p2.add(l);
                        }

                        {
                            id.requestFocusInWindow();
                            id.setPreferredSize(FIELD_SIZE);
                            id.getDocument().addDocumentListener(new DocumentListener() {
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
                                    creds.set(creds.peek().id(id.getText()));
                                }
                            });

                            p2.add(id);
                        }

                        p.add(p2);
                    }

                    {
                        final var p2 = new JPanel();
                        p2.setLayout(new FlowLayout(FlowLayout.LEFT));

                        {
                            final var l = new JLabel("Contraseña", SwingConstants.LEADING);

                            l.setPreferredSize(FORM_LABEL_SIZE);
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
                                    creds.set(creds.peek().passwd(new String(t.getPassword())));
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

                        confirm.addActionListener(e -> {
                            final var c = creds.get();

                            this.load(() -> {
                                try {
                                    return controller.authResp(c);
                                } catch (Exception exception) {
                                    return CompletableFuture.completedFuture(false);
                                }
                            }, (final HttpResponse<Boolean> r) -> {
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

                                this.removeWindowListener(closed);
                                this.replace(HomeWindow.class);
                            }, DispatchedWindow.AUTH_MESSAGE, DispatchedWindow.AUTH_TIMEOUT, false);
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