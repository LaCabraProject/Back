package org.lacabra.store.server.api.type.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Generated;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.json.deserializer.ItemDeserializer;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.json.serializer.ObjectIdSerializer;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.DTOable;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.converter.ItemTypeConverter;
import org.lacabra.store.server.jdo.converter.ObjectIdConverter;
import org.lacabra.store.server.jdo.dao.Mergeable;
import org.lacabra.store.server.jdo.dao.UserDAO;

import javax.jdo.annotations.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.*;
import java.util.stream.Collectors;

@JsonDeserialize(using = ItemDeserializer.Persistent.class)
@JsonSerialize
@PersistenceCapable(table = "item")
@ForeignKey(name = "parent", members = "id", columns = {@Column(name = "parent", defaultValue = "#NULL", allowsNull =
        "true")}, unique = "false", deleteAction = ForeignKeyAction.NONE, updateAction = ForeignKeyAction.NONE)
@Query(name = "FindItem", value = "SELECT FROM Item WHERE id == :id")
@Query(name = "FindByParent", language = "javax.jdo.query.SQL", value = "SELECT * FROM ITEM WHERE parent = :parent")
public class Item implements Serializable, Mergeable<Item>, DTOable<Item, ItemDTO> {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Convert(value = ObjectIdConverter.class)
    @PrimaryKey
    @Column(name = "id", jdbcType = "VARCHAR", sqlType = "VARCHAR", length = 24)
    @Persistent(useDefaultConversion = true)
    private ObjectId id;

    @JsonProperty("type")
    @Convert(ItemTypeConverter.class)
    @Persistent
    private ItemType type;

    @JsonProperty("name")
    @Persistent
    private String name;

    @JsonProperty("description")
    @Persistent
    private String description;

    @JsonProperty("keywords")
    @Persistent
    private HashSet<String> keywords;

    @Persistent
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("discount")
    @Persistent
    private Integer discount;

    @Persistent
    @JsonProperty("stock")
    private BigInteger stock;

    @JsonSerialize(using = UserToIdSerializer.class)
    @JsonProperty("parent")
    @Persistent
    @Column(name = "parent")
    private User parent;

    @Generated
    public Item() {
    }

    @Generated
    public Item(final String id) {
        this(ObjectId.from(id));
    }

    @Generated
    public Item(final Number id) {
        this(ObjectId.from(id));
    }

    @Generated
    public Item(final ObjectId id) {
        this(id, null, null, null, null, null, null, null, (String) null);
    }

