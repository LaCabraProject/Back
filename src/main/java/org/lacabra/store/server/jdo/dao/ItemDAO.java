package org.lacabra.store.server.jdo.dao;

import jakarta.enterprise.context.ApplicationScoped;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.item.Item;

import javax.jdo.Query;
import java.util.Collections;

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
                DAO.instances.put(Item.class, ItemDAO.instance);
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

    @Override
    public boolean store(Item item) {
        if (item == null) {
            return super.store((Item) null);
        }

        item = item.merge(new Item(item.id(), null, null, null, null, null, null, null,
                UserDAO.getInstance().findOne(item.parent())));

        return super.store(item.merge(new Item(item.id(), null, null, null, item.keywords() == null ?
                Collections.EMPTY_SET
                : null, item.price() == null ? 0 : null, item.discount() == null ? 0 : item.discount(),
                item.stock() == null ? 0 : item.stock(), null)));
    }
}