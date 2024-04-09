package org.lacabra.store.server.api.route.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jakarta.annotation.security.PermitAll;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.security.context.TokenSecurityContext;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.dao.ItemDAO;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public final class Route {
    @PUT
    @PermitAll
    public Response PUT(@Context ContainerRequestContext context, String json) {
        AuthTokenDetails user = ((TokenSecurityContext) context.getSecurityContext()).getAuthTokenDetails();
        if (!user.authorities().contains(Authority.Artist))
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), "Not an artist.").build();

        String uid = user.username();

        var omp = new ObjectMapperProvider();
        var mapper = omp.getContext(Item[].class);

        JsonNode node;

        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Could not parse JSON.").build();
        }

        if (!node.isArray()) {
            node = mapper.getNodeFactory().arrayNode().add(node);
        }

        Object[] ret = new Object[node.size()];

        try {
            int i = 0;
            mapper = omp.getContext(Item.class);

            for (final JsonNode n : node) {
                var v = mapper.readValue(n.toString(), TypeFactory.defaultInstance().constructType(Item.class));
                Logger.getLogger().severe("node: " + n.toPrettyString() + ", result: " + v);
                ret[i++] = v;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var dao = ItemDAO.getInstance();


        List<Item> delete = new ArrayList<>(ret.length);
        List<Item> store = new ArrayList<>(ret.length);

        for (final Object o : ret) {
            if (o instanceof Item i) {
                Item found = dao.findOne(i);
                if (!(found == null || found.parent().id().equals(uid))) {
                    final String reason = "Not the parent of the item.";
                    Logger.getLogger().warning("Unauthorized: " + reason);

                    return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), reason).build();
                }

                if (i.type() == null) {
                    final String reason = "Items must have a type.";
                    Logger.getLogger().warning("Bad request: " + reason);

                    return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
                }

                if (i.name() == null) {
                    final String reason = "Items must have a name.";
                    Logger.getLogger().warning("Bad request: " + reason);

                    return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
                }

                delete.add(found);
                store.add(i.merge(new Item(i.id(), null, null, null, null, null, null, null, new User(uid))));
            }

            final String reason = String.format("Not an item: %s", o);
            Logger.getLogger().warning("Bad request: " + reason);

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
        }

        delete.forEach(dao::delete);
        dao.store(store);

        return Response.ok(ret).build();
    }

    @Path("/item/all")
    @Produces(MediaType.APPLICATION_JSON)
    public final static class All {
        @GET
        public static List<Item> GET() {
            return new ArrayList<>();
        }
    }

    @Path("/item/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public final static class ID {
        @GET
        public static Response GET(@PathParam("id") String id) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }

        @Path("/item/{id}/buy")
        public final static class Buy {
            @POST
            public static Response POST(@PathParam("id") String id) {
                return Response.status(Response.Status.NOT_IMPLEMENTED).build();
            }
        }
    }
}
