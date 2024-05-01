package org.lacabra.store.client.assembler;

import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAssembler {
    private static ItemAssembler instance;

    private ItemAssembler() {
        super();
    }

    public static ItemAssembler getInstance() {
        if (instance == null) {
            instance = new ItemAssembler();
        }

        return instance;
    }

    public ItemDTO ItemToDTO(final Item item) {
        if (item == null)
            return new ItemDTO(null);

        return new ItemDTO(item.id(), item.type(), item.name(), item.description(), item.keywords(), item.price(),
                item.discount(), item.stock(), item.parent().id());
    }

    public Item DTOToItem(final ItemDTO itemDTO) {
        if (itemDTO == null)
            return new Item();

        return new Item(itemDTO.id(), itemDTO.type(), itemDTO.name(), itemDTO.description(), itemDTO.keywords(),
                itemDTO.price(), itemDTO.discount(), itemDTO.stock(), new User(itemDTO.parent()));
    }

    public List<ItemDTO> ItemsToDTO(final Collection<Item> items) {
        if (items == null)
            return new ArrayList<>();

        return items.stream().map(this::ItemToDTO).toList();
    }
}
