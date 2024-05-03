package org.lacabra.store.server.api.type.stats;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lacabra.store.server.jdo.dao.UserDAO;

import java.math.BigInteger;

public final class UserStats {
    @JsonProperty("clients")
    public final BigInteger clients;

    @JsonProperty("artists")
    public final BigInteger artists;

    @JsonProperty("admins")
    public final BigInteger admins;

    @JsonProperty("total")
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

        return dao.count(dao.getQuery("CountArtists"));
    }

    public static BigInteger admins() {
        UserDAO dao = UserDAO.getInstance();

        return dao.count(dao.getQuery("CountAdmins"));
    }

    public static BigInteger total() {
        return UserDAO.getInstance().count();
    }

    public static UserStats get() {
        return new UserStats(UserStats.clients(), UserStats.artists(), UserStats.admins(), UserStats.total());
    }
}
