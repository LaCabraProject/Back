package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.dispatcher.DispatchedWindow;
import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public final class AuthWindow extends DispatchedWindow {
    public final static String TITLE = "Autenticación necesaria";
    public final static Dimension SIZE = new Dimension(400, 400);
    @Serial
    private final static long serialVersionUID = 1L;

    public AuthWindow(final WindowDispatcher wd) {
        super(wd);
    }

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
