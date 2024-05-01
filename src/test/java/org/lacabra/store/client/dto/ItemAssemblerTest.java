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
    Item item;
    @Mock
    ObjectId id;
    @Mock
    ItemType type;

    User user;

    List mockedList = mock(List.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        assembler = ItemAssembler.getInstance();
    }

    @Test
    public void testItemToDTO() {
        //ItemDTO dto = assembler.ItemToDTO(item);
        //assertEquals(dto.id(), item.id());
        //assertEquals(dto.name(), item.name());
        //assertEquals(dto.description(), item.description());
        //assertEquals(dto.price(), item.price());
        //assertSame(dto.discount(), item.discount());
        //assertEquals(dto.stock(), item.stock());
    }
}
