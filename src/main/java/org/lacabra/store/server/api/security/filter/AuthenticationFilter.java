package org.lacabra.store.server.api.security.filter;

import org.lacabra.store.server.api.security.service.token.AuthTokenUtils;
import org.lacabra.store.server.api.type.security.context.TokenSecurityContext;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.security.user.AuthedUserDetails;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jpa.dao.UserDAO;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Dependent
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Inject
    private AuthTokenUtils tokenUtils;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        switch (requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)) {
            case String s when s.startsWith("Bearer ") ->
                    handleTokenBasedAuthentication(s.substring("Bearer ".length()), requestContext);
            case null, default -> {
            }
        }
    }

    private void handleTokenBasedAuthentication(String token, ContainerRequestContext requestContext) {
        AuthTokenDetails tokenDetails = tokenUtils.parse(token);
        User user = UserDAO.getInstance().find(tokenDetails.username());

        requestContext.setSecurityContext(new TokenSecurityContext(new AuthedUserDetails(user.id(),
                user.authorities()), tokenDetails, requestContext.getSecurityContext().isSecure()));
    }
}