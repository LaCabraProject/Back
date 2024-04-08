package org.lacabra.store.server.jdo.dao;

import jakarta.el.MethodNotFoundException;
import org.lacabra.store.internals.logging.Logger;

import javax.jdo.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public void delete(T object) {
        Object del = null;
        try {
            del = this.find(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (del == null) return;

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.flush();
            pm.removeUserObject(del);
            tx.commit();

            Logger.getLogger().info(String.format("%s deleted successfully.", object.toString()));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("Could not delete object %s: %s", object.toString(),
                    e.getMessage()), e);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
        }
    }

    public void store(T object) {
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.flush();
            pm.makePersistent(object);
            tx.commit();

            Logger.getLogger().info(String.format("%s stored successfully.", object.toString()));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("Could not store %s %s: %s (%s)",
                    Pattern.compile("^.").matcher(Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A"
                            + "-Z" + "][^A-Z])")).reduce("", (total, str) -> total + "" + str).toLowerCase()).replaceFirst(m -> m.group().toUpperCase()), object.toString(), e.getMessage(), e.getCause()));
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
        }
    }

    public void store(Collection<T> objects) {
        objects.iterator().forEachRemaining(this::store);
    }

    public void store(T[] objects) {
        this.store(Arrays.stream(objects).toList());
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return this.find();
    }

    @Override
    public final List<T> find() {
        return this.find(pm.newQuery(String.format("SELECT b FROM %s b", this.instance().objClass.getSimpleName())));
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
        return (List<T>) this.findCount(query, param, false);
    }

    private Object findCount(Query<T> query, Object param, boolean count) {
        Object ret = count ? BigInteger.ZERO : Collections.EMPTY_LIST;

        if (query == null && param == null) return ret;

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
                    for (Field f : param.getClass().getFields()) {
                        if (f.canAccess(param)) params.put(f.getName(), f.get(param));
                    }

                    for (Method m : param.getClass().getMethods()) {
                        if (params.containsKey(m.getName())) continue;

                        if (m.canAccess(param)) params.put(m.getName(), m.invoke(param));
                    }

                    query.setNamedParameters(params);
                }

                if (count) {
                    query.setResultClass(Integer.class);
                    Iterator<?> it = query.executeList().iterator();
                    Object r = BigInteger.ZERO;
                    if (it.hasNext()) {
                        r = it.next();
                        if (!(r instanceof Number)) throw new Exception("result of count query is not a number: " + r);

                        ret = new BigInteger(String.valueOf(r));
                    }
                } else {
                    query.setResultClass(this.objClass);
                    var rl = query.executeList();
                    if (!(rl == null || rl.isEmpty())) ret = rl;
                }
            }

            tx.commit();

            Logger.getLogger().info(!count && ((List<?>) ret).isEmpty() ? String.format("%s not found (param: %s).",
                    Pattern.compile("^.").matcher(Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A"
                            + "-Z][^A-Z])")).reduce("", (total, str) -> total + "" + str).toLowerCase()).replaceFirst(m -> m.group().toUpperCase()), param) : String.format("%s found.", ret));
        } catch (Exception e) {
            Logger.getLogger().warning(String.format("There was an error trying to %s %ss %s: %s", count ? "count" :
                    "find", Arrays.stream(this.instance().objClass.getSimpleName().split("(?=[A-Z][^A-Z])")).reduce(
                    "", (total, str) -> total + str).toLowerCase(), query != null ? "(" + query.toString() +
                    ")" : this.pkQuery != null ? "(" + this.pkQuery.toString() + ")" :
                    this.instance().pkGetter.getName().startsWith("get") ?
                            ("by " + this.instance().pkGetter.getName().substring("get".length()).toLowerCase()) : (
                            "using method" + this.instance().pkGetter.getName()), (e.getCause() == null ?
                    e.getMessage() : e.getCause()).toString()));
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
        }

        return ret;
    }

    @Override
    public final T findOne() {
        return this.find().getFirst();
    }

    @Override
    public final T findOne(Object param) {
        return this.find(param).getFirst();
    }

    @Override
    public final T findOne(Query<T> query) {
        return this.find(query).getFirst();
    }

    @Override
    public final T findOne(Query<T> query, Object param) {
        return this.find(query, param).getFirst();
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
        return (BigInteger) this.findCount(query, param, true);
    }
}