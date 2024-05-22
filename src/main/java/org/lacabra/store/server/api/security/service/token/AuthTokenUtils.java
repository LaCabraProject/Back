package org.lacabra.store.server.api.security.service.token;

import org.lacabra.store.server.api.type.security.exception.AuthTokenRefreshmentException;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.user.Authority;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public class AuthTokenUtils {
    private static Long validFor;
    private static Integer refreshLimit;

    static {
        Properties properties = new Properties();
        try (final var is = AuthTokenUtils.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                properties.load(is);

                AuthTokenUtils.validFor = Long.valueOf(String.valueOf(properties.get("authentication.jwt.validFor")));
                AuthTokenUtils.refreshLimit =
                        Integer.valueOf(String.valueOf(properties.get("authentication.jwt.refreshLimit")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String issue(String username, Set<Authority> authorities) {
        final var id = UUID.randomUUID().toString();
        final var issuedDate = ZonedDateTime.now();

        return AuthTokenIssuer.issue(new AuthTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(issuedDate.plusSeconds(validFor))
                .withRefreshCount(0)
                .withRefreshLimit(refreshLimit)
                .build());
    }

    public static AuthTokenDetails parse(String token) {
        return AuthTokenParser.parse(token);
    }

    public static String refresh(AuthTokenDetails details) {
        if (details == null || !details.isEligibleForRefreshment()) {
            throw new AuthTokenRefreshmentException("This token cannot be refreshed");
        }

        final var issuedDate = ZonedDateTime.now();

        return AuthTokenIssuer.issue(new AuthTokenDetails.Builder()
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
