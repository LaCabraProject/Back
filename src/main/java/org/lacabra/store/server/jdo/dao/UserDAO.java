package org.lacabra.store.server.jdo.dao;

import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.user.User;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDAO extends DAO<User> {
    private static UserDAO instance;

    static {
        DAO.instances.put(User.class, UserDAO.getInstance());
    }

    private UserDAO() throws NoSuchMethodException {
        super(User.class, "FindUser");
    }

    public static UserDAO getInstance() {
        if (UserDAO.instance == null) {
            try {
                UserDAO.instance = new UserDAO();
                DAO.instances.put(User.class, UserDAO.instance);
            } catch (NoSuchMethodException e) {
                Logger.getLogger().severe(e);
            }
        }

        return UserDAO.instance;
    }

    @Override
    protected UserDAO instance() {
        return UserDAO.instance;
    }
}