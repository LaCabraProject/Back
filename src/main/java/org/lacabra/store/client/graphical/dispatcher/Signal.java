package org.lacabra.store.client.graphical.dispatcher;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public final class Signal<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private T value;
    private final Map<Long, Consumer<T>> effects = new HashMap<>();

    public Signal(final T value) {
        this.set(value);
    }

    public T get() {
        return this.value;
    }

    public T set(final T value) {
        final var v = this.value = value;

        this.effects.values().forEach(f -> (new SwingWorker<Void, T>() {
            @Override
            public Void doInBackground() {
                f.accept(v);

                return null;
            }
        }).execute());

        return v;
    }

    public Long connect(final Consumer<T> effect) {
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

    public Consumer<T> disconnect(final Consumer<T> effect) {
        if (effect == null) return null;

        return this.disconnect(effects.entrySet().stream().filter(e -> effect.equals(e.getValue())).map(Map.Entry::getKey).findFirst().orElse(null));
    }

    public Consumer<T> disconnect(final Long id) {
        if (id == null) return null;

        return effects.remove(id);
    }
}
