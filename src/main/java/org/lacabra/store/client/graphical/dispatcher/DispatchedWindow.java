package org.lacabra.store.client.graphical;


import org.lacabra.store.client.controller.MainController;

import javax.swing.*;

public abstract class DispatchedWindow extends JFrame {
    private WindowDispatcher dispatcher;

    public DispatchedWindow() {
        super();

        this.setDispatcher(null);
    }

    public DispatchedWindow(final WindowDispatcher wd) {
        super();

        this.setDispatcher(wd);
    }

    public MainController controller() {
        final var d = this.getDispatcher();

        if (d == null)
            return null;

        return d.controller();
    }

    public JPanel banner() {
        final var d = this.getDispatcher();

        if (d == null)
            return null;

        return d.banner(this);
    }

    public JPanel footer() {
        final var d = this.getDispatcher();

        if (d == null)
            return null;

        return d.footer();
    }

    public WindowDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void setDispatcher(final WindowDispatcher dispatcher) {
        this.close();
        this.dispatcher = dispatcher;
    }

    public DispatchedWindow getWindow (final Long id) {
        final var d = this.getDispatcher();

        if (d == null)
            return null;

        return d.getWindow(id);
    }

    public Long dispatch(final Class<? extends DispatchedWindow> w) {
        final var d = this.getDispatcher();

        if (d == null)
            return null;

        return d.dispatch(w);
    }

    public Long dispatch(final DispatchedWindow w) {
        final var d = this.getDispatcher();

        if (d == null)
            return null;

        return d.dispatch(w);
    }

    public void close() {
        if (!this.dispatcher.close(this))
            this.dispose();
    }
}
