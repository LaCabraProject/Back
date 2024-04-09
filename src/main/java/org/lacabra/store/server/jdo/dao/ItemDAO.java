package org.lacabra.store.server.jdo.dao;

import jakarta.enterprise.context.ApplicationScoped;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

@ApplicationScoped
public class ItemDAO extends DAO<Item> {
    private static ItemDAO instance;

    static {
        DAO.instances.put(Item.class, ItemDAO.getInstance());
    }

    private ItemDAO() throws NoSuchMethodException {
        super(Item.class, "FindItem");
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