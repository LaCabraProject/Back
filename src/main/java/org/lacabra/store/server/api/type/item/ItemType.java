package org.lacabra.store.server.api.type.item;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ItemType implements Serializable {
    Clothing(Constants.Clothing),
    Accessories(Constants.Accessories),
    Decoration(Constants.Decoration),
    Utilities(Constants.Utilities);

    private final String type;

    ItemType(String type) {
        this.type = type;
    }

    public static ItemType parse(String type) {
        return switch (type) {
            case Constants.Clothing -> Clothing;
            case Constants.Accessories -> Accessories;
            case Constants.Decoration -> Decoration;
            case Constants.Utilities -> Utilities;

            case null, default -> null;
        };
    }

    @Override
    @JsonValue
    public String toString() {
        return this.type;
    }

    private static final class Constants {
        public static final String Clothing = "clothing";
        public static final String Accessories = "accessories";
        public static final String Decoration = "decoration";
        public static final String Utilities = "Utilities";
    }
}