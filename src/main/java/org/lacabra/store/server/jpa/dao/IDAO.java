package org.lacabra.store.server.jpa.dao;


import java.util.List;

public interface IDAO<T> {
    public void store(T object);

    public void delete(T object);

    public List<T> findAll();

    public T find(Object param);
}
