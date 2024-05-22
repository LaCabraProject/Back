package org.lacabra.store.client.graphical.dispatcher;

import javax.swing.Timer;
import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public final class Signal<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient final Map<Long, Map.Entry<Consumer<T>, Long[]>> effects = new HashMap<>();
    private transient final ConcurrentLinkedQueue<Map.Entry<Long, SwingWorker<Void, T>>> running =
            new ConcurrentLinkedQueue<>();
    private T value;
    private transient Timer timer;

    public Signal() {
        this.set(null);
    }

    @SafeVarargs
    public Signal(final T value, final Consumer<T>... effects) {
        for (Consumer<T> effect : effects) {
            this.effect(effect);
        }

        this.set(value);
    }

    public boolean isRunning() {
        return this.running.stream().map(Map.Entry::getValue).anyMatch(x -> !(x.isCancelled() || x.isDone()));
    }

    public boolean isRunning(final Consumer<T> effect) {
        return this.isRunning(this.id(effect));
    }

    public boolean isRunning(final Long id) {
        if (id == null)
            return false;

        for (Map.Entry<Long, SwingWorker<Void, T>> worker : this.running) {
            if (!worker.getKey().equals(id))
                continue;

            final var f = worker.getValue();

            return f.isCancelled() || f.isDone();
        }

        return false;
    }

    public Long id(final Consumer<T> effect) {
        return this.effects.entrySet().stream().filter(e -> effect.equals(e.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public T peek() {
        return this.value;
    }

    public T get() {
        final var v = this.value;

        this.effects.forEach((id, f) -> {
            final var w = new SwingWorker<Void, T>() {
                @Override
                public Void doInBackground() {
                    for (; Arrays.stream(f.getValue()).anyMatch(id -> isRunning(id)); ) ;

                    f.getKey().accept(v);

                    return null;
                }

                @Override
                public void done() {
                    running.remove();
                }
            };

            w.doInBackground();
            this.running.add(new AbstractMap.SimpleImmutableEntry<>(id, w));
        });

        return v;
    }

    public T set(final T value) {
        final var old = this.value;

        this.value = value;

        this.running.forEach(x -> x.getValue().cancel(true));
        for (; this.isRunning(); ) ;
        this.running.clear();

        if (Objects.equals(old, this.value))
            return this.value;

        return this.get();
    }

    public Long effect(final Consumer<T> effect, final Long... dependencies) {
        if (effect == null) return null;

        final Set<Long> keys = Set.of(this.effects.keySet().toArray(new Long[0]));
        final Random r = new Random();

        for (int i = 0; i < 1_000_000; i++) {
            final Long l = r.nextLong();
            if (keys.contains(l)) continue;

            this.effects.put(l, new AbstractMap.SimpleImmutableEntry<>(effect, dependencies));

            return l;
        }

        return null;
    }

    public Consumer<T> dispose(final Consumer<T> effect) {
        if (effect == null) return null;

        return this.dispose(this.id(effect));
    }

    public Consumer<T> dispose(final Long id) {
        if (id == null) return null;

        return this.effects.remove(id).getKey();
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
