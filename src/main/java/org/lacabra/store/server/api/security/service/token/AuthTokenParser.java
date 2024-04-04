package org.lacabra.store.server.api.security.service.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.lacabra.store.server.api.type.security.exception.InvalidAuthTokenException;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.security.token.AuthTokenSettings;
import org.lacabra.store.server.api.type.user.Authority;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AuthTokenParser {
    @Inject
    private AuthTokenSettings settings;

    public AuthTokenDetails parse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(settings.secret())))
                    .requireAudience(settings.audience())
                    .clockSkewSeconds(settings.clockSkew()).build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new AuthTokenDetails.Builder()
                    .withId((String) claims.get(Claims.ID))
                    .withUsername(claims.getSubject())
                    .withAuthorities(((List<String>) claims.getOrDefault(settings.authoritiesClaimName(),
                            new ArrayList<>())).stream().map(Authority::valueOf).collect(Collectors.toSet()))
                    .withIssuedDate(ZonedDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()))
                    .withExpirationDate(ZonedDateTime.ofInstant(claims.getExpiration().toInstant(),
                            ZoneId.systemDefault()))
                    .withRefreshCount((int) claims.get(settings.refreshCountClaimName()))
                    .withRefreshLimit((int) claims.get(settings.refreshLimitClaimName()))
                    .build();
        } catch (Exception e) {
            throw switch (e) {
                case ExpiredJwtException ex -> new InvalidAuthTokenException("Expired token", ex);
                case InvalidClaimException ex ->
                        new InvalidAuthTokenException("Invalid value for claim \"" + ex.getClaimName() + "\"", ex);
                default -> new InvalidAuthTokenException("Invalid token", e);
            };
        }
    }
}
