package org.lacabra.store.server.api.type.item;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.user.User;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@PersistenceCapable(table = "item")
public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private ObjectId id;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Persistent
    private ItemType type;

    @Persistent
    private String name;
    @Persistent
    private String description;
    @Persistent
    private Set<String> keywords;

    @Column(jdbcType = "REAL")
    private BigDecimal price;
    private int discount;
    @Column(jdbcType = "BIGINT")
    private BigInteger stock;

    @Column(name = "parent_id")
    private User parent;
}
