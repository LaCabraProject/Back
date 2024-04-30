package org.lacabra.store.internals.type.tuple;

import java.io.Serial;
import java.io.Serializable;

public final class Triplet<X extends Serializable, Y extends Serializable, Z extends Serializable> extends Tuple {
    @Serial
    private static final long serialVersionUID = 1L;

    public Triplet(X x, Y y, Z z) {
        super(x, y, z);
    }

    @SuppressWarnings("unchecked")
    public X x() {
        return (X) this.get(0);
    }

    @SuppressWarnings("unchecked")
    public Y y() {
        return (Y) this.get(1);
    }

    @SuppressWarnings("unchecked")
    public Z z() {
        return (Z) this.get(2);
    }
}