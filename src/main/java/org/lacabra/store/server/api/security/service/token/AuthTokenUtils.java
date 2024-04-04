package org.lacabra.store.server.api.security.service.token;

import org.lacabra.store.server.api.type.security.exception.AuthTokenRefreshmentException;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.user.Authority;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public final class AuthTokenUtils {
    private Long validFor;

    private Integer refreshLimit;

    private AuthTokenIssuer issuer;
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
