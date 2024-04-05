package org.lacabra.store.server.api.type.security.error;

import org.lacabra.store.server.api.type.security.exception.AuthTokenRefreshmentException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthTokenRefreshmentExceptionMapper implements ExceptionMapper<AuthTokenRefreshmentException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(AuthTokenRefreshmentException exception) {
        Status status = Status.FORBIDDEN;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage("The authentication token cannot be refreshed.");
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}