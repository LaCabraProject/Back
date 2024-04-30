package org.lacabra.store.client.dto;

import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import java.util.ArrayList;
import java.util.List;

public class ItemAssembler {
    private static ItemAssembler instance;

    private ItemAssembler() {

    }

    public static ItemAssembler getInstance() {
        if (instance == null) {
            instance = new ItemAssembler();
        }
        return instance;
    }

    public ItemDTO ItemToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO(item.id(), item.type(), item.name(), item.description(), item.keywords(),
                item.price(), item.discount(), item.stock(), item.parent().id());
        return itemDTO;
    }
    public Item DTOToItem(ItemDTO itemDTO) {
        Item item = new Item( itemDTO.id(), itemDTO.type(), itemDTO.name(), itemDTO.description(), itemDTO.keywords(), itemDTO.price(), itemDTO.discount(), itemDTO.stock(), new User(itemDTO.parent()));
        return item;
    }
    public List<ItemDTO> ItemsToDTO(List<Item> items) {
        List<ItemDTO> dtos = new ArrayList<>();

        for (Item item : items) {
            dtos.add(this.ItemToDTO(item));
        }
        return dtos;
    }
}
