package org.lacabra.store.client.graphical.dispatcher;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public final class Signal<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private T value;

    private transient final Map<Long, Consumer<T>> effects = new HashMap<>();
    private transient final ConcurrentLinkedQueue<SwingWorker<Void, T>> running = new ConcurrentLinkedQueue<>();
    private transient Timer timer;

    public Signal() {
        this.set(null);
    }

    public Signal(final T value) {
        this.set(value);
    }

    public T peek() {
        return this.value;
    }

    public T get() {
        final var v = this.value;

        this.effects.values().forEach(f -> {
            final var w = new SwingWorker<Void, T>() {
                @Override
                public Void doInBackground() {
                    f.accept(v);

                    return null;
                }

                @Override
                public void done() {
                    running.remove(this);
                }
            };

            this.running.add(w);
        });

        return v;
    }

    public T set(final T value) {
        this.value = value;

        this.running.forEach(x -> x.cancel(true));
        for (; this.running.stream().anyMatch(x -> !(x.isCancelled() || x.isDone())); ) ;
        this.running.clear();

        return this.get();
    }

    public Long effect(final Consumer<T> effect) {
        if (effect == null) return null;

        final Set<Long> keys = Set.of(this.effects.keySet().toArray(new Long[0]));
        final Random r = new Random();

        for (int i = 0; i < 1_000_000; i++) {
            final Long l = r.nextLong();
            if (keys.contains(l)) continue;

            this.effects.put(l, effect);

            return l;
        }

        return null;
    }

    public Consumer<T> dispose(final Consumer<T> effect) {
        if (effect == null) return null;

        return this.dispose(this.effects.entrySet().stream().filter(e -> effect.equals(e.getValue())).map(Map.Entry::getKey).findFirst().orElse(null));
    }

    public Consumer<T> dispose(final Long id) {
        if (id == null) return null;

        return this.effects.remove(id);
    }

    public void interval(final int interval) {
        if (interval <= 0) {
            if (this.timer == null)
                return;

            this.timer.stop();

            return;
        }

        this.timer = new Timer(interval, e -> {
            this.get();
        });
    }
}
