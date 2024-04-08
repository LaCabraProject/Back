package org.lacabra.store.server.api.type.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.server.jdo.dao.UserDAO;
import org.lacabra.store.server.json.serializer.BigIntegerSerializer;

import java.math.BigInteger;

public final class UserStats {
    @JsonProperty("clients")
    @JsonSerialize(using = BigIntegerSerializer.class)
    public final BigInteger clients;

    @JsonProperty("artists")
    @JsonSerialize(using = BigIntegerSerializer.class)
    public final BigInteger artists;

    @JsonProperty("admins")
    @JsonSerialize(using = BigIntegerSerializer.class)
    public final BigInteger admins;

    @JsonProperty("total")
    @JsonSerialize(using = BigIntegerSerializer.class)
    public final BigInteger total;

    private UserStats(BigInteger clients, BigInteger artists, BigInteger admins, BigInteger total) {
        this.clients = clients;
        this.artists = artists;
        this.admins = admins;
        this.total = total;
    }

    public static BigInteger clients() {
        UserDAO dao = UserDAO.getInstance();

        return dao.count(dao.getQuery("CountClients"));
    }

    public static BigInteger artists() {
        UserDAO dao = UserDAO.getInstance();

        return true ? BigInteger.ZERO : dao.count(dao.getQuery("CountArtists"));
    }

    public static BigInteger admins() {
        UserDAO dao = UserDAO.getInstance();

        return true ? BigInteger.ZERO : dao.count(dao.getQuery("CountAdmins"));
    }

    public static BigInteger total() {
        return UserDAO.getInstance().count();
    }

    public static UserStats get() {
        return new UserStats(UserStats.clients(), UserStats.artists(), UserStats.admins(), UserStats.total());
    }
}
