package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.ItemType;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.junit.Assert.*;

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
    private Set<String> keywords;
    private BigDecimal price;
    private Integer discount;
    private BigInteger stock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        itemDTO1 = new ItemDTO();
        itemDTO2 = new ItemDTO(type, name, description, keywords, price, discount, stock, userId);
        itemDTO3 = new ItemDTO(id, type, name, description, keywords, price, discount, stock, userId);
        itemDTO1.setId(id);
        itemDTO1.setType(type);
        itemDTO1.setParent(parent);
        itemDTO1.setName(name);
        itemDTO1.setDescription(description);
        itemDTO1.setKeywords(keywords);
        itemDTO1.setPrice(price);
        itemDTO1.setDiscount(discount);
        itemDTO1.setStock(stock);
        itemDTO2.setParent(userId);
    }

    @Test
    public void getId() {
        assertEquals(id, itemDTO1.id());
    }

    @Test
    public void getType() {
        assertEquals(type, itemDTO1.type());
    }

    @Test
    public void getParent() {
        assertEquals(parent, itemDTO1.parent());
    }

    @Test
    public void getName() {
        assertEquals(name, itemDTO1.name());
    }

    @Test
    public void getDescription() {
        assertEquals(description, itemDTO1.description());
    }

    @Test
    public void getKeywords() {
        assertEquals(keywords, itemDTO1.keywords());
    }

    @Test
    public void getPrice() {
        assertEquals(price, itemDTO1.price());
    }

    @Test
    public void getDiscount() {
        assertSame(discount, itemDTO1.discount());
    }

    @Test
    public void getStock() {
        assertEquals(stock, itemDTO1.stock());
    }
}
