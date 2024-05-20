package org.lacabra.store.client.graphical.component;

import org.lacabra.store.internals.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AsyncLoader<T> extends Window implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final static String DEFAULT_MESSAGE = "Cargando...";
    private final static String LOADING_GIF = "gif/spinner.gif";

    public AsyncLoader(final CompletableFuture<T> fetch, final Consumer<T> then) {
        this(fetch, then, DEFAULT_MESSAGE);
    }

    public AsyncLoader(final CompletableFuture<T> fetch, final Consumer<T> then, final String message) {
        this(fetch, then, message, null, null);
    }

    public AsyncLoader(final CompletableFuture<T> fetch, final Consumer<T> then, final String message,
                       final Integer timeout, final T value) {
        this(fetch, then, message, timeout, value, false);
    }

    public AsyncLoader(final CompletableFuture<T> fetch, final Consumer<T> then, final String message,
                       final Integer timeout, final T value, final boolean fast) {
        this(() -> fetch, then, message, timeout, value, fast);
    }

    public AsyncLoader(final Supplier<CompletableFuture<T>> fetch, final Consumer<T> then, final String message,
                       final Integer timeout, final T value, final boolean fast) {
        super(null);

        {
            final var p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

            {
                p.add(Box.createRigidArea(new Dimension(15, 0)));
            }

            {
                final var p2 = new JPanel();
                p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

                {
                    final var l = new JLabel(message == null ? DEFAULT_MESSAGE : message);
                    l.setFont(l.getFont().deriveFont(Font.BOLD, 20f));

                    {
                        p2.add(Box.createRigidArea(new Dimension(0, 15)));
                    }

                    var f = fast;
                    ImageDisplayer id = null;

                    if (!fast) {
                        try {
                            id = new ImageDisplayer(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(LOADING_GIF)).toURI().toURL()).getImage(), 64);
                        } catch (Exception e) {
                            Logger.getLogger().warning(String.format("Could not load file %s: %s.", LOADING_GIF,
                                    e.getMessage()));

                            f = true;
                        }
                    }

                    if (f) {
                        p2.add(l);
                        p2.add(Box.createRigidArea(new Dimension(0, 15)));
                    } else {
                        final var p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

                        {
                            p3.add(id);
                            p3.add(l);
                        }

                        p2.add(p3);
                    }

                    {
                        p2.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }

                p.add(p2);

                {
                    p.add(Box.createRigidArea(new Dimension(15, 0)));
                }
            }

            this.add(p);
        }


        this.pack();
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);

        this.setVisible(true);

        T ret;
        try {
            ret = fetch.get().get(timeout == null ? Integer.MAX_VALUE : timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            this.dispose();
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            if (timeout == null) {
                this.dispose();
                throw new RuntimeException(e);
            }

            ret = value;
        }

        this.dispose();
        then.accept(ret);
    }
}
