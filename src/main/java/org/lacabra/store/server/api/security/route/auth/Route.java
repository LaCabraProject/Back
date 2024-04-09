package org.lacabra.store.server.api.security.route.auth;

import jakarta.annotation.security.PermitAll;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.lacabra.store.server.api.security.service.token.AuthTokenUtils;
import org.lacabra.store.server.api.type.security.context.TokenSecurityContext;
import org.lacabra.store.server.api.type.security.password.CredValidator;
import org.lacabra.store.server.api.type.security.token.AuthToken;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/auth")
public class Route {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response POST(Credentials creds) {
        User user = CredValidator.validate(creds.id(), creds.passwd());
        return Response.ok(new AuthToken(AuthTokenUtils.issue(user.id().get(), user.authorities())).token()).build();
    }

    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh(@Context ContainerRequestContext context) {
        return Response.ok(new AuthToken(AuthTokenUtils.refresh(((TokenSecurityContext) context.getSecurityContext()).getAuthTokenDetails()))).build();
    }
}