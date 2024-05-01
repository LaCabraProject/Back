package org.lacabra.store.server.jdo.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.item.Item;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@ApplicationScoped
public class ItemDAO extends DAO<Item> {
    private static ItemDAO instance;

    static {
        var dao = ItemDAO.getInstance();
        DAO.instances.put(Item.class, dao);

        try (InputStream in = ItemDAO.class.getClassLoader().getResourceAsStream("data/items.json")) {
            if (in != null) {
                ObjectMapper mapper = new ObjectMapperProvider().getContext(Item[].class);
                dao.store(mapper.readValue(in, Item[].class));
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
    @SuppressWarnings("unchecked")
    public boolean store(Item item) {
        if (item == null) {
            return super.store((Item) null);
        }

        item = item.merge(new Item(item.id(), null, null, null, null, null, null, null,
                item.parent() == null ? null : UserDAO.getInstance().findOne(item.parent())));

        return super.store(item.merge(new Item(item.id(), null, null, null, item.keywords() == null ?
                Collections.EMPTY_SET
                : null, item.price() == null ? 0 : null, item.discount() == null ? 0 : item.discount(),
                item.stock() == null ? 0 : item.stock(), null)));
    }
}