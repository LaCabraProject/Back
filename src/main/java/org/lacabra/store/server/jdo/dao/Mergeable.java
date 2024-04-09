package org.lacabra.store.server.jdo.dao;

public interface Mergeable<T> {
    public Mergeable<T> merge(T override);
}
