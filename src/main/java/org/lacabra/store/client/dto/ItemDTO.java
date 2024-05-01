package org.lacabra.store.client.dto;


import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
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
    private Integer discount;
    private BigInteger stock;
    private UserId parent;

    public ItemDTO() {
    }

    public ItemDTO(final ObjectId id) {
        this(id, null, null, null, null, null, null, null, null);
    }

    public ItemDTO(final ItemType type, final String name, final String description, final Collection<String> keywords,
                   final Number price, final Integer discount, final BigInteger stock, final UserId parent) {
        this(ObjectId.random(Item.class), type, name, description, keywords, price, discount, stock, parent);
    }

    @SuppressWarnings("unchecked")
    public ItemDTO(final ObjectId id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords,
                   final Number price, final Integer discount, final Number stock, final UserId parent) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.keywords = keywords == null ? Collections.EMPTY_SET : new HashSet<>(keywords);
        this.price = new BigDecimal(String.valueOf(price));
        this.discount = discount == null ? 0 : discount;
        this.stock = new BigDecimal(String.valueOf(stock)).toBigInteger();
        this.parent = parent;
    }

    public ObjectId id() {
        return this.id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public ItemType type() {
        return this.type;
    }

    public void setType(final ItemType type) {
        this.type = type;
    }

    public String name() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String description() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<String> keywords() {
        return this.keywords;
    }

    public void setKeywords(final Set<String> keywords) {
        this.keywords = keywords;
    }

    public BigDecimal price() {
        return this.price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public BigInteger stock() {
        return this.stock;
    }

    public void setStock(final BigInteger stock) {
        this.stock = stock;
    }

    public Integer discount() {
        return this.discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public UserId parent() {
        return this.parent;
    }

    public void setParent(final UserId parent) {
        this.parent = parent;
    }
}

