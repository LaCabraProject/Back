package org.lacabra.store.client.dto;


import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ItemDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private ObjectId id;
    private ItemType type;
    private String name;
    private String description;
    private Set<String> keywords;
    private BigDecimal price;
    private int discount;
    private BigInteger stock;
    private UserId parent;

    public ItemDTO() {
    }
    public ItemDTO(ObjectId id) {
        this(id, null, null, null, null, null, null, null, null);
    }

    public ItemDTO(ItemType type, String name, String description, Collection<String> keywords,
                Number price, Integer discount, BigInteger stock, UserId parent) {
        this(ObjectId.random(Item.class), type, name, description, keywords, price, discount, stock, parent);
    }

    public ItemDTO(ObjectId id, ItemType type, String name, String description, Collection<String> keywords,
                Number price, Integer discount, Number stock, UserId parent) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.keywords = keywords == null ? Collections.EMPTY_SET : new HashSet<>(keywords);
        this.price = new BigDecimal(String.valueOf(price));
        this.discount = discount;
        this.stock = new BigDecimal(String.valueOf(stock)).toBigInteger();
    }

    public ObjectId id() {
        return this.id;
    }

    public ItemType type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public Set<String> keywords() {
        return this.keywords;
    }

    public BigDecimal price() {
        return this.price;
    }

    public BigInteger stock() {
        return this.stock;
    }
    public Integer discount() {
        return this.discount;
    }

    public UserId parent() {
        return this.parent;
    }
}

