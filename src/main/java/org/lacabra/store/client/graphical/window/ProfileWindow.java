/**
 * @file ProfileWindow.java
 * @brief Define la ventana de perfil de usuario para la aplicación.
 */

package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.Signal;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Map;
import java.util.function.Function;

/**
 * @class ProfileWindow
 * @brief Implementa la interfaz gráfica para la ventana de perfil de usuario.
 */
public final class ProfileWindow extends DispatchedWindow {

    /** @brief Función que genera el título de la ventana. */
    public static final Function<String, String> TITLE = name -> String.format("Perfil de usuario%s", name == null ?
            "" : (":" + " " + name));

    /** @brief Tamaño de la ventana. */
    public static final Dimension SIZE = new Dimension(800, 600);

    /** @brief Margen para los componentes de la ventana. */
    public static final int INSET = 10;

    /** @brief Espacio entre componentes. */
    public static final int GAP = INSET / 2;

    /** @brief Intervalo de actualización del usuario. */
    public static final int UPDATE_USER = 1000;

    /** @brief Serial version UID para la serialización. */
    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * @brief Configura el dispatcher de la ventana.
     * @param wd Dispatcher de ventanas.
     */
    @Override
    public void setDispatcher(final WindowDispatcher wd) {
        this.setDispatcher(wd, (Signal<UserDTO>) null);
    }

    /**
     * @brief Configura el dispatcher de la ventana con una señal de usuario.
     * @param wd Dispatcher de ventanas.
     * @param signal Señal de datos del usuario.
     */

    public void setDispatcher(final WindowDispatcher wd, final Signal<UserDTO> signal) {
        super.setDispatcher(wd, signal);

        final var controller = this.controller();
        if (controller == null) return;

        controller.auth().thenAccept((auth) -> {
            if (!auth) {
                controller.unauth();
                this.replace(AuthWindow.class);

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
                        p.add(new JLabel(k));
                        if (k.equals("Biografía")) {
                            final var t = new JTextArea(v);
                            t.setEditable(false);
                            p.add(new JScrollPane(t));
                        } else {
                            final var t = new JTextField(v);
                            t.setEditable(false);
                            p.add(t);
                        }
                    });
                }, update);

                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1;
                this.add(p, c);
            }

            {
                final var commentPanel = new JPanel(new BorderLayout());
                commentPanel.setBorder(BorderFactory.createTitledBorder("Comentarios adicionales"));

                final var commentArea = new JTextArea(5, 30);
                final var commentScrollPane = new JScrollPane(commentArea);
                commentPanel.add(commentScrollPane, BorderLayout.CENTER);

                c.gridy = 1;
                c.weighty = 0.3;
                c.fill = GridBagConstraints.BOTH;
                this.add(commentPanel, c);
            }


            {
                final var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                final var updateButton = new JButton("Actualizar");
                final var closeButton = new JButton("Cerrar");

                updateButton.addActionListener(e -> updateUserInfo());
                closeButton.addActionListener(e -> this.close());

                buttonPanel.add(updateButton);
                buttonPanel.add(closeButton);

                c.gridy = 2;
                c.weighty = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                this.add(buttonPanel, c);
            }

            this.setVisible(true);
        });
    }

    private void updateUserInfo() {
        System.out.println("Actualizar la información del usuario");
    }
}

