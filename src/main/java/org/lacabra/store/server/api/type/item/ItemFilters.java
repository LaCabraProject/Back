package org.lacabra.store.server.api.type.item;

import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public record ItemFilters(ObjectId id, ItemType type, String name, String description, HashSet<String> keywords,
                          BigDecimal[] price, Integer[] discount, UserId parent) implements Serializable {
    public ItemFilters id(final ObjectId id) {
        return new ItemFilters(id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                this.parent);
    }

    public ItemFilters type(final ItemType type) {
        return new ItemFilters(this.id, type, this.name, this.description, this.keywords, this.price, this.discount,
                this.parent);
    }

    public ItemFilters name(final String name) {
        return new ItemFilters(this.id, this.type, name, this.description, this.keywords, this.price,
                this.discount, this.parent);
    }

    public ItemFilters description(final String description) {
        return new ItemFilters(this.id, this.type, this.name, description, this.keywords, this.price,
                this.discount, this.parent);
    }

    public ItemFilters keywords(final Collection<String> keywords) {
        return new ItemFilters(this.id, this.type, this.name, this.description, new HashSet<>(keywords), this.price,
                this.discount, this.parent);
    }

    public ItemFilters price(final BigDecimal[] price) {
        return new ItemFilters(this.id, this.type, this.name, this.description, this.keywords, price,
                this.discount, this.parent);
    }

    public ItemFilters discount(final Integer[] discount) {
        return new ItemFilters(this.id, this.type, this.name, this.description, this.keywords, this.price,
                discount, this.parent);
    }

    public ItemFilters parent(final UserId parent) {
        return new ItemFilters(this.id, this.type, this.name, this.description, this.keywords, this.price,
                this.discount, parent);
    }

    public Map<String, String[]> toSearchParams() {
        final var m = new HashMap<String, String[]>();

        if (this.id != null)
            m.put("id", new String[]{this.id.toString()});

        if (this.name != null)
            m.put("name", new String[]{this.name});

        if (this.description != null)
            m.put("description", new String[]{this.name});

        if (this.keywords != null)
            m.put("keywords", this.keywords().toArray(String[]::new));

        if (this.price != null)
            m.put("price", Arrays.stream(this.price).map(BigDecimal::toString).toArray(String[]::new));

        if (this.discount != null)
            m.put("discount", Arrays.stream(this.discount).map(Object::toString).toArray(String[]::new));

        if (this.parent != null)
            if (this.id != null)
                m.put("parent", new String[]{this.parent.toString()});

        if (m.entrySet().isEmpty())
            return null;

        return m;
    }
}
