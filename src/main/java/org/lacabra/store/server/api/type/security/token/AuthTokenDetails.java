package org.lacabra.store.server.api.type.security.token;

import org.lacabra.store.server.api.type.user.Authority;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AuthTokenDetails {
    private final String id;
    private final String username;
    private final Set<Authority> authorities;
    private final ZonedDateTime issuedDate;
    private final ZonedDateTime expirationDate;
    private final int refreshCount;
    private final int refreshLimit;

    private AuthTokenDetails(String id, String username, Set<Authority> authorities, ZonedDateTime issuedDate,
                             ZonedDateTime expirationDate, int refreshCount, int refreshLimit) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
        this.refreshCount = refreshCount;
        this.refreshLimit = refreshLimit;
    }

    public String id() {
        return id;
    }

    public String username() {
        return username;
    }


    public Set<Authority> authorities() {
        return authorities;
    }

    public ZonedDateTime issuedDate() {
        return issuedDate;
    }

    public ZonedDateTime expirationDate() {
        return expirationDate;
    }

    public int refreshCount() {
        return refreshCount;
    }

    public int refreshLimit() {
        return refreshLimit;
    }

    public boolean isEligibleForRefreshment() {
        return refreshCount < refreshLimit;
    }

    public static class Builder {
        private String id;
        private String username;
        private Set<Authority> authorities;
        private ZonedDateTime issuedDate;
        private ZonedDateTime expirationDate;
        private int refreshCount;
        private int refreshLimit;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withAuthorities(Set<Authority> authorities) {
            this.authorities = Collections.unmodifiableSet(authorities == null ? new HashSet<>() : authorities);
            return this;
        }

        public Builder withIssuedDate(ZonedDateTime issuedDate) {
            this.issuedDate = issuedDate;
            return this;
        }

        public Builder withExpirationDate(ZonedDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder withRefreshCount(int refreshCount) {
            this.refreshCount = refreshCount;
            return this;
        }

        public Builder withRefreshLimit(int refreshLimit) {
            this.refreshLimit = refreshLimit;
            return this;
        }

        public AuthTokenDetails build() {
            return new AuthTokenDetails(id, username, authorities, issuedDate, expirationDate, refreshCount,
                    refreshLimit);
        }
    }
}
