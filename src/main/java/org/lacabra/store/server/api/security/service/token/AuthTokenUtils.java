package org.lacabra.store.server.api.security.service.token;

import org.lacabra.store.server.api.type.security.exception.AuthTokenRefreshmentException;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.configuration.Configurable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class AuthTokenUtils {
    @Inject
    @Configurable("authentication.jwt.validFor")
    private Long validFor;

    @Inject
    @Configurable("authentication.jwt.refreshLimit")
    private Integer refreshLimit;

    @Inject
    private AuthTokenIssuer issuer;

    @Inject
    private AuthTokenParser parser;

    public String issue(String username, Set<Authority> authorities) {
        String id = UUID.randomUUID().toString();
        ZonedDateTime issuedDate = ZonedDateTime.now();

        return issuer.issue(new AuthTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(issuedDate.plusSeconds(validFor))
                .withRefreshCount(0)
                .withRefreshLimit(refreshLimit)
                .build());
    }

    public AuthTokenDetails parse(String token) {
        return parser.parse(token);
    }

    public String refresh(AuthTokenDetails details) {

        if (!details.isEligibleForRefreshment()) {
            throw new AuthTokenRefreshmentException("This token cannot be refreshed");
        }

        ZonedDateTime issuedDate = ZonedDateTime.now();

        return issuer.issue(new AuthTokenDetails.Builder()
                .withId(details.id())
                .withUsername(details.username())
                .withAuthorities(details.authorities())
                .withIssuedDate(issuedDate)
                .withExpirationDate(issuedDate.plusSeconds(validFor))
                .withRefreshCount(details.refreshCount() + 1)
                .withRefreshLimit(refreshLimit)
                .build());
    }
}
