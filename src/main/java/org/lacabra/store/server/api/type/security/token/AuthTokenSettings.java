package org.lacabra.store.server.api.type.security.token;

public record AuthTokenSettings(String secret, Long clockSkew, String audience, String issuer,
                                String authoritiesClaimName, String refreshCountClaimName,
                                String refreshLimitClaimName) {
    public AuthTokenSettings {
    }

    public AuthTokenSettings(String secret, Long clockSkew, String audience, String issuer) {
        this(secret, clockSkew, audience, issuer, "authorities", "refreshCount", "refreshLimit");
    }
}
