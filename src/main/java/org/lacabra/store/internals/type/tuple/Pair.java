package org.lacabra.store.internals.type.tuple;

import java.io.Serializable;

public final class Pair<X extends Serializable, Y extends Serializable> extends Tuple {
    public Pair(X x, Y y) {
        super(x, y);
    }

    @SuppressWarnings("unchecked")
    public X x() {
        return (X) this.get(0);
    }

    @SuppressWarnings("unchecked")
    public Y y() {
        return (Y) this.get(0);
    }
}