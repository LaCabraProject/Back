package org.lacabra.store.server.api.route.stats;

import org.lacabra.store.server.api.type.stats.ItemStats;
import org.lacabra.store.server.api.type.stats.Stats;
import org.lacabra.store.server.api.type.stats.UserStats;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public final class Route {
    @GET
    public static Stats GET() {
        return new Stats(Items.GET(), Users.GET());
    }

    @Path("/stats/items")
    public final static class Items {
        @GET
        public static ItemStats GET() {
            return new ItemStats();
        }

        @Path("/stats/items/total")
        public final static class Total {
            @GET
            public static long GET() {
                return Items.GET().total();
            }
        }
    }

    @Path("/stats/users")
    public final static class Users {
        @GET
        public static UserStats GET() {
            return new UserStats(0, 0, 0);
        }

        @Path("/stats/users/total")
        public final static class Total {
            @GET
            public static long GET() {
                return Users.GET().total();
            }
        }
    }
}
