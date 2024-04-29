package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.server.api.type.item.Item;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(dto.id(), item.id());
        assertEquals(dto.name(), item.name());
        assertEquals(dto.description(), item.description());
        assertEquals(dto.price(), item.price());
        assertSame(dto.discount(), item.discount());
        assertEquals(dto.stock(), item.stock());
    }
}
