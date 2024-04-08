package org.lacabra.store.server.api.route.stats;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.annotation.security.PermitAll;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.lacabra.store.server.api.type.stats.ItemStats;
import org.lacabra.store.server.api.type.stats.Stats;
import org.lacabra.store.server.api.type.stats.UserStats;
import org.lacabra.store.server.json.serializer.BigIntegerSerializer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;

@Path("stats")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@PermitAll
public final class Route {
    @GET
    public static Stats GET() {
        return new Stats(Items.GET(), Users.GET());
    }

    @Path("stats/items")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public final static class Items {
        @GET
        public static ItemStats GET() {
            return new ItemStats();
        }

        @Path("stats/items/total")
        @PermitAll
        public final static class Total {
            @GET
            @JsonSerialize(using = BigIntegerSerializer.class)
            public static BigInteger GET() {
                return ItemStats.total();
            }
        }
    }

    @Path("stats/users")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public final static class Users {
        @GET
        public static UserStats GET() {
            return UserStats.get();
        }

        @Path("stats/users/total")
        @PermitAll
        public final static class Total {
            @GET
            @JsonSerialize(using = BigIntegerSerializer.class)
            public static BigInteger GET() {
                return UserStats.total();
            }
        }
    }
}
