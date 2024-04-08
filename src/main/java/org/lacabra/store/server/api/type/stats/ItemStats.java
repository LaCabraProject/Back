package org.lacabra.store.server.api.type.stats;

import org.lacabra.store.server.jdo.dao.ItemDAO;

import java.math.BigInteger;

public record ItemStats() {
    public ItemStats() {
    }

    public static BigInteger total() {
        return ItemDAO.getInstance().count();
    }
}