    @Generated
    public Item(final ItemType type, final String name, final String description, final Collection<String> keywords,
                final Number price, final Number discount, final Number stock, final String parent) {
        this(type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    @Generated
    public Item(final ItemType type, final String name, final String description, final Collection<String> keywords,
                final Number price, final Number discount, final Number stock, final UserId parent) {
        this(type, name, description, keywords, price, discount, stock, new User(parent));
    }

    @Generated
    public Item(final ItemType type, final String name, final String description, final Collection<String> keywords,
                final Number price, final Number discount, final Number stock, final User parent) {
        this(ObjectId.random(Item.class), type, name, description, keywords, price, discount, stock, parent);
    }

    @Generated
    public Item(final String id, final ItemType type, final String name, final String description,
                final Collection<String> keywords, final Number price, final Number discount, final Number stock
            , final String parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    @Generated
    public Item(final Number id, final ItemType type, final String name, final String description,
                final Collection<String> keywords, final Number price, final Number discount, final Number stock
            , final String parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    @Generated
    public Item(final Number id, final ItemType type, final String name, final String description,
                final Collection<String> keywords, final Number price, final Number discount, final Number stock
            , final UserId parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, new User(parent));
    }

    @Generated
    public Item(final ObjectId id, final ItemType type, final String name, final String description,
                final Collection<String> keywords, final Number price, final Number discount, final Number stock
            , final String parent) {
        this(id, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    @Generated
    public Item(final ObjectId id, final ItemType type, final String name, final String description,
                final Collection<String> keywords, final Number price, final Number discount, final Number stock
            , final UserId parent) {
        this(id, type, name, description, keywords, price, discount, stock, new User(parent));
    }

    public Item(final ObjectId id, final ItemType type, final String name, final String description,
                final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                final User parent) {
        super();

        this.id(id);
        this.type(type);
        this.name(name);
        this.description(description);
        this.keywords(keywords);
        this.price(price);
        this.discount(discount);
        this.stock(stock);
        this.parent(parent);
    }

    @Generated
    public Item(final ObjectId id, final Item item) {
        this(id, item.type, item.name, item.description, item.keywords, item.price, item.discount, item.stock,
                item.parent);
    }

    public Item(final Item item) {
        this(item.id, item);
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

    public HashSet<String> keywords() {
        return this.keywords == null ? new HashSet<>() : new HashSet<>(this.keywords);
    }

    public BigDecimal price() {
        return this.price == null ? BigDecimal.ZERO : this.price;
    }

    public BigInteger stock() {
        return this.stock == null ? BigInteger.ZERO : this.stock;
    }

    public Integer discount() {
        return this.discount == null ? null : this.discount;
    }

    public User parent() {
        return this.parent;
    }

    private void id(final String id) {
        this.id(ObjectId.from(id));
    }

    private void id(final Number id) {
        this.id(ObjectId.from(id));
    }

    private void id(final ObjectId id) {
        this.id = id;
    }

    private void type(final ItemType type) {
        this.type = type;
    }

    private void name(final String name) {
        this.name = name;
    }

    private void description(final String description) {
        this.description = description;
    }

    private void keywords(final Collection<String> keywords) {
        this.keywords = keywords == null ? null :
                keywords.stream().filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));
    }

    private void price(final Number price) {
        if (price == null)
            this.price = null;

        else
            this.price = new BigDecimal(price.toString()).max(BigDecimal.ZERO);
    }

    private void stock(final Number stock) {
        this.stock = (switch (stock) {
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
        });

        if (this.stock != null)
            this.stock = this.stock.max(BigInteger.ZERO);
    }

    private void discount(final Number discount) {
        this.discount = switch (discount) {
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
        };

        if (this.discount != null)
            this.discount = Math.max(0, Math.min(100, this.discount));
    }

    private void parent(final String parent) {
        this.parent(UserId.from(parent));
    }

    private void parent(final UserId parent) {
        this.parent(new User(parent));
    }

    private void parent(final User parent) {
        this.parent = parent;
    }

    @Override
    public Item merge(final Item override) {
        UserDAO.getInstance();

        if (override == null) {
            Mergeable.super.merge(this);

            return this;
        }

        if (override.id != null) this.id(override.id);

        if (override.type != null) this.type(override.type);

        if (override.name != null) this.name(override.name);

        if (override.description != null) this.description(override.description);

        if (override.keywords != null) this.keywords(override.keywords);

        if (override.price != null) this.price(override.price);

        if (override.stock != null) this.stock(override.stock);

        if (override.discount != null) this.discount(override.discount);

        if (override.parent != null) this.parent(override.parent);

        Mergeable.super.merge(this);

        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(Item.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemDTO toDTO() {
        return null;
    }

    public static Item fromDTO(final ItemDTO dto) {
        if (dto == null) return null;

        return new Item(dto.id(), dto.type(), dto.name(), dto.description(), dto.keywords(), dto.price(),
                dto.discount(), dto.stock(), new User(dto.parent()));
    }

    public static final class UserToIdSerializer extends JsonSerializer<User> {
        @Override
        public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value == null || value.id() == null) {
                jgen.writeNull();

                return;
            }

            jgen.writeString(new ObjectMapperProvider().getContext(UserId.class).writer().writeValueAsString(value.id()).replaceAll("^\"(.*)\"$", "$1"));
        }
    }
}
