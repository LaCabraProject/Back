package org.lacabra.store.types;

import org.junit.jupiter.api.Test;
import org.lacabra.store.internals.type.id.ObjectId;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public final class ObjectIdTest {
    @Test
    public void create() {
        assertNull(ObjectId.from(-1));
        assertNotNull(ObjectId.from(0));
        assertNotNull(ObjectId.from("ff"));
    }

    @Test
    public void equals() {
        assertEquals(ObjectId.from(0), 0);
        assertEquals(ObjectId.from(0x11), BigDecimal.valueOf(17));
        assertEquals(ObjectId.from(ObjectId.MIN), ObjectId.from(0));
    }
}
