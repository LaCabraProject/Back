/**
 * @file ItemDTO.java
 * @brief Definición de la clase ItemDTO que representa un elemento de tipo "item" en la aplicación cliente.
 */

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

/**
 * @class ItemDTO
 * @brief Esta clase representa un elemento de tipo "item" en la aplicación cliente.
 * Proporciona métodos para acceder y manipular los atributos del item.
 */

@JsonDeserialize(using = ItemDeserializer.DTO.class)
public record ItemDTO(@JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
                      @JsonProperty("type") ItemType type, @JsonProperty("name") String name,
                      @JsonProperty("description") String description,
                      @JsonProperty("keywords") HashSet<String> keywords, @JsonProperty("price") BigDecimal price,
                      @JsonProperty("discount") Integer discount, @JsonProperty("stock") BigInteger stock,
                      @JsonProperty("parent") UserId parent) implements Serializable, DTO<ItemDTO, Item> {

    /** @brief Número de versión para la serialización. */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @brief Constructor vacío de la clase ItemDTO.
     */

    public ItemDTO() {
        this((ObjectId) null);
    }

    /**
     * @brief Constructor que acepta un ID numérico.
     *
     * @param id Identificador numérico del item.
     */

    public ItemDTO(final Number id) {
        this(ObjectId.from(id));
    }

    /**
     * @brief Constructor que acepta un ID en forma de cadena.
     *
     * @param id Identificador en forma de cadena del item.
     */

    public ItemDTO(final String id) {
        this(ObjectId.from(id));
    }

    /**
     * @brief Constructor que acepta un ObjectId.
     *
     * @param id Identificador único del item.
     */

    public ItemDTO(final ObjectId id) {
        this(id, null, null, null, null, null, null, null, (UserId) null);
    }

    /**
     * @brief Constructor que acepta todos los atributos del item.
     *
     * @param id           Identificador único del item.
     * @param type         Tipo de item.
     * @param name         Nombre del item.
     * @param description  Descripción del item.
     * @param keywords     Palabras clave asociadas al item.
     * @param price        Precio del item.
     * @param discount     Descuento aplicado al item.
     * @param stock        Stock disponible del item.
     * @param parent       Identificador del padre del item.
     */

    public ItemDTO(final String id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, parent);
    }

    /**
     * @brief Constructor que acepta un ID numérico y otros atributos.
     *
     * @param id          Identificador numérico del item.
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item.
     */
    public ItemDTO(final Number id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, parent);
    }

    /**
     * @brief Constructor que acepta un ID en forma de cadena y otros atributos.
     *
     * @param id          Identificador en forma de cadena del item.
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item en forma de cadena.
     */
    public ItemDTO(final String id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    /**
     * @brief Constructor que acepta un ID numérico en forma de cadena y otros atributos.
     *
     * @param id          Identificador numérico en forma de cadena del item.
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item en forma de cadena.
     */
    public ItemDTO(final Number id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this(ObjectId.from(id), type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    /**
     * @brief Constructor que acepta un ObjectId y otros atributos.
     *
     * @param id          Identificador único del item.
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item en forma de cadena.
     */
    public ItemDTO(final ObjectId id, final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this(id, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    /**
     * @brief Constructor que acepta otros atributos excepto el ID.
     *
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item en forma de cadena.
     */
    public ItemDTO(final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final String parent) {
        this((String) null, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }

    /**
     * @brief Constructor que acepta otros atributos excepto el ID.
     *
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item.
     */
    public ItemDTO(final ItemType type, final String name, final String description,
                   final Collection<String> keywords, final Number price, final Number discount, final Number stock,
                   final UserId parent) {
        this((String) null, type, name, description, keywords, price, discount, stock, UserId.from(parent));
    }


    /**
     * @brief Constructor que acepta un ObjectId y otros atributos, con manejo especial de nulos y conversiones.
     *
     * @param id          Identificador único del item.
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item.
     */
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

    /**
     * @brief Constructor que acepta un ObjectId y otros atributos directamente asignados.
     *
     * @param id          Identificador único del item.
     * @param type        Tipo de item.
     * @param name        Nombre del item.
     * @param description Descripción del item.
     * @param keywords    Palabras clave asociadas al item.
     * @param price       Precio del item.
     * @param discount    Descuento aplicado al item.
     * @param stock       Stock disponible del item.
     * @param parent      Identificador del padre del item.
     */
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


    /**
     * @brief Constructor que crea una copia de un objeto ItemDTO existente.
     *
     * @param item El ItemDTO existente del cual se creará la copia.
     */
    public ItemDTO(final ItemDTO item) {
        this(item == null ? null : item.id, item == null ? null : item.type, item == null ? null : item.name,
                item == null ? null : item.description, item == null ? null : item.keywords, item == null ? null :
                        item.price, item == null ? null : item.discount, item == null ? null : item.stock,
                item == null ? null : item.parent);
    }

    /**
     * @brief Establece el identificador único del item.
     *
     * @param id El nuevo identificador único del item.
     * @return Una nueva instancia de ItemDTO con el identificador actualizado.
     */
    public ItemDTO id(final String id) {
        return this.id(ObjectId.from(id));
    }

    /**
     * @brief Establece el identificador único del item.
     *
     * @param id El nuevo identificador único del item.
     * @return Una nueva instancia de ItemDTO con el identificador actualizado.
     */
    public ItemDTO id(final Number id) {
        return this.id(ObjectId.from(id));
    }

    /**
     * @brief Establece el identificador único del item.
     *
     * @param id El nuevo identificador único del item.
     * @return Una nueva instancia de ItemDTO con el identificador actualizado.
     */
    public ItemDTO id(final ObjectId id) {
        return new ItemDTO(id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    /**
     * @brief Establece el tipo del item.
     *
     * @param type El nuevo tipo del item.
     * @return Una nueva instancia de ItemDTO con el tipo actualizado.
     */
    public ItemDTO type(final ItemType type) {
        return new ItemDTO(this.id, type, this.name, this.description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    /**
     * @brief Establece el nombre del item.
     *
     * @param name El nuevo nombre del item.
     * @return Una nueva instancia de ItemDTO con el nombre actualizado.
     */
    public ItemDTO name(final String name) {
        return new ItemDTO(this.id, this.type, name, this.description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    /**
     * @brief Establece la descripción del item.
     *
     * @param description La nueva descripción del item.
     * @return Una nueva instancia de ItemDTO con la descripción actualizada.
     */
    public ItemDTO description(final String description) {
        return new ItemDTO(this.id, this.type, this.name, description, this.keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    /**
     * @brief Establece las palabras clave asociadas al item.
     *
     * @param keywords Las nuevas palabras clave asociadas al item.
     * @return Una nueva instancia de ItemDTO con las palabras clave actualizadas.
     */
    public ItemDTO keywords(final Collection<String> keywords) {
        return new ItemDTO(this.id, this.type, this.name, this.description, keywords, this.price, this.discount,
                this.stock, this.parent);
    }

    /**
     * @brief Establece el precio del item.
     *
     * @param price El nuevo precio del item.
     * @return Una nueva instancia de ItemDTO con el precio actualizado.
     */
    public ItemDTO price(final Number price) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, price, this.discount,
                this.stock, this.parent);
    }

    /**
     * @brief Establece el descuento aplicado al item.
     *
     * @param discount El nuevo descuento aplicado al item.
     * @return Una nueva instancia de ItemDTO con el descuento actualizado.
     */
    public ItemDTO discount(final Number discount) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, this.price, discount,
                this.stock, this.parent);
    }


    /**
     * @brief Establece el stock disponible del item.
     *
     * @param stock El nuevo stock disponible del item.
     * @return Una nueva instancia de ItemDTO con el stock actualizado.
     */
    public ItemDTO stock(final Number stock) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                stock, this.parent);
    }

    /**
     * @brief Establece el identificador del padre del item a partir de un string.
     *
     * @param id El identificador del padre del item como string.
     * @return Una nueva instancia de ItemDTO con el identificador del padre actualizado.
     */
    public ItemDTO parent(final String id) {
        return this.parent(UserId.from(id));
    }

    /**
     * @brief Establece el identificador del padre del item.
     *
     * @param parent El identificador del padre del item.
     * @return Una nueva instancia de ItemDTO con el identificador del padre actualizado.
     */
    public ItemDTO parent(final UserId parent) {
        return new ItemDTO(this.id, this.type, this.name, this.description, this.keywords, this.price, this.discount,
                this.stock, parent);
    }

    /**
     * @brief Devuelve una representación en formato JSON del objeto ItemDTO.
     *
     * @return Una cadena de caracteres que representa el objeto ItemDTO en formato JSON.
     * @throws RuntimeException Si ocurre un error al procesar la conversión a JSON.
     */
    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(ItemDTO.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @brief Convierte el objeto ItemDTO en un objeto persistente del tipo Item.
     *
     * @return Un objeto persistente del tipo Item.
     */
    @Override
    public Item toPersistent() {
        return Item.fromDTO(this);
    }

}

