package org.lacabra.store.server.api.route.item;

import org.lacabra.store.server.jpa.dto.ItemDTO;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
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
    public Response PUT(String json) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Path("/item/all")
    @Produces(MediaType.APPLICATION_JSON)
    public final static class All {
        @GET
        public static List<ItemDTO> GET() {
            return new ArrayList<>();
        }
    }

    @Path("/item/{id}")
    public final static class ID {
        @GET
        public static Response GET(@PathParam("id") String id) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }

        @PUT
        public Response PUT(String json) {
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
