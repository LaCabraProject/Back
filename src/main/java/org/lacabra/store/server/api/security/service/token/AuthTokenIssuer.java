package org.lacabra.store.server.api.security.service.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.security.token.AuthTokenSettings;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Date;

@Dependent
public final class AuthTokenIssuer {
    @Inject
    private AuthTokenSettings settings;

    public String issue(AuthTokenDetails details) {
        return Jwts.builder()
                .id(details.id())
                .subject(details.username())
                .issuer(settings.issuer())
                .setAudience(settings.audience())
                .issuedAt(Date.from(details.issuedDate().toInstant()))
                .expiration(Date.from(details.expirationDate().toInstant()))
                .claim(settings.authoritiesClaimName(), details.authorities())
                .claim(settings.refreshCountClaimName(), details.refreshCount())
                .claim(settings.refreshLimitClaimName(), details.refreshLimit())
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(settings.secret())))
                .compact();
    }
}
