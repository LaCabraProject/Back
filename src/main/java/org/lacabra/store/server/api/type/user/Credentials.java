package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.json.serializer.UserIdSerializer;

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
        this.authorities = EnumSet.noneOf(Authority.class);
    }

    public Credentials(Authority authorities) {
        this(Collections.singleton(authorities));
    }

    public Credentials(Collection<Authority> authorities) {
        this.id = null;
        this.passwd = null;
        this.authorities = authorities == null ? EnumSet.noneOf(Authority.class) : EnumSet.copyOf(authorities);
    }

    public Credentials(String id) {
        this(id, (String) null);
    }

    public Credentials(UserId id) {
        this(id, (String) null);
    }

    public Credentials(String id, Authority authorities) {
        this(id, Collections.singleton(authorities));
    }

    public Credentials(String id, Collection<Authority> authorities) {
        this(UserId.from(id), authorities);
    }

    public Credentials(String id, String passwd) {
        this(UserId.from(id), passwd);
    }

    public Credentials(UserId id, String passwd) {
        this.id = Objects.requireNonNull(id);
        this.passwd = passwd;
        this.authorities = EnumSet.noneOf(Authority.class);
    }

    public Credentials(String id, Authority authorities, String passwd) {
        this(id, Collections.singleton(authorities), passwd);
    }

    public Credentials(String id, Collection<Authority> authorities, String passwd) {
        this(UserId.from(id), authorities, passwd);
    }

    public Credentials(UserId id, Authority authorities) {
        this(id, Collections.singleton(authorities));
    }

    public Credentials(UserId id, Collection<Authority> authorities) {
        this.id = Objects.requireNonNull(id);
        this.passwd = null;
        this.authorities = authorities == null ? EnumSet.noneOf(Authority.class) : EnumSet.copyOf(authorities);
    }

    public Credentials(UserId id, Authority authorities, String passwd) {
        this(id, Collections.singleton(authorities), passwd);
    }

    public Credentials(UserId id, Collection<Authority> authorities, String passwd) {
        this.id = id;
        this.authorities = authorities == null ? EnumSet.noneOf(Authority.class) : EnumSet.copyOf(authorities);
        this.passwd = passwd;
    }

    public Credentials(Credentials creds) {
        this(creds.id, creds.authorities, creds.passwd);
    }

    public EnumSet<Authority> authorities() {
        return EnumSet.copyOf(this.authorities);
    }

    public UserId id() {
        return this.id;
    }

    public String passwd() {
        return this.passwd;
    }

    @Override
    public boolean equals(Object o) {
        return switch (o) {
            case Credentials u -> switch (u.id) {
                case null -> false;
                case UserId i -> this.id.equals(i);
            } && switch (u.passwd) {
                case null -> false;
                case String p -> this.passwd.equals(p);
            } && this.authorities.equals(u.authorities);

            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return this.id.get();
    }
}