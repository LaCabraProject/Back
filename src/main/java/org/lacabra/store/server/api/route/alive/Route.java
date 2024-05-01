package org.lacabra.store.server.api.route.alive;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;

import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/alive")
@RequestScoped
public final class Route {
    @PermitAll
    @HEAD
    public Response HEAD() {
        return Response.ok().build();
    }
}
