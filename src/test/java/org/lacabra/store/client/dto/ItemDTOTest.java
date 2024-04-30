package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.item.ItemType;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemDTOTest {

    ItemDTO itemDTO;
    @Mock
    private ObjectId id;
    @Mock
    private ItemType type;
    @Mock
    private UserId parent;
    private String name;
    private String description;
    private Set<String> keywords;
    private BigDecimal price;
    private int discount;
    private BigInteger stock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        itemDTO = new ItemDTO();
        itemDTO.setId(id);
        itemDTO.setType(type);
        itemDTO.setParent(parent);
        itemDTO.setName(name);
        itemDTO.setDescription(description);
        itemDTO.setKeywords(keywords);
        itemDTO.setPrice(price);
        itemDTO.setDiscount(discount);
        itemDTO.setStock(stock);
    }
    @Test
    public void getId() {
        assertEquals(id, itemDTO.id());
    }
    @Test
    public void getType() {
        assertEquals(type, itemDTO.type());
    }
    @Test
    public void getParent() {
        assertEquals(parent, itemDTO.parent());
    }
    @Test
    public void getName() {
        assertEquals(name, itemDTO.name());
    }
    @Test
    public void getDescription() {
        assertEquals(description, itemDTO.description());
    }
    @Test
    public void getKeywords() {
        assertEquals(keywords, itemDTO.keywords());
    }
    @Test
    public void getPrice() {
        assertEquals(price, itemDTO.price());
    }
    @Test
    public void getDiscount() {
        assertTrue(discount == itemDTO.discount());
    }
    @Test
    public void getStock() {
        assertEquals(stock, itemDTO.stock());
    }
}
