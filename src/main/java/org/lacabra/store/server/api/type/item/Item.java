package org.lacabra.store.server.api.type.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.converter.BigIntegerConverter;
import org.lacabra.store.server.jdo.converter.ItemTypeConverter;
import org.lacabra.store.server.jdo.converter.ObjectIdConverter;
import org.lacabra.store.server.jdo.dao.Mergeable;
import org.lacabra.store.internals.json.deserializer.ItemDeserializer;
import org.lacabra.store.internals.json.serializer.BigIntegerSerializer;
import org.lacabra.store.internals.json.serializer.ObjectIdSerializer;

import javax.jdo.annotations.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@JsonDeserialize(using = ItemDeserializer.class)
@PersistenceCapable(table = "item")
@ForeignKey(name = "parent", members = "id", columns = {@Column(name = "parent", defaultValue = "#NULL",
        allowsNull = "true")},
        unique = "false", deleteAction =
        ForeignKeyAction.NONE, updateAction = ForeignKeyAction.NONE)
@Query(name = "FindItem", value = "SELECT FROM Item WHERE id == :id")
@Query(name = "FindByParent", language = "javax.jdo.query.SQL", value = "SELECT * FROM ITEM WHERE parent = :parent")
public class Item implements Serializable, Mergeable<Item> {
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
    private Set<String> keywords;

    @JsonProperty("price")
    @Column(jdbcType = "DECIMAL", defaultValue = "0")
    private BigDecimal price;

    @JsonProperty("discount")
    @Column(jdbcType = "INTEGER", defaultValue = "0")
    @Persistent
    private Integer discount;

    @Persistent
    @Column(jdbcType = "BIGINT", defaultValue = "0")
    @Convert(BigIntegerConverter.class)
    @JsonProperty("stock")
    @JsonSerialize(using = BigIntegerSerializer.class)
    private BigInteger stock;

    @JsonSerialize(using = UserToIdSerializer.class)
    @JsonProperty("parent")
    @Persistent
    @Column(name = "parent")
    private User parent;

    public Item() {
    }

    public Item(ObjectId id) {
        this(id, null, null, null, null, null, null, null, null);
    }

    public Item(ItemType type, String name, String description, Collection<String> keywords, Number price,
                Integer discount, BigInteger stock, User parent) {
        this(ObjectId.random(Item.class), type, name, description, keywords, price, discount, stock, parent);
    }

    public Item(ObjectId id, ItemType type, String name, String description, Collection<String> keywords,
                Number price, Integer discount, Number stock, User parent) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.keywords = keywords == null ? null : new HashSet<>(keywords);
        this.price = price == null ? null : new BigDecimal(String.valueOf(price));
        this.discount = discount;
        this.stock = stock == null ? null : new BigDecimal(String.valueOf(stock)).toBigInteger();
        this.parent = parent;
    }

    public Item(ObjectId id, Item item) {
        this(id, item.type, item.name, item.description, item.keywords, item.price, item.discount, item.stock,
                item.parent);
    }

    public Item(Item item) {
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

    public Set<String> keywords() {
        return this.keywords == null ? null : new HashSet<>(this.keywords);
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

    public User parent() {
        return this.parent;
    }

    private void setId(ObjectId id) {
        this.id = id;
    }

    private void setType(ItemType type) {
        this.type = type;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    private void setPrice(BigDecimal price) {
        this.price = price;
    }

    private void setStock(BigInteger stock) {
        this.stock = stock;
    }

    private void setDiscount(Integer discount) {
        this.discount = discount;
    }

    private void setParent(User parent) {
        this.parent = parent;
    }

    @Override
    public Item merge(Item override) {
        if (override == null) return this;

        if (override.id != null)
            this.setId(override.id);

        if (override.type != null) this.setType(override.type);

        if (override.name != null) this.setName(override.name);

        if (override.description != null) this.setDescription(override.description);

        if (override.keywords != null) this.setKeywords(override.keywords);

        if (override.price != null) this.setPrice(override.price);

        if (override.stock != null) this.setStock(override.stock);

        if (override.discount != null) this.setDiscount(override.discount);

        if (override.parent != null) this.setParent(override.parent);

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
