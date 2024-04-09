package org.lacabra.store.client.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UserId id;
    private String passwd;
    private Set<Authority> authorities;
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
        Objects.requireNonNull(creds);

        this.id = creds.id;
        this.passwd = creds.passwd;
        this.authorities = creds.authorities;
        this.data = data;
    }

    public User(User user) {
        this(user == null ? null : new Credentials(user.id, user.authorities, user.passwd), user == null ? null :
                user.data);
    }

    public User() {
        this((User) null);
    }


    public Set<Authority> authorities() {
        return this.authorities;
    }

    public String id() {
        return this.id.get();
    }

    public String passwd() {
        return this.passwd;
    }

    public UserData data() {
        return data;
    }
}
