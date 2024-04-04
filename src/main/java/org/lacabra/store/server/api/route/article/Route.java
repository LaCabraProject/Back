package org.lacabra.store.server.api.route.article;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/article")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class Route {
    @PUT
    public Response PUT(String json) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Path("/article/{id}")
    public final static class ID {
        @GET
        public static Response GET(@PathParam("id") String id) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }

        @PUT
        public Response PUT(String json) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }

        @Path("/article/{id}/buy")
        public final static class Buy {
            @POST
            public static Response POST(@PathParam("id") String id) {
                return Response.status(Response.Status.NOT_IMPLEMENTED).build();
            }
        }
    }
}
