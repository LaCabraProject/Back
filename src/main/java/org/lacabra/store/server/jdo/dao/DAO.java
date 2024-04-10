package org.lacabra.store.server.jdo.dao;

import jakarta.el.MethodNotFoundException;
import org.datanucleus.api.jdo.JDOQuery;
import org.lacabra.store.internals.logging.Logger;

import javax.jdo.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

public abstract class DAO<T extends Serializable> implements IDAO<T> {
    protected static final Map<Class<?>, DAO<?>> instances = new HashMap<>();
    private final static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("LaCabraProject");

    protected final static PersistenceManager pm = pmf.getPersistenceManager();
    protected Class<T> objClass;
    protected Method pkGetter;
    protected Query<T> pkQuery;

    protected DAO(Class<T> objClass, Method pkGetter) {
        super();

        this.objClass = Objects.requireNonNull(objClass);
        this.pkGetter = Objects.requireNonNull(pkGetter);

        if (Arrays.stream(objClass.getMethods()).noneMatch((Method m) -> m.equals(pkGetter)))
            throw new MethodNotFoundException(String.format("No method called %s in class %s.", pkGetter,
                    objClass.getName()));
    }

    @SuppressWarnings("unchecked")
    protected DAO(Class<T> objClass, String pkQuery) {
        super();

        this.objClass = Objects.requireNonNull(objClass);
        this.pkQuery = (Query<T>) pm.newNamedQuery(this.objClass, Objects.requireNonNull(pkQuery));
    }

    public static DAO<?> getInstance(Class<?> objClass) {
        DAO<?> ret = DAO.instances.get(objClass);

        if (ret != null) return ret;

        Optional<?> opt = DAO.instances.keySet().stream().filter((k) -> k.isAssignableFrom(objClass)).findFirst();
        return opt.isPresent() ? DAO.instances.get(opt.get()) : null;
    }

    protected abstract DAO<T> instance();

    @Override
    public Query<T> getQuery(String name) {
        return pm.newNamedQuery(this.objClass, name);
    }

    public boolean delete(T object) {
        Object del = null;
        try {
            del = this.find(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (del == null) return false;

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.flush();
            pm.removeUserObject(del);
            tx.commit();

            Logger.getLogger().info(String.format("%s deleted successfully.", del));
            return true;
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("Could not delete object %s: %s", del, e.getMessage()), e);
            return false;
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
        }
    }

