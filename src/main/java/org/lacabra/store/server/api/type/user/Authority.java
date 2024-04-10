package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum Authority implements Serializable {
    Client(Constants.Client),
    Artist(Constants.Artist),
    Admin(Constants.Admin);

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    public static Authority parse(String authority) {
        return switch (authority) {
            case Constants.Client -> Client;
            case Constants.Artist -> Artist;
            case Constants.Admin -> Admin;

            case null, default -> null;
        };
    }

    @Override
    @JsonValue
    public String toString() {
        return this.authority;
    }

    private static final class Constants {
        public static final String Client = "client";
        public static final String Artist = "artist";
        public static final String Admin = "admin";
    }
}