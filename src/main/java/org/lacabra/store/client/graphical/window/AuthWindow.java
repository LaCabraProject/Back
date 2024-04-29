package org.lacabra.store.client.graphical.window;

import org.lacabra.store.client.graphical.DispatchedWindow;
import org.lacabra.store.client.graphical.WindowDispatcher;

import javax.swing.*;
import java.awt.*;

public final class WindowAuth extends DispatchedWindow {
    public final static String TITLE = "Autenticación necesaria";
    public final static Dimension SIZE = new Dimension(400, 400);

    public WindowAuth() {
        this(null);
    }

    public WindowAuth(WindowDispatcher wd) {
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

            this.setLayout(new BorderLayout());

            {
                final JPanel p = new JPanel();
                p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

                {
                    final JPanel p2 = new JPanel();
                    p2.setLayout(new FlowLayout());

                    {
                        final JButton b = new JButton("Iniciar sesión");
                        b.addActionListener(e -> {
                            this.close();
                            this.dispatch(WindowLogin.class);
                        });

                        p2.add(b);
                    }

                    {
                        final JButton b = new JButton("Crear cuenta");
                        b.addActionListener(e -> {
                            this.close();
                            this.dispatch(WindowRegister.class);
                        });

                        p2.add(b);
                    }

                    p.add(p2);
                }

                this.getContentPane().add(p, BorderLayout.CENTER);
            }
        });
    }
}
