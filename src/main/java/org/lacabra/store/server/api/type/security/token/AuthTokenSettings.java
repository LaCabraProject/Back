package org.lacabra.store.server.api.type.security.token;

import org.lacabra.store.server.configuration.Configurable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public final class AuthTokenSettings {
    @Inject
    @Configurable("authentication.jwt.secret")
    private String secret;

    @Inject
    @Configurable("authentication.jwt.clockSkew")
    private Long clockSkew;

    @Inject
    @Configurable("authentication.jwt.audience")
    private String audience;

    @Inject
    @Configurable("authentication.jwt.issuer")
    private String issuer;

    @Inject
    @Configurable("authentication.jwt.claimNames.authorities")
    private String authoritiesClaimName;

    @Inject
    @Configurable("authentication.jwt.claimNames.refreshCount")
    private String refreshCountClaimName;

    @Inject
    @Configurable("authentication.jwt.claimNames.refreshLimit")
    private String refreshLimitClaimName;

    public String secret() {
        return this.secret;
    }

    public Long clockSkew() {return this.clockSkew;}

    public String audience() {
        return this.audience;
    }

    public String issuer() {
        return this.issuer;
    }

    public String authoritiesClaimName() {
        return this.authoritiesClaimName;
    }

    public String refreshCountClaimName() {
        return this.refreshCountClaimName;
    }

    public String refreshLimitClaimName() {
        return this.refreshLimitClaimName;
    }
}
