package org.lacabra.store.client.graphical.dispatcher;


import org.lacabra.store.client.controller.MainController;
import org.lacabra.store.client.graphical.component.AsyncLoader;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class DispatchedWindow extends JFrame implements IWindowDispatcher {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final static String AUTH_MESSAGE = "Autenticando...";
    protected final static int AUTH_TIMEOUT = 5000;

    private static long INSTANCES = 0;
    final private long instance;

    private WindowDispatcher dispatcher;

    public DispatchedWindow() {
        this(null);
    }

    public DispatchedWindow(final WindowDispatcher wd) {
        this(wd, (Signal<?>) null);
    }

    public DispatchedWindow(final WindowDispatcher wd, final Serializable... attrs) {
        this(wd, Arrays.stream(attrs == null ? new Serializable[0] : attrs).map(x -> {
            if (x instanceof Signal<?> s) {
                return s;
            }

            return new Signal<>(x);
        }).toArray(Signal[]::new));
    }

    public DispatchedWindow(final WindowDispatcher wd, final Signal<?>... signals) {
        super();
        this.instance = DispatchedWindow.INSTANCES++;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.dispatcher = wd;
        this.setDispatcher(wd, signals);
    }

    public WindowState state() {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.stateOf(this);
    }

    public Signal<?>[] signals() {
        final var state = this.state();

        if (state == null) return new Signal<?>[0];

        return state.getAll();
    }

    public Long[] connect(final Signal<?>... signals) {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.connect(this, signals);
    }

    public Long connect(final Signal<?> signal) {
        final var ret = this.connect(new Signal<?>[]{signal});
        if (ret == null || ret.length == 0) return null;

        return ret[0];
    }

    public Signal<?> disconnect(final Signal<?> signal) {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.disconnect(this, signal);
    }

    public Signal<?> disconnect(final Long signal) {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.disconnect(this, signal);
    }

    public Map<Long, DispatchedWindow> windows() {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.windows();
    }

    public MainController controller() {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.controller();
    }

    public WindowDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void setDispatcher(final WindowDispatcher dispatcher) {
        this.setDispatcher(dispatcher, (Signal<?>) null);
    }

    public void setDispatcher(final WindowDispatcher dispatcher, final Signal<?>... signals) {
        if (Objects.equals(dispatcher, this.dispatcher)) {
            this.connect(signals);
            return;
        }

        if (this.dispatcher != null) this.dispatcher.close(this);

        this.dispatcher = dispatcher;
        dispatcher.dispatch(this);
        this.connect(signals);
    }

    public Long getId() {
        return this.getId(this);
    }

    public Long getId(final DispatchedWindow w) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.getId(w);
    }

    public DispatchedWindow getWindow(final Long id) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.getWindow(id);
    }

    public Long dispatch(final Class<? extends DispatchedWindow> w, final Signal<?>... signals) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.dispatch(w, signals);
    }

    public Long dispatch(final DispatchedWindow w, final Signal<?>... signals) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.dispatch(w, signals);
    }

    public DispatchedWindow replace(final Class<? extends DispatchedWindow> cls, final Signal<?>... signals) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final var d = this.getDispatcher();

        if (d == null) {
            this.close();
            return null;
        }

        d.close(this);
        return d.getWindow(d.dispatch(cls, signals));
    }

    public DispatchedWindow replace(final DispatchedWindow w, final Signal<?>... signals) {
        final var d = this.getDispatcher();

        if (d == null) {
            this.close();
            return null;
        }

        d.close(this);
        return d.getWindow(d.dispatch(w, signals));
    }

    public boolean close(final Long id) {
        final var d = this.getDispatcher();

        if (d == null) return false;

        return d.close(id);
    }

    public boolean close(final DispatchedWindow w) {
        final var d = this.getDispatcher();

        if (d == null) return false;

        return d.close(w);
    }

    public void close() {
        final var d = this.getDispatcher();

        if (d == null || !d.close(this)) this.dispose();
    }

    public JPanel banner() {
        return this.banner(this);
    }

    public JPanel banner(final JFrame frame) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.banner(frame);
    }

    public JPanel footer() {
        return this.footer(false);
    }

    public JPanel footer(final boolean full) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.footer(full);
    }

    public void message(final String message) {
        this.message(this, message);
    }

    public void message(final Long id, final String message) {
        final var d = this.getDispatcher();
        if (d == null) return;

        d.message(id, message);
    }

    public void message(final DispatchedWindow w, final String message) {
        this.message(this.getId(w), message);
    }

    public String input(final String message) {
        return this.input(this, message);
    }

    public String input(final Long id, final String message) {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.input(id, message);
    }

    public String input(final DispatchedWindow w, final String message) {
        return this.input(this.getId(w), message);
    }

    private List<Pair<Component, Boolean>> getCompState(final Container c) {
        final var ret = new ArrayList<Pair<Component, Boolean>>();

        ret.add(new Pair<>(c, c.isEnabled()));
        for (Component comp : c.getComponents()) {
            ret.add(new Pair<>(comp, comp.isEnabled()));

            if (comp instanceof Container cont) ret.addAll(getCompState(cont));
        }

        return ret;
    }

    public void load(final CompletableFuture<?> fetch, final Consumer<?> then, final String message,
                     final Integer timeout, final Object value) {
        this.load(() -> fetch, then, message, timeout, value);
    }

    public void load(final Supplier<CompletableFuture<?>> fetch, final Consumer<?> then, final String message,
                     final Integer timeout, final Object value) {
        final List<Pair<Component, Boolean>> state = getCompState(this);
        state.forEach(x -> x.x().setEnabled(false));

        try {
            new AsyncLoader(fetch, (v) -> {
                state.forEach(x -> x.x().setEnabled(x.y()));
                ((Consumer) then).accept(v);
            }, message, timeout, value, false).setLocationRelativeTo(this.isVisible() ? this : null);
        } catch (Exception e) {
            Logger.getLogger().severe(e);
            state.forEach(x -> x.x().setEnabled(x.y()));
        }
    }

    public void auth(final Runnable yes, final Runnable no) {
        final var controller = controller();
        if (controller == null) {
            no.run();

            return;
        }

        this.load(controller.auth(), (final Boolean auth) -> {
            if (auth) yes.run();

            else no.run();
        }, AUTH_MESSAGE, AUTH_TIMEOUT, false);
    }

    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        return switch (o) {
            case DispatchedWindow w -> this.instance == w.instance;

            case null, default -> false;
        };
    }
}
