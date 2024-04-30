package org.lacabra.store.client.graphical.dispatcher;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public final class WindowState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final HashMap<Long, Signal<?>> signals = new HashMap<>();

    public WindowState() {
        this((Signal<?>) null);
    }

    public WindowState(final Signal<?>... signals) {
        super();
        this.connect(signals);
    }

    public Signal<?>[] getAll() {
        return this.signals.values().toArray(Signal[]::new);
    }

    public Signal<?> get(final Long id) {
        if (id == null)
            return null;

        return this.signals.get(id);
    }

    public Long get(final Signal<?> signal) {
        if (signal == null)
            return null;

        return this.signals.entrySet().stream().filter(e -> e.getValue().equals(signal)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public Long[] connect(final Signal<?>... signals) {
        if (signals == null)
            return new Long[0];

        return Stream.of(signals).map(this::connect).toArray(Long[]::new);
    }

    public Long connect(final Signal<?> signal) {
        if (signal == null)
            return null;

        final Set<Long> keys = Set.of(this.signals.keySet().toArray(new Long[0]));
        final Random r = new Random();

        for (int i = 0; i < 1_000_000; i++) {
            final Long l = r.nextLong();
            if (keys.contains(l)) continue;

            this.signals.put(l, signal);

            return l;
        }

        return null;
    }

    public Signal<?> disconnect(final Signal<?> signal) {
        return this.disconnect(this.get(signal));
    }

    public Signal<?> disconnect(final Long id) {
        if (id == null)
            return null;

        return this.signals.remove(id);
    }
}
