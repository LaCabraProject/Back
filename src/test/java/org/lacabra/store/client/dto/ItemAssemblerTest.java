package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;
import org.lacabra.store.server.api.type.user.User;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.mock;

public class ItemAssemblerTest {

    ItemAssembler assembler;
    @Mock
    Item item;
    @Mock
    List<Item> items;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        assembler = ItemAssembler.getInstance();
    }

    @Test
    public void testItemToDTO() {
        ItemDTO dto = assembler.ItemToDTO(item);
        List<ItemDTO> dtos = assembler.ItemsToDTO(items);
    }
}
