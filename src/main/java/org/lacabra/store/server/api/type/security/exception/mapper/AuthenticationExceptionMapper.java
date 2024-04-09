package org.lacabra.store.server.api.type.security.exception.mapper;

import org.lacabra.store.server.api.type.security.error.ApiErrorDetails;
import org.lacabra.store.server.api.type.security.exception.AuthenticationException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(AuthenticationException exception) {

        Response.Status status = Response.Status.UNAUTHORIZED;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage("Could not authenticate.");
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}