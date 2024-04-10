package org.lacabra.store.server.jdo.dao;

import javax.jdo.Query;
import java.math.BigInteger;
import java.util.List;

public interface IDAO<T> {
    Query<T> getQuery(String name);

    boolean store(T object);

    boolean delete(T object);

    List<T> findAll();

    List<T> find();

    List<T> find(Object param);

    List<T> find(Query<T> query);

    List<T> find(Query<T> query, Object param);

    T findOne();

    T findOne(Object param);

    T findOne(Query<T> query);

    T findOne(Query<T> query, Object param);

    List<T> findAttached();

    List<T> findAttached(Object param);

    List<T> findAttached(Query<T> query);

    List<T> findAttached(Query<T> query, Object param);

    T findOneAttached();

    T findOneAttached(Object param);

    T findOneAttached(Query<T> query);

    T findOneAttached(Query<T> query, Object param);

    BigInteger countAll();

    BigInteger count();

    BigInteger count(Query<T> query);

    BigInteger count(Query<T> query, Object param);
}
