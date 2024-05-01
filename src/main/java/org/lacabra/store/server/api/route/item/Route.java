package org.lacabra.store.server.api.route.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.security.PermitAll;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.tuple.Pair;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        Item[] ret;

        try {
            ret = mapper.treeToValue(node, Item[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var dao = ItemDAO.getInstance();

        List<Pair<Item, Item>> trans = new ArrayList<>();

        for (final Item i : ret) {
            if (i == null) {
                final String reason = "Not an item.";
                Logger.getLogger().warning("Bad request: " + reason);

                return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
            }

            Item found = null;

            if (i.id() != null) {
                found = dao.findOne(dao.getQuery("FindItem"), i);
                if (!(found == null || (found.parent() != null && found.parent().id().equals(uid)))) {
                    final String reason = "Not the parent of the item.";
                    Logger.getLogger().warning("Unauthorized: " + reason);

                    return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), reason).build();
                }
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

            trans.add(new Pair<>(found, i.merge(new Item(i.id() == null ? ObjectId.random(Item.class) : null,
                    null, null, null, null,
                    null, null, null, new User(uid)))));
        }

        List<Item> stored = new ArrayList<>();
        for (Pair<Item, Item> pair : trans) {
            final Response.ResponseBuilder r = Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    String.format(
                            "Could " +
                                    "not put item %s.", pair.x().id()));

            if (!dao.delete(pair.x()))
                return r.build();

            if (!dao.store(pair.y()))
                return r.build();

            stored.add(pair.y());
        }

        return switch (Integer.valueOf(stored.size())) {
            case Integer zero when zero.equals(0) -> Response.ok().build();
            case Integer one when one.equals(1) -> Response.ok(stored.getFirst()).build();
            case Integer other -> Response.ok(stored.toArray(new Item[0])).build();
        };
    }

    @PATCH
    @PermitAll
    public Response PATCH(@Context ContainerRequestContext context, String json) {
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

        Item[] ret;

        try {
            ret = mapper.treeToValue(node, Item[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var dao = ItemDAO.getInstance();

        List<Item> stored = new ArrayList<>();

        for (final Item i : ret) {
            if (i == null) {
                final String reason = "Not an item.";
                Logger.getLogger().warning("Bad request: " + reason);

                return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
            }


            if (i.id() == null) {
                final String reason = "Item must have an ID.";
                Logger.getLogger().warning("Bad request: " + reason);

                return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
            }

            Item found = dao.findOne(dao.getQuery("FindItem"), i);
            if (found == null) {
                final String reason = String.format("Item %s not found.", i.id());
                Logger.getLogger().warning("Bad request: " + reason);

                return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), reason).build();
            }

            if (!Objects.equals(found.parent().id(), uid)) {
                final String reason = String.format("Not the parent of item %s.");
                Logger.getLogger().warning("Unauthorized: " + reason);

                return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), reason).build();
            }

            stored.add(found.merge(i));
        }

        return switch (Integer.valueOf(stored.size())) {
            case Integer zero when zero.equals(0) -> Response.status(Response.Status.NO_CONTENT).build();
            case Integer one when one.equals(1) -> Response.ok(stored.getFirst()).build();
            case Integer other -> Response.ok(stored.toArray(new Item[0])).build();
        };
    }

    @Path("/item/all")
    @Produces(MediaType.APPLICATION_JSON)
    public final static class All {
        @GET
        @PermitAll
        public static List<Item> GET() {
            return ItemDAO.getInstance().find();
        }
    }

    @Path("/item/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public final static class ID {
        @GET
        @PermitAll
        public static Response GET(@PathParam("id") String id) {
            var oid = ObjectId.from(id);

            if (oid == null) return Response.status(Response.Status.NOT_FOUND).build();

            var item = ItemDAO.getInstance().findOne(new Item(ObjectId.from(id)));
            if (item == null) return Response.status(Response.Status.NOT_FOUND).build();

            return Response.ok().entity(item).build();
        }

        @Path("/item/{id}/buy")
        public final static class Buy {
            @POST
            public static Response POST(@PathParam("id") String id) {
                if (!ObjectId.is(id)) return Response.status(Response.Status.NOT_FOUND).build();

                var item = ItemDAO.getInstance().findOne(new Item(ObjectId.from(id)));
                if (item == null) return Response.status(Response.Status.NOT_FOUND).build();

                if (item.stock().equals(BigInteger.ZERO))
                    return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "No stock.").build();

                ItemDAO.getInstance().store(item.merge(new Item(null, null, null, null, null, null, BigInteger.ZERO,
                        null)));

                return Response.ok().build();
            }
        }
    }
}
