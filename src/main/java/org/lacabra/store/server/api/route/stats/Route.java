package org.lacabra.store.server.api.route.stats;

import org.lacabra.store.server.api.type.stats.ArticleStats;
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
        return new Stats(Articles.GET(), Users.GET());
    }

    @Path("/stats/articles")
    public final static class Articles {
        @GET
        public static ArticleStats GET() {
            return new ArticleStats();
        }

        @Path("/stats/articles/total")
        public final static class Total {
            @GET
            public static long GET() {
                return Articles.GET().total();
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
