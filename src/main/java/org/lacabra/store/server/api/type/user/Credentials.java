package org.lacabra.store.server.api.type.user;

import jakarta.persistence.*;
import org.lacabra.store.server.api.type.id.UserId;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Embeddable
public final class Credentials implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.LAZY)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user"))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<Authority> authorities;

    @NotNull
    @Column(name = "id")
    private String id;
    private String passwd;

    public Credentials() {
        this.authorities = Collections.EMPTY_SET;
        this.id = null;
        this.passwd = null;
    }

    public Credentials(Authority authorities) {
        this(Collections.singleton(authorities));
    }

    public Credentials(Collection<Authority> authorities) {
        this.authorities = authorities == null ? Collections.EMPTY_SET : Set.copyOf(authorities);
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
        this.id = Objects.requireNonNull(id).get();
        this.passwd = passwd;
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
        this.id = Objects.requireNonNull(id).get();
        this.authorities = authorities == null ? Collections.EMPTY_SET : Set.copyOf(authorities);
    }

    public Credentials(UserId id, Authority authorities, String passwd) {
        this(id, Collections.singleton(authorities), passwd);
    }

    public Credentials(UserId id, Collection<Authority> authorities, String passwd) {
        this.id = Objects.requireNonNull(id).get();
        this.authorities = authorities == null ? Collections.EMPTY_SET : Set.copyOf(authorities);
        this.passwd = passwd;
    }

    public Credentials(Credentials creds) {
        this(creds.id, creds.authorities, creds.passwd);
    }

    public Set<Authority> authorities() {
        return this.authorities;
    }

    public String id() {
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
                case String i -> this.id.equals(i);
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
}