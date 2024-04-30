package org.lacabra.store.internals.type.tuple;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public final class Pair<X extends Serializable, Y extends Serializable> extends Tuple implements Map.Entry<X, Y> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Pair(X x, Y y) {
        super(x, y);
    }

    @SuppressWarnings("unchecked")
    public X x() {
        return (X) this.get(0);
    }

    @SuppressWarnings("unchecked")
    public Y y() {
        return (Y) this.get(1);
    }

    @Override
    public X getKey() {
        return this.x();
    }

    @Override
    public Y getValue() {
        return null;
    }

    @Override
    public Y setValue(Y value) {
        return this.y();
    }
}