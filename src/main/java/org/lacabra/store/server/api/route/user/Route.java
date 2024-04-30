package org.lacabra.store.server.api.route.user;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
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

            var user = UserDAO.getInstance().findOne(new Credentials(id));
            if (user == null)
                return Response.status(Response.Status.NOT_FOUND).build();

            user = user.merge(new User(new Credentials(user.id(), user.authorities(), null), user.data()));

            return Response.ok().entity(user).build();
        }

        @PUT
        public Response PUT(String json) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }
    }

    @Path("/user/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PermitAll
    public final static class Register {
        @POST
        public Response POST(@FormParam("id") String id,
                             @FormParam("passwd") String passwd) {
            if (!UserId.is(id))
                return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid User ID.").build();

            var dao = UserDAO.getInstance();

            User u = new User(new Credentials(id, List.of(Authority.Client, Authority.Artist), passwd));
            if (dao.findOne(u) != null)
                return Response.status(Response.Status.CONFLICT.getStatusCode(), "User already exists.").build();

            dao.store(u);
            return Response.ok().build();
        }
    }
}
