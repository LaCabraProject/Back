package org.lacabra.store.client.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;


    private ObjectId id;
    private ItemType type;
    private String name;
    private String description;
    private Set<String> keywords;
    private BigDecimal price;
    private int discount;
    private BigInteger stock;
    private User parent;
}
