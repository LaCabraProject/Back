package org.lacabra.store.client.graphical.dispatcher;


import org.lacabra.store.client.controller.MainController;

import javax.swing.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public abstract class DispatchedWindow extends JFrame implements IWindowDispatcher {
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

        this.setDispatcher(wd, signals);
    }

    public WindowState state() {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.stateOf(this);
    }

    public Signal<?>[] signals() {
        final var state = this.state();

        if (state == null)
            return new Signal[0];

        return state.getAll();
    }

    public Long connect(final Signal<?> signal) {
        final var d = this.getDispatcher();
        if (d == null) return null;

        return d.connect(this, signal);
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
        this.close();
        this.dispatcher = dispatcher;
        this.state().connect(signals);
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

    public Long dispatch(final Class<? extends DispatchedWindow> w) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.dispatch(w);
    }

    public Long dispatch(final DispatchedWindow w) {
        final var d = this.getDispatcher();

        if (d == null) return null;

        return d.dispatch(w);
    }

    public DispatchedWindow replace(final Class<? extends DispatchedWindow> cls) {
        final var d = this.getDispatcher();

        this.close();

        if (d == null) return null;

        return d.getWindow(d.dispatch(cls));
    }

    public DispatchedWindow replace(final DispatchedWindow w) {
        final var d = this.getDispatcher();

        this.close();

        if (d == null) return null;

        return d.getWindow(d.dispatch(w));
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
        if (!this.dispatcher.close(this)) this.dispose();
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
}
