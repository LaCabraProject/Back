package org.lacabra.store.server.jpa.dao;

import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.user.User;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemDAO extends DAO<User> {
    private static ItemDAO instance;

    static {
        DAO.instances.put(User.class, ItemDAO.getInstance());
    }

    private ItemDAO() throws NoSuchMethodException {
        super(User.class, "FindUserByCreds");
    }

    public static ItemDAO getInstance() {
        if (ItemDAO.instance == null) {
            try {
                ItemDAO.instance = new ItemDAO();
                DAO.instances.put(User.class, ItemDAO.instance);
            } catch (NoSuchMethodException e) {
                Logger.getLogger().severe(e);
            }
        }

        return ItemDAO.instance;
    }

    @Override
    protected ItemDAO instance() {
        return ItemDAO.instance;
    }
}