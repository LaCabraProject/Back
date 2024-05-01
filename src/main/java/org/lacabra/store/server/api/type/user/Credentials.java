package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.internals.json.serializer.UserIdSerializer;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;

public final class Credentials implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @JsonSerialize(using = UserIdSerializer.class)
    private final UserId id;

    @JsonProperty("passwd")
    private final String passwd;

    @JsonProperty("authorities")
    private final EnumSet<Authority> authorities;

    public Credentials() {
        this.id = null;
        this.passwd = null;
        this.authorities = null;
    }

    public Credentials(Authority authorities) {
        this(authorities == null ? null : EnumSet.of(authorities));
    }

    public Credentials(Collection<Authority> authorities) {
        this.id = null;
        this.passwd = null;
        this.authorities = authorities == null ? null : authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(authorities);
    }

    public Credentials(String id) {
        this(id, (String) null);
    }

    public Credentials(UserId id) {
        this(id, (String) null);
    }

    public Credentials(String id, Authority authorities) {
        this(id, authorities == null ? null : EnumSet.of(authorities));
    }

    public Credentials(String id, Collection<Authority> authorities) {
        this(UserId.from(id), authorities);
    }

    public Credentials(String id, String passwd) {
        this(UserId.from(id), passwd);
    }

    public Credentials(UserId id, String passwd) {
        this.id = id;
        this.passwd = passwd;
        this.authorities = null;
    }

    public Credentials(String id, Authority authorities, String passwd) {
        this(id, authorities == null ? null : EnumSet.of(authorities), passwd);
    }

    public Credentials(String id, Collection<Authority> authorities, String passwd) {
        this(UserId.from(id), authorities, passwd);
    }

    public Credentials(UserId id, Authority authorities) {
        this(id, authorities == null ? null : EnumSet.of(authorities));
    }

    public Credentials(UserId id, Collection<Authority> authorities) {
        this.id = id;
        this.passwd = null;
        this.authorities = authorities == null || authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(authorities);
    }

    public Credentials(UserId id, Authority authorities, String passwd) {
        this(id, authorities == null ? null : Collections.singleton(authorities), passwd);
    }

    public Credentials(UserId id, Collection<Authority> authorities, String passwd) {
        this.id = id;
        this.authorities = authorities == null ? null : authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(authorities);
        this.passwd = passwd;
    }

    public Credentials(Credentials creds) {
        this(creds.id, creds.authorities, creds.passwd);
    }

    public EnumSet<Authority> authorities() {
        return this.authorities == null || this.authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(this.authorities);
    }

    public Credentials authorities(final Collection<Authority> authorities) {
        return new Credentials(this.id, authorities, this.passwd);
    }

    public UserId id() {
        return this.id;
    }

    public Credentials id(final String id) {
        return this.id(UserId.from(id));
    }

    public Credentials id(final UserId id) {
        return new Credentials(id, this.authorities, this.passwd);
    }

    public String passwd() {
        return this.passwd;
    }

    public Credentials passwd(final String passwd) {
        return new Credentials(this.id, this.authorities, passwd);
    }

    @Override
    public boolean equals(final Object o) {
        return switch (o) {
            case Credentials u ->
                    Objects.equals(this.id, u.id) && Objects.equals(this.passwd, u.passwd) && Objects.equals(this.authorities, u.authorities);

            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(Credentials.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}