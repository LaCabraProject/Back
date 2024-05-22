package org.lacabra.store.client.graphical.dispatcher;

import org.lacabra.store.client.controller.MainController;

import java.io.Serial;
import java.net.MalformedURLException;

public final class LockedWindowDispatcher extends WindowDispatcher {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean dispatched = false;

    public LockedWindowDispatcher() {
        this(null);
    }

    public LockedWindowDispatcher(final MainController controller) {
        super(controller);
    }

    public static LockedWindowDispatcher fromArgs(final String[] args) throws MalformedURLException {
        return new LockedWindowDispatcher(MainController.fromArgs(args));
    }

    @Override
    public Long dispatch(final Class<? extends DispatchedWindow> cls, final Signal<?>... signals) {
        if (this.dispatched)
            return null;

        this.dispatched = true;

        return super.dispatch(cls, signals);
    }

    @Override
    public Long dispatch(final DispatchedWindow w, final Signal<?>... signals) {
        if (this.dispatched)
            return null;

        this.dispatched = true;

        return super.dispatch(w, signals);
    }
}
