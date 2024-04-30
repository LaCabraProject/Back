package org.lacabra.store.server.api.security.service.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.Dependent;
import org.lacabra.store.server.api.type.security.token.AuthTokenDetails;
import org.lacabra.store.server.api.type.security.token.AuthTokenSettings;

import java.util.Date;

@Dependent
@SuppressWarnings("deprecation")
public class AuthTokenIssuer {
    public static String issue(AuthTokenDetails details) {
        return Jwts.builder()
                .id(details.id())
                .subject(details.username())
                .issuer(AuthTokenSettings.issuer())
                .setAudience(AuthTokenSettings.audience())
                .issuedAt(Date.from(details.issuedDate().toInstant()))
                .expiration(Date.from(details.expirationDate().toInstant()))
                .claim(AuthTokenSettings.authoritiesClaimName(), details.authorities())
                .claim(AuthTokenSettings.refreshCountClaimName(), details.refreshCount())
                .claim(AuthTokenSettings.refreshLimitClaimName(), details.refreshLimit())
                .signWith(SignatureAlgorithm.HS256,
                        Keys.hmacShaKeyFor(Decoders.BASE64.decode(AuthTokenSettings.secret())))
                .compact();
    }
}