    public boolean store(T object) {
        if (object == null) {
            Logger.getLogger().warning("tried to store null object.");

            return false;
        }

        T found = this.findOne(object);

        if (found != null) {
            if (found instanceof Mergeable) {
                ((Mergeable<T>) found).merge(object);
            } else this.delete(found);
        }

        Transaction tx = pm.currentTransaction();

        try {
            if (!(found instanceof Mergeable<?>)) {
                tx.begin();
                pm.flush();
                pm.makePersistent(object);
                tx.commit();
            }

            Logger.getLogger().info(String.format("%s stored successfully.", object));

            return true;
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("Could not store %s %s: %s (%s)",
                    Pattern.compile("^.").matcher(Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A"
                            + "-Z" + "][^A-Z])")).reduce("", (total, str) -> total + str.toLowerCase()).toLowerCase()).replaceFirst(m -> m.group().toUpperCase()), object.toString(), e.getMessage(), e.getCause()));

            return false;
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
        }
    }

    public boolean store(Collection<T> objects) {
        return objects.stream().map(this::store).reduce(true, (all, sub) -> all && sub);
    }

    public boolean store(T[] objects) {
        return this.store(Arrays.stream(objects).toList());
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return this.find();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<T> find() {
        return this.find((Query<T>) pm.newQuery(String.format("SELECT b FROM %s b",
                this.instance().objClass.getSimpleName())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> find(Object param) {
        if (param instanceof Query q) return this.find((Query<T>) q, null);

        return this.find(this.pkQuery, param);
    }

    @Override
    public List<T> find(Query<T> query) {
        return this.find(query, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> find(Query<T> query, Object param) {
        return (List<T>) this.findCountAttached(query, param, false, true, false);
    }

    private Object findCountAttached(Query<T> query, Object param, boolean count, boolean detached, boolean one) {
        Object ret = count ? BigInteger.ZERO : Collections.EMPTY_LIST;

        if (query == null && param == null) return ret;

        pm.getFetchPlan().setGroup(FetchGroup.ALL);
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.flush();

            if (query == null) {
                ret = Collections.singletonList(pm.getObjectById(this.instance().objClass,
                        this.pkGetter.invoke(param)));
            } else {
                if (param != null) {
                    Map<String, Object> params = new HashMap<>();
                    for (Field f : param.getClass().getDeclaredFields()) {
                        if (Modifier.isStatic(f.getModifiers())) continue;

                        params.put(f.getName(), null);

                        if (f.canAccess(param)) params.put(f.getName(), f.get(param));
                    }

                    for (Method m : param.getClass().getDeclaredMethods()) {
                        if (!params.containsKey(m.getName())) continue;

                        if (!Modifier.isStatic(m.getModifiers()) && m.canAccess(param))
                            params.putIfAbsent(m.getName(), m.invoke(param));
                    }

                    query.setNamedParameters(Map.ofEntries(params.entrySet().stream().filter(x -> x.getValue() != null).toArray(Map.Entry[]::new)));
                }

                if (count) {
                    query.setResultClass(BigInteger.class);
                    ret = query.executeUnique();

                    if (detached)
                        ret = pm.detachCopy(ret);
                } else if (one) {
                    T r;

                    if (query instanceof JDOQuery<T> q) {
                        r = q.executeResultUnique(this.objClass);
                    } else {
                        query.setResultClass(this.objClass);
                        r = query.executeUnique();
                    }

                    if (detached && r != null)
                        ret = pm.detachCopy(r);

                    else if (!detached)
                        ret = r;
                } else {
                    List<T> rl;

                    if (query instanceof JDOQuery<T> q) {
                        rl = q.executeResultList(this.objClass);
                    } else {
                        query.setResultClass(this.objClass);
                        rl = query.executeList();
                    }

                    if (detached && !(rl == null || rl.isEmpty())) {
                        ret = pm.detachCopyAll(rl).stream().toList();
                    } else if (!detached)
                        ret = rl;
                }
            }

            tx.commit();

            Logger.getLogger().info(!count && (ret == null || (ret instanceof List<?> && ((List<?>) ret).isEmpty())) ?
                    String.format("%s not " +
                                    "found (param: %s).",
                            Pattern.compile("^.").matcher(Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A"
                                    + "-Z][^A-Z])")).reduce("", (total, str) -> total + "" + str).toLowerCase()).replaceFirst(m -> m.group().toUpperCase()), param == null ? "(null)" : param) : String.format("%s found.", ret));

            return ret;
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("There was an error trying to %s %ss %s: %s%s", count ? "count"
                            : "find",
                    Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A-Z][^A-Z])")).reduce("",
                            (total, str) -> total + str).toLowerCase(), query != null ? "(" + query.toString() + ")"
                            : this.pkQuery != null ? "(" + this.pkQuery.toString() + ")" :
                            this.instance().pkGetter.getName().startsWith("get") ?
                                    ("by " + this.instance().pkGetter.getName().substring("get".length()).toLowerCase()) : ("using method" + this.instance().pkGetter.getName()), e.getMessage(), e.getCause() == null ? "" : ("(" + e.getCause() + ")")));

            e.printStackTrace();

            return ret;
        } finally {
            if (query != null) query.closeAll();
            if (tx != null && tx.isActive()) tx.rollback();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T findOne() {
        return (T) this.findOne(pm.newQuery("javax.jdo.query.SQL",
                "SELECT From " + this.objClass.getSimpleName()), null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T findOne(Object param) {
        if (param instanceof Query<?> q)
            return this.findOne((Query<T>) q, null);

        return this.findOne(this.pkQuery, param);
    }

    @Override
    public final T findOne(Query<T> query) {
        return this.findOne(query, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T findOne(Query<T> query, Object param) {
        return (T) this.findCountAttached(query, param, false, false, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<T> findAttached() {
        return this.findAttached((Query<T>) pm.newQuery(String.format("SELECT b FROM %s b",
                this.instance().objClass.getSimpleName())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAttached(Object param) {
        if (param instanceof Query q) return this.findAttached((Query<T>) q, null);

        return this.find(this.pkQuery, param);
    }

    @Override
    public List<T> findAttached(Query<T> query) {
        return this.findAttached(query, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAttached(Query<T> query, Object param) {
        return (List<T>) this.findCountAttached(query, param, false, false, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T findOneAttached() {
        return (T) this.findOneAttached(pm.newQuery("javax.jdo.query.SQL",
                "SELECT From " + this.objClass.getSimpleName()), null);
    }

    @Override
    public final T findOneAttached(Object param) {
        return this.findOneAttached(this.pkQuery, param);
    }

    @Override
    public final T findOneAttached(Query<T> query) {
        return this.findOneAttached(query, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T findOneAttached(Query<T> query, Object param) {
        return (T) this.findCountAttached(query, param, false, true, true);
    }

    @Override
    public final BigInteger countAll() {
        return this.count();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final BigInteger count() {
        return this.count((Query<T>) pm.newQuery("javax.jdo.query.SQL", String.format("SELECT COUNT(*) FROM %s",
                this.instance().objClass.getSimpleName().toUpperCase())));
    }

    @Override
    public final BigInteger count(Query<T> query) {
        return this.count(query, null);
    }

    @Override
    public final BigInteger count(Query<T> query, Object param) {
        return (BigInteger) this.findCountAttached(query, param, true, false, false);
    }


}