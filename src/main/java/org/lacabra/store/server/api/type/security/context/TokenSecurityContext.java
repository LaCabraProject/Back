package org.lacabra.store.server.api.type.security.context;

import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.security.user.AuthedUserDetails;
import org.lacabra.store.server.api.type.user.Authority;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class TokenSecurityContext implements SecurityContext {
    private final boolean secure;
    private final AuthedUserDetails user;
    private final AuthTokenDetails token;

    public TokenSecurityContext(AuthedUserDetails user, AuthTokenDetails token, boolean secure) {
        this.user = user;
        this.token = token;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String s) {
        return user != null && user.authorities().contains(Authority.valueOf(s));
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }

    public AuthTokenDetails getAuthTokenDetails() {
        return token;
    }
}
