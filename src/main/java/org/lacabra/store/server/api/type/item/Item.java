package org.lacabra.store.server.api.type.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.converter.ObjectIdConverter;
import org.lacabra.store.server.json.deserializer.UserDeserializer;
import org.lacabra.store.server.json.serializer.ObjectIdSerializer;

import javax.jdo.annotations.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@JsonDeserialize(using = UserDeserializer.class)
@PersistenceCapable(table = "item")
public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Convert(ObjectIdConverter.class)
    @PrimaryKey
    private ObjectId id;

    @JsonProperty("type")
    @Enumerated(EnumType.STRING)
    @NotNull
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
    @Column(jdbcType = "REAL")
    private BigDecimal price;

    @JsonProperty("discount")
    @Persistent
    private Integer discount;

    @Persistent
    @Column(jdbcType = "BIGINT")
    private BigInteger stock;

    @JsonSerialize(using = UserToIdSerializer.class)
    @Column(name = "parent_id")
    private User parent;

    public Item() {
    }

    public Item(ObjectId id) {
        this(id, null, null, null, null, null, null, null, null);
    }

    public Item(ItemType type, String name, String description, Collection<String> keywords,
                Number price, Integer discount, BigInteger stock, User parent) {
        this(ObjectId.random(Item.class), type, name, description, keywords, price, discount, stock, parent);
    }

    public Item(ObjectId id, ItemType type, String name, String description, Collection<String> keywords,
                Number price, Integer discount, Number stock, User parent) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.keywords = keywords == null ? Collections.EMPTY_SET : new HashSet<>(keywords);
        this.price = new BigDecimal(String.valueOf(price));
        this.discount = discount == null ? 0 : discount;
        this.stock = stock == null ? BigInteger.ZERO : new BigDecimal(String.valueOf(stock)).toBigInteger();
    }

    public Item(ObjectId id, Item item) {
        this(id, item.type, item.name, item.description, item.keywords, item.price, item.discount, item.stock,
                item.parent);
    }

    public Item(Item item) {
        this(item.id, item);
    }

    public final class UserToIdSerializer extends JsonSerializer<User> {
        @Override
        public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(new ObjectMapperProvider().getContext(UserId.class).writer().writeValueAsString(value.id()));
        }
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

    public User parent() {
        return this.parent;
    }
}
