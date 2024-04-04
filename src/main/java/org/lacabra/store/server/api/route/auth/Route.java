package org.lacabra.store.server.api.route.auth;

import org.lacabra.store.server.api.type.user.Credentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public final class Route {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response POST(Credentials creds) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
