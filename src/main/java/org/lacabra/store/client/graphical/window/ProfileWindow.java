package org.lacabra.store.client.windows;

import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.client.graphical.window.AuthWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.Function;

public final class ProfileWindow extends DispatchedWindow {
    public static final Function<String, String> TITLE = name -> String.format("Perfil de usuario%s", name == null ?
            "" : (":" + " " + name));

    public static final Dimension SIZE = new Dimension(800, 600);
    public static final int INSET = 10;
    public static final int GAP = INSET / 2;

    public static final int UPDATE_USER = 1000;

    public ProfileWindow(final UserDTO user) {
        this(null, user);
    }

    public ProfileWindow(final WindowDispatcher wd, final UserDTO user) {
        super(wd, user);
    }

    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        this.setDispatcher(wd, (Signal<UserDTO>) null);
    }

    public void setDispatcher(final WindowDispatcher wd, final Signal<UserDTO> signal) {
        super.setDispatcher(wd, signal);

        final var controller = this.controller();
        if (controller == null) return;

        controller.auth().thenAccept((auth) -> {
            if (!auth) {
                this.close();

                controller.unauth();
                this.dispatch(AuthWindow.class);

                return;
            }

            this.setTitle(TITLE.apply(null));

            this.setSize(SIZE);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setLocationRelativeTo(null);

            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            this.setLayout(new GridBagLayout());
            final var c = new GridBagConstraints();

            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(INSET, INSET, INSET, INSET);

            Long update = null;
            if (signal != null) {
                signal.interval(UPDATE_USER);

                update = signal.effect(curr -> {
                    if (signal.isRunning()) return;

                    if (curr == null) return;

                    final var user = controller.GET.User.id(curr.id()).join();
                    if (user == null) {
                        this.close();
                        return;
                    }

                    signal.set(user);
                });
            }

            {
                final var p = new JPanel(new GridLayout(6, 2, GAP, GAP));
                p.setBorder(BorderFactory.createTitledBorder("Información del usuario"));

                if (signal != null) signal.effect(user -> {
                    if (user == null)
                        return;

                    p.removeAll();

                    Map.of(
                            "ID", user.id().toString(),
                            "Nombre", "",
                            "Correo", "",
                            "Dirección", "",
                            "Teléfono", "",
                            "Biografía", ""
                    ).forEach((k, v) -> {
                        if (v == null || v.isBlank())
                            return;

                        p.add(new JLabel(v));

                        if (k.equals("Biografía")) {
                            final var t = new JTextArea();
                            t.setEditable(false);

                            p.add(t);

                            return;
                        }

                        final var t = new JTextField();
                        t.setEditable(false);

                        p.add(t);
                    });
                }, update);

                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1;

                this.add(p, c);
            }

            this.setVisible(true);
        });
    }
}
