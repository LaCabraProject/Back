package org.lacabra.store.server.jdo.dao;

import javax.jdo.Query;
import java.math.BigInteger;
import java.util.List;

public interface IDAO<T> {
    public Query<T> getQuery(String name);

    public void store(T object);

    public void delete(T object);

    public List<T> findAll();

    public List<T> find();

    public List<T> find(Object param);

    public List<T> find(Query<T> query);

    public List<T> find(Query<T> query, Object param);

    public T findOne();

    public T findOne(Object param);

    public T findOne(Query<T> query);

    public T findOne(Query<T> query, Object param);

    public BigInteger countAll();

    public BigInteger count();

    public BigInteger count(Query<T> query);

    public BigInteger count(Query<T> query, Object param);
}
