package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.json.serializer.UserIdSerializer;

import javax.jdo.annotations.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Query(name = "FindUser", language = "JDOQL", value = "SELECT FROM USER u WHERE u.id = :id")
@Query(name = "FindClients", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities LIKE" +
        " '%client%'")
@Query(name = "FindArtists", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities LIKE" +
        " '%artist%'")
@Query(name = "FindAdmins", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities LIKE " +
        "'%admin%'")
@Query(name = "CountClients", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities " +
        "LIKE '%client%'")
@Query(name = "CountArtists", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities " +
        "LIKE '%artist%'")
@Query(name = "CountAdmins", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities LIKE" +
        " '%admin%'")
@PersistenceCapable(table = "user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @JsonProperty("id")
    @JsonSerialize(using = UserIdSerializer.class)
    private UserId id;

    @JsonProperty("passwd")
    @Persistent
    private String passwd;

    @JsonProperty("authorities")
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Persistent
    private Set<Authority> authorities;

    @JsonUnwrapped
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

    public UserId id() {
        return this.id;
    }

    public String passwd() {
        return this.passwd;
    }

    public UserData data() {
        return data;
    }
}
