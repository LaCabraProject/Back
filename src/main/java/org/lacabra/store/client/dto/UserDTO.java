package org.lacabra.store.client.dto;

import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.UserData;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UserId id;
    private String passwd;
    private HashSet<Authority> authorities;
    private UserData data;

    public UserDTO() {
    }

    public UserDTO(String id) {
        this(id, null);
    }

    public UserDTO(String id, String passwd) {
        this(new Credentials(id, passwd));
    }

    public UserDTO(Credentials creds) {
        this(creds, null);
    }

    public UserDTO(Credentials creds, UserData data) {
        if (creds != null) {
            this.id = creds.id();
            this.passwd = creds.passwd();
            this.authorities = creds.authorities() != null ? new HashSet<>(creds.authorities()) : new HashSet<>();
        }

        this.data = data;
    }

    public UserDTO(org.lacabra.store.server.api.type.user.User user) {
        this(user == null ? null : new Credentials(user.id(), user.authorities(), user.passwd()), user == null ? null :
                user.data());
    }

    public Set<Authority> authorities() {
        return new HashSet<>(this.authorities);
    }
    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = new HashSet<>(authorities);
    }

    public UserId id() {
        return this.id;
    }
    public void setId(UserId id) {
        this.id = id;
    }

    public String passwd() {
        return this.passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public UserData data() {
        return data;
    }
    public void setData(UserData data) {
        this.data = data;
    }
}
