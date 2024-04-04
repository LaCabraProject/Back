package org.lacabra.store.server.api.type.user;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@NamedQuery(name = "FindUser", query = "SELECT u FROM User u WHERE u.creds.id = :id")
@NamedNativeQuery(name = "FindClient", query =
        "SELECT u FROM User u WHERE \"client\" IN (u.creds.authorities) AND u.creds.id = :id")
@NamedNativeQuery(name = "FindArtist", query =
        "SELECT u FROM User u WHERE \"artist\" IN (u.creds.authorities) AND u.creds.id = :id")
@NamedNativeQuery(name = "FindAdmin", query =
        "SELECT u FROM User u WHERE \"admin\" IN (u.creds.authorities) AND u.creds.id = :id")
@Table(name = "user")
@Entity
public sealed class User implements Serializable permits Client, Artist, Admin {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Credentials creds;
    @Embedded
    private UserData data;

    public User(String id) {
        this(id, null);
    }

    public User(String id, String passwd) {
        this(new Credentials(id, passwd));
    }

    public User(Credentials creds) {
        this(creds, null);
    }

    public User(Credentials creds, UserData data) {
        this.creds = Objects.requireNonNull(creds);
        this.data = data;
    }

    public User(User user) {
        this(user == null ? null : user.creds, user == null ? null : user.data);
    }

    public User() {
        this((User) null);
    }


    public Set<Authority> authorities() {
        return creds == null ? null : creds.authorities();
    }

    public String id() {
        return creds == null ? null : creds.id();
    }

    public String passwd() {
        return creds == null ? null : creds.passwd();
    }

    public UserData data() {
        return data;
    }
}
