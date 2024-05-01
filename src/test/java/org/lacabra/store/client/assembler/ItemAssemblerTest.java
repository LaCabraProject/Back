package org.lacabra.store.client.assembler;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.server.api.type.item.Item;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

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
