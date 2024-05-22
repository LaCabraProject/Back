/**
 * @file AuthWindow.java
 * @brief Define la ventana de autenticación necesaria para la aplicación.
 */

package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * @class AuthWindow
 * @brief Implementa la interfaz gráfica para la ventana de autenticación.
 */
public final class AuthWindow extends DispatchedWindow {
    /**
     * @brief Título de la ventana de autenticación.
     */
    public static final String TITLE = "Autenticación necesaria";

    /**
     * @brief Tamaño de la ventana de autenticación.
     */
    public static final Dimension SIZE = new Dimension(400, 400);

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @param wd Dispatcher de ventanas.
     * @brief Constructor de la ventana de autenticación.
     */
    public AuthWindow(final WindowDispatcher wd) {
        super(wd);
    }

    /**
     * @param dispatcher Dispatcher de ventanas.
     * @brief Configura el dispatcher de la ventana.
     */

    @Override
    public void setDispatcher(final WindowDispatcher dispatcher) {
        super.setDispatcher(dispatcher);

        final WindowDispatcher d = this.getDispatcher();
        if (d == null)
            return;

        final var controller = d.controller();
        if (controller == null)
            return;

        this.auth(() -> this.replace(HomeWindow.class), () -> {
            controller.unauth();

            {
                final var w = windows();

                if (w != null) {
                    final var aw = w.values().stream().filter(x -> x.getClass().equals(AuthWindow.class)).findFirst();
                    if (aw.isPresent()) {
                        this.close();
                        aw.get().requestFocus();
                    }
                }
            }

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setTitle(TITLE);
            this.setSize(SIZE);
            this.setLocationRelativeTo(null);

            this.setLayout(new BorderLayout());

            {
                final JPanel p = new JPanel();
                p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

                {
                    final JPanel p2 = new JPanel();
                    p2.setLayout(new FlowLayout());

                    {
                        final JButton b = new JButton("Iniciar sesión");
                        b.addActionListener(e -> this.replace(LoginWindow.class));

                        p2.add(b);
                    }

                    {
                        final JButton b = new JButton("Crear cuenta");
                        b.addActionListener(e -> this.replace(SignupWindow.class));

                        p2.add(b);
                    }

                    p.add(p2);
                }

                this.getContentPane().add(p, BorderLayout.CENTER);
                this.pack();
                this.setSize((int) (this.getWidth() * 1.5), this.getHeight());

                this.setVisible(true);
            }
        });
    }
}
