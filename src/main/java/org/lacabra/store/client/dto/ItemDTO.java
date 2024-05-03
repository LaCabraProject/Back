package org.lacabra.store.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.internals.json.deserializer.ItemDeserializer;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.json.serializer.ObjectIdSerializer;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.item.ItemType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.*;
import java.util.stream.Collectors;

@JsonDeserialize(using = ItemDeserializer.DTO.class)
public record ItemDTO(@JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
                      @JsonProperty("type") ItemType type, @JsonProperty("name") String name,
                      @JsonProperty("description") String description,
                      @JsonProperty("keywords") HashSet<String> keywords, @JsonProperty("price") BigDecimal price,
                      @JsonProperty("discount") Integer discount, @JsonProperty("stock") BigInteger stock,
                      @JsonProperty("parent") UserId parent) implements Serializable, DTO<ItemDTO, Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public ItemDTO() {
        this((ObjectId) null);
    }

    public ItemDTO(final Number id) {
        this(ObjectId.from(id));
    }

    public ItemDTO(final String id) {
        this(ObjectId.from(id));
    }

    public ItemDTO(final ObjectId id) {
        this(id, null, null, null, null, null, null, null, (UserId) null);
    }

    public ItemDTO(final String id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, parent);
    }

    public ItemDTO(final Number id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, parent);
    }

    public ItemDTO(final String id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    public ItemDTO(final Number id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    public ItemDTO(final ObjectId id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this(id, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    public ItemDTO(final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this((String) null, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    public ItemDTO(final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this((String) null, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    public ItemDTO(final ObjectId id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this(id, type, name, description, keywords == null ? null :
                        keywords.stream().filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new)),
                price == null ? null : new BigDecimal(price.toString()), switch (discount) {
                    case Byte b -> Integer.valueOf(b);
                    case Short s -> Integer.valueOf(s);
                    case Integer i -> Integer.valueOf(i);
                    case AtomicInteger ai -> ai.get();
                    case Long l -> Long.valueOf(Math.max(Integer.MIN_VALUE, Math.min(l, Integer.MAX_VALUE))).intValue();
                    case AtomicLong al ->
                            Long.valueOf(Math.max(Integer.MIN_VALUE, Math.min(al.get(), Integer.MAX_VALUE))).intValue();
                    case LongAccumulator la ->
                            Long.valueOf(Math.max(Integer.MIN_VALUE, Math.min(la.get(), Integer.MAX_VALUE))).intValue();
                    case LongAdder la ->
                            Long.valueOf(Math.max(Integer.MIN_VALUE, Math.min(la.sum(), Integer.MAX_VALUE))).intValue();
                    case BigInteger bi ->
                            bi.min(BigInteger.valueOf(Integer.MAX_VALUE)).max(BigInteger.valueOf(Integer.MIN_VALUE)).intValue();

                    case Float f ->
                            BigDecimal.valueOf((double) f).min(BigDecimal.valueOf(Integer.MAX_VALUE)).max(BigDecimal.valueOf(Integer.MIN_VALUE)).intValue();
                    case Double d ->
                            BigDecimal.valueOf(d).min(BigDecimal.valueOf(Integer.MAX_VALUE)).max(BigDecimal.valueOf(Integer.MIN_VALUE)).intValue();
                    case DoubleAccumulator da ->
                            BigDecimal.valueOf(da.get()).min(BigDecimal.valueOf(Integer.MAX_VALUE)).max(BigDecimal.valueOf(Integer.MIN_VALUE)).intValue();
                    case DoubleAdder da ->
                            BigDecimal.valueOf(da.sum()).min(BigDecimal.valueOf(Integer.MAX_VALUE)).max(BigDecimal.valueOf(Integer.MIN_VALUE)).intValue();

                    case BigDecimal dec ->
                            dec.min(BigDecimal.valueOf(Integer.MAX_VALUE)).max(BigDecimal.valueOf(Integer.MIN_VALUE)).intValue();

                    case null, default -> null;
                }, switch (stock) {
                    case Byte b -> BigInteger.valueOf(b);
                    case Short s -> BigInteger.valueOf(s);
                    case Integer i -> BigInteger.valueOf(i);
                    case AtomicInteger ai -> BigInteger.valueOf(ai.get());
                    case Long l -> BigInteger.valueOf(l);
                    case AtomicLong al -> BigInteger.valueOf(al.get());
                    case LongAccumulator la -> BigInteger.valueOf(la.get());
                    case LongAdder la -> BigInteger.valueOf(la.sum());
                    case BigInteger bi -> bi;

                    case Float f -> BigDecimal.valueOf((double) f).toBigInteger();
                    case Double d -> BigDecimal.valueOf(d).toBigInteger();
                    case DoubleAccumulator da -> BigDecimal.valueOf(da.get()).toBigInteger();
                    case DoubleAdder da -> BigDecimal.valueOf(da.sum()).toBigInteger();

                    case BigDecimal dec -> dec.toBigInteger();

                    case null, default -> null;
                }, parent);
    }

    public ItemDTO(final ObjectId id, final ItemType type, final String name, final String description,
                   final HashSet<String> keywords, final BigDecimal price, final Integer discount,
                   final BigInteger stock, final UserId parent) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.keywords = keywords == null ? new HashSet<>() : new HashSet<>(keywords);
        this.price = new BigDecimal(Objects.requireNonNullElse(price, 0).toString()).max(BigDecimal.ZERO);
        this.discount = Math.min(Math.max(Objects.requireNonNullElse(discount, 0), 0), 100);
        this.stock = Objects.requireNonNullElse(stock, BigInteger.ZERO).max(BigInteger.ZERO);
        this.parent = parent;
    }

    public ItemDTO(final ItemDTO item) {
        this(item == null ? null : item.id, item == null ? null : item.type, item == null ? null : item.name,
                item == null ? null : item.description, item == null ? null : item.keywords, item == null ? null :
                        item.price, item == null ? null : item.discount, item == null ? null : item.stock,
                item == null ? null : item.parent);
    }

    public ItemDTO id(final String id) {
        return this.id(ObjectId.from(id));
    }

    public ItemDTO id(final Number id) {
        return this.id(ObjectId.from(id));
    }

    public ItemDTO id(final ObjectId id) {
        return new ItemDTO(id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    public ItemDTO type(final ItemType type) {
        return new ItemDTO(this.id, type, this.name, this.description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    public ItemDTO name(final String name) {
        return new ItemDTO(this.id, this.type, name, this.description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    public ItemDTO description(final String description) {
        return new ItemDTO(this.id, this.type, this.name, description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    public ItemDTO keywords(final Collection<String> keywords) {
        return new ItemDTO(this.id, this.type, this.name, this.description, keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    public ItemDTO price(final Number price) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, price, this.discount,
                this.stock, this.parent);
    }

    public ItemDTO discount(final Number discount) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, this.price, discount,
                this.stock, this.parent);
    }

    public ItemDTO stock(final Number stock) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                stock, this.parent);
    }

    public ItemDTO parent(final String id) {
        return this.parent(UserId.from(id));
    }

    public ItemDTO parent(final UserId parent) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                this.stock, parent);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(ItemDTO.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item toPersistent() {
        return Item.fromDTO(this);
    }
}

