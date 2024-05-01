package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.ItemType;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemDTOTest {
    ItemDTO itemDTO1;
    ItemDTO itemDTO2;
    ItemDTO itemDTO3;

    @Mock
    private ObjectId id;
    @Mock
    private UserId userId;
    @Mock
    private ItemType type;
    @Mock
    private UserId parent;
    private String name;
    private String description;
    @Mock
    private Set<String> keywords;
    @Spy
    private BigDecimal price = BigDecimal.ZERO;
    private Integer discount = 0;
    @Spy
    private BigInteger stock = BigInteger.ZERO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(keywords.stream()).thenAnswer(x -> Stream.empty());

        itemDTO1 = new ItemDTO(id, type, name, description, keywords, price, discount, stock, parent);

        itemDTO2 = new ItemDTO(type, name, description, keywords, price, discount, stock, userId);
        itemDTO2 = itemDTO2.parent(userId);

        itemDTO3 = new ItemDTO(id, type, name, description, keywords, price, discount, stock, userId);
    }

    @Test
    public void getId() {
        assertEquals(itemDTO1.id(), id);
    }

    @Test
    public void getType() {
        assertEquals(itemDTO1.type(), type);
    }

    @Test
    public void getParent() {
        assertEquals(itemDTO1.parent(), parent);
    }

    @Test
    public void getName() {
        assertEquals(itemDTO1.name(), name);
    }

    @Test
    public void getDescription() {
        assertEquals(itemDTO1.description(), description);
    }

    @Test
    public void getKeywords() {
        assertTrue(itemDTO1.keywords().isEmpty());
    }

    @Test
    public void getPrice() {
        assertEquals(itemDTO1.price(), BigDecimal.ZERO);
    }

    @Test
    public void getDiscount() {
        assertSame(discount, itemDTO1.discount());
    }

    @Test
    public void getStock() {
        assertEquals(stock.subtract(itemDTO1.stock()).intValue(), 0);
    }
}
