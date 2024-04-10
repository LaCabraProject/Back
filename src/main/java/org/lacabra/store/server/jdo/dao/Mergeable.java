package org.lacabra.store.server.jdo.dao;

import org.lacabra.store.internals.logging.Logger;

import javax.jdo.annotations.PersistenceCapable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface Mergeable<T> {
    @SuppressWarnings("unchecked")
    default T merge(T override) {
        if (override == null)
            return (T) this;

        for (final Field f : this.getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;

            if (!f.canAccess(this))
                continue;

            if (!f.isAnnotationPresent(PersistenceCapable.class))
                continue;

            var dao = DAO.getInstance(f.getDeclaringClass());
            if (dao == null)
                continue;

            try {
                Object v = dao.findOneAttached(f.get(this));
                if (v == null)
                    continue;

                f.set(this, v);
            } catch (IllegalAccessException e) {
                Logger.getLogger().severe(e);
            }
        }

        return (T) this;
    }
}
