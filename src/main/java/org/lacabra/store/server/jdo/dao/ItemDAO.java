package org.lacabra.store.server.jdo.dao;

import jakarta.enterprise.context.ApplicationScoped;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.item.Item;

import java.io.IOException;

@ApplicationScoped
public final class ItemDAO extends DAO<Item> {
    private static ItemDAO instance;

    static {
        final var dao = ItemDAO.getInstance();
        DAO.instances.put(Item.class, dao);

        try (final var in = ItemDAO.class.getClassLoader().getResourceAsStream("data/items.json")) {
            if (in != null) {
                dao.store(new ObjectMapperProvider().getContext(Item[].class).readValue(in, Item[].class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        if (item != null)
            item = item.merge(new Item(item.id(), null, null, null, null, null, null, null, item.parent() == null ?
                    null : UserDAO.getInstance().findOne(item.parent())));

        return super.store(item);
    }
}