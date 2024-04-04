package org.lacabra.store.server.jpa.dao;

import jakarta.el.MethodNotFoundException;
import jakarta.persistence.*;
import org.lacabra.store.internals.logging.Logger;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public abstract class DAO<T extends Serializable> implements IDAO<T> {
    protected static final Map<Class<?>, DAO<?>> instances = new HashMap<>();
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Strava");
    protected final static EntityManager em = emf.createEntityManager();
    protected Class<T> objClass;
    protected Method pkGetter;
    protected TypedQuery<T> pkQuery;

    protected DAO(Class<T> objClass, Method pkGetter) {
        super();

        this.objClass = Objects.requireNonNull(objClass);
        this.pkGetter = Objects.requireNonNull(pkGetter);

        if (Arrays.stream(objClass.getMethods()).noneMatch((Method m) -> m.equals(pkGetter)))
            throw new MethodNotFoundException(
                    String.format("No method called %s in class %s.", pkGetter, objClass.getName()));
    }

    protected DAO(Class<T> objClass, String pkQuery) {
        super();

        this.objClass = Objects.requireNonNull(objClass);
        this.pkQuery = em.createNamedQuery(Objects.requireNonNull(pkQuery), this.objClass);
    }

    public final static DAO<?> getInstance(Class<?> objClass) {
        DAO<?> ret = DAO.instances.get(objClass);

        if (ret != null)
            return ret;

        Class<?> sup = DAO.instances.keySet().stream().filter((k) -> k.isAssignableFrom(objClass)).findFirst().get();
        return sup != null ? DAO.instances.get(sup) : null;
    }

    protected abstract DAO<T> instance();

    public void delete(T object) {
        Object del = null;
        try {
            del = this.find(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (del == null)
            return;

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.flush();
            em.remove(del);
            tx.commit();

            Logger.getLogger().info(String.format("%s deleted successfully.", object.toString()));
        } catch (Exception e) {
            Logger.getLogger()
                    .warning(String.format("Could not delete object %s: %s", object.toString(), e.getMessage()), e);
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();
        }
    }

    public void store(T object) {
        T found = null;
        try {
            found = this.instance().find(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.flush();

            if (found != null) {
                em.merge(object);
            } else
                em.persist(object);

            tx.commit();

            Logger.getLogger().info(String.format("%s stored successfully.", object.toString()));
        } catch (Exception e) {
            Logger.getLogger()
                    .warning(String.format("Could not store %s %s: %s (%s)", Pattern.compile("^.")
                                    .matcher(Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A-Z" +
                                                    "][^A-Z])"))
                                            .reduce("", (total, str) -> total + "" + str).toLowerCase())
                                    .replaceFirst(m -> m.group().toUpperCase()), object.toString(), e.getMessage(),
                            e.getCause()));
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();
        }
    }

    public void store(Collection<T> objects) {
        objects.iterator().forEachRemaining((x) -> this.store(x));
    }

    public void store(T[] objects) {
        this.store(Arrays.stream(objects).toList());
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        EntityTransaction tx = em.getTransaction();

        List<T> objs = new ArrayList<T>();

        try {
            tx.begin();
            em.flush();

            objs = em.createQuery(String.format("SELECT b FROM %s b", this.instance().objClass.getSimpleName()))
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            Logger.getLogger()
                    .severe(String
                            .format("There was an error trying to retrieve all %ss: %s",
                                    Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A-Z][^A-Z])"))
                                            .reduce("", (total, str) -> total + "" + str).toLowerCase(),
                                    e.getMessage()));
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }

        return objs;
    }

    public T find(Object param) {
        return this.find(this.pkQuery, param);
    }

    @SuppressWarnings("unchecked")
    public T find(TypedQuery<T> query, Object param) {
        if (param == null)
            return null;

        EntityTransaction tx = em.getTransaction();

        T ret = null;

        try {
            tx.begin();
            em.flush();

            if (query == null)
                ret = em.find(this.instance().objClass, this.pkGetter.invoke(param));

            else {
                try {
                    query.getParameters().forEach((p) -> {
                        try {
                            query.setParameter(p.getName(), param.getClass().getField(p.getName()));
                        } catch (NoSuchFieldException e) {
                            try {
                                query.setParameter(p.getName(), param.getClass().getMethod(p.getName()).invoke(param));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        } catch (Exception e) {
                            throw e;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }

                var rl = query.getResultList();
                if (!(rl == null || rl.isEmpty()))
                    ret = query.getSingleResult();
            }

            tx.commit();

            Logger.getLogger()
                    .info(ret == null
                            ? String.format("%s not found (param: %s).",
                            Pattern.compile("^.").matcher(Arrays
                                            .stream(this.instance().objClass.getSimpleName().split("(?=[A-Z][^A-Z])"))
                                            .reduce("", (total, str) -> total + "" + str).toLowerCase())
                                    .replaceFirst(m -> m.group().toUpperCase()),
                            param)
                            : String.format("%s found.", ret.toString()));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("There was an error trying to find %s %s: %s",
                    Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A-Z][^A-Z])"))
                            .reduce("", (total, str) -> total + "" + str).toLowerCase(),
                    this.pkQuery != null ? "(" + this.pkQuery.toString() + ")"
                            : this.instance().pkGetter.getName().startsWith("get") ? ("by "
                            + this.instance().pkGetter.getName().substring("get".length()).toLowerCase())
                            : ("using method" + this.instance().pkGetter.getName()),
                    e.getMessage()));
        } finally {
            if (tx != null && tx.isActive())
                tx.rollback();
        }

        return ret;
    }
}