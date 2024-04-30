package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.LockedWindowDispatcher;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;

import javax.swing.*;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

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

    @Override
    public void setDispatcher(WindowDispatcher dispatcher) {
        super.setDispatcher(dispatcher);

        final WindowDispatcher d = this.getDispatcher();
        if (d == null) return;

        final var controller = d.controller();
        if (controller == null) return;

        controller.auth().thenAccept((auth) -> {
            if (auth) {
                this.close();
                d.dispatch(HomeWindow.class);

                return;
            }

            this.setDefaultCloseOperation(EXIT_ON_CLOSE);

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);

            {
                final var p = new JPanel();
                p.setLayout(new GridLayout(6, 1));
                p.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

                {
                    final var user = new JTextField();
                    final var passwd = new JPasswordField();
                    final var type = new ButtonGroup();
                    final var confirm = new JButton("Regístrate");

                    final Supplier<Credentials> creds = () -> new Credentials(user.getText(),
                            StreamSupport.stream(Spliterators.spliteratorUnknownSize(type.getElements().asIterator(),
                                            Spliterator.ORDERED), false).filter(AbstractButton::isSelected)
                                    .map(x -> x.getText().equalsIgnoreCase("Comprar") ? Authority.Client :
                                            x.getText().equalsIgnoreCase("Vender") ? Authority.Artist : null).filter(Objects::nonNull).toList(),
                            Arrays.toString(passwd.getPassword()));

                    final ActionListener validate = e -> {
                        final var c = creds.get();
                        confirm.setEnabled(!(c.id() == null || c.passwd() == null || c.passwd().isEmpty()));
                    };

                    {
                        final var p2 = new JPanel();

                        {
                            final var label = new JLabel("Nombre de usuario");

                            p2.add(label);
                        }

                        {
                            user.setPreferredSize(FIELD_SIZE);
                            user.addActionListener(validate);

                            p2.add(user);
                        }

                        p.add(p2);
                    }

                    {
                        final var p2 = new JPanel();

                        {
                            final var label = new JLabel("Contraseña");

                            p2.add(label);
                        }

                        {
                            passwd.setPreferredSize(FIELD_SIZE);
                            passwd.addActionListener(validate);

                            p2.add(passwd);
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

                                p3.add(b);
                                type.add(b);
                            }

                            {
                                final var b = new JCheckBox("Vender");

                                p3.add(b);
                                type.add(b);
                            }

                            p2.add(p3, c);
                        }

                        p.add(p2);
                    }

                    {
                        confirm.setPreferredSize(FIELD_SIZE);

                        confirm.setEnabled(false);
                        confirm.addActionListener(e ->
                                controller().authResp(creds.get()).thenAccept(r -> {
                                    final var status = r.statusCode();

                                    if (status == Response.Status.OK.getStatusCode()) {
                                        final var w = this.replace(HomeWindow.class);

                                        if (w != null)
                                            w.message("¡Te has registrado con éxito! Recuerda que tienes un 10% " +
                                                    "de descuento en tu primer pedido.");

                                        return;
                                    }

                                    final var body = r.body();
                                    this.message(String.format("%s%s",
                                            status == Response.Status.UNAUTHORIZED.getStatusCode() ? "" :
                                                    String.format("%d :", status),
                                            body == null ? "" : body));
                                })
                        );

                        p.add(confirm);
                    }
                }

                this.add(p, BorderLayout.CENTER);
            }

            this.setVisible(true);
        });
    }

    public static void main(final String[] args) throws MalformedURLException {
        new LockedWindowDispatcher(MainController.fromArgs(args)).dispatch(SignupWindow.class);
    }
}