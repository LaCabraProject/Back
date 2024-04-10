package org.lacabra.store.server.api.security.service.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.Dependent;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.security.exception.InvalidAuthTokenException;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.security.token.AuthTokenSettings;
import org.lacabra.store.server.api.type.user.Authority;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Dependent
public class AuthTokenParser {
    public static AuthTokenDetails parse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(AuthTokenSettings.secret())))
                    .requireAudience(AuthTokenSettings.audience())
                    .clockSkewSeconds(AuthTokenSettings.clockSkew()).build()
                    .parseClaimsJws(token)
                    .getBody();

            return new AuthTokenDetails.Builder()
                    .withId((String) claims.get(Claims.ID))
                    .withUsername(claims.getSubject())
                    .withAuthorities(((List<String>) claims.getOrDefault(AuthTokenSettings.authoritiesClaimName(),
                            new ArrayList<>())).stream().map(Authority::parse).collect(Collectors.toSet()))
                    .withIssuedDate(ZonedDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()))
                    .withExpirationDate(ZonedDateTime.ofInstant(claims.getExpiration().toInstant(),
                            ZoneId.systemDefault()))
                    .withRefreshCount((int) claims.get(AuthTokenSettings.refreshCountClaimName()))
                    .withRefreshLimit((int) claims.get(AuthTokenSettings.refreshLimitClaimName()))
                    .build();
        } catch (Exception e) {
            RuntimeException exception = switch (e) {
                case ExpiredJwtException ex -> new InvalidAuthTokenException("Expired token", ex);
                case InvalidClaimException ex ->
                        new InvalidAuthTokenException("Invalid value for claim \"" + ex.getClaimName() + "\"", ex);
                default -> new InvalidAuthTokenException("Invalid token", e);
            };

            Logger.getLogger().warning(exception);
            throw exception;
        }
    }
}
