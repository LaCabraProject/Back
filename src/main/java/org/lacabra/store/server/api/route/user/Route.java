package org.lacabra.store.server.api.route.user;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.dao.UserDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@PermitAll
public final class Route {
    @PUT
    public Response PUT(String json) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Path("/user/all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public final static class All {
        @GET
        public static List<User> GET() {
            return UserDAO.getInstance().find();
        }
    }

    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public final static class ID {
        @GET
        public static Response GET(@PathParam("id") String id) {
            if (!UserId.is(id))
                return Response.status(Response.Status.NOT_FOUND).build();

            var user = UserDAO.getInstance().findOne(new User(new Credentials(id)));
            if (user == null)
                return Response.status(Response.Status.NOT_FOUND).build();

            user = new User(new Credentials(user.id(), user.authorities(), null), user.data());

            return Response.ok().entity(user).build();
        }

        @PUT
        public Response PUT(String json) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }
    }
}
