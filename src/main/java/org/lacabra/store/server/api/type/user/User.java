package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.internals.json.deserializer.UserDeserializer;
import org.lacabra.store.internals.json.serializer.UserIdSerializer;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.jdo.converter.AuthorityConverter;
import org.lacabra.store.server.jdo.converter.UserIdConverter;
import org.lacabra.store.server.jdo.dao.Mergeable;

import javax.jdo.annotations.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Query(name = "FindUser", value = "SELECT FROM User WHERE id == :id")
@Query(name = "CredMatch", value = "SELECT FROM User WHERE id == :id && passwd == :passwd")
@Query(name = "FindClients", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities " +
        "LIKE" + " '%client%'")
@Query(name = "FindArtists", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities " +
        "LIKE" + " '%artist%'")
@Query(name = "FindAdmins", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities LIKE "
        + "'%admin%'")
@Query(name = "CountClients", language = "javax.jdo.query.SQL", value =
        "SELECT * FROM USER u WHERE u.authorities " + "LIKE '%client%'")
@Query(name = "CountArtists", language = "javax.jdo.query.SQL", value =
        "SELECT * FROM USER u WHERE u.authorities " + "LIKE '%artist%'")
@Query(name = "CountAdmins", language = "javax.jdo.query.SQL", value = "SELECT * FROM USER u WHERE u.authorities " +
        "LIKE" + " '%admin%'")
@PersistenceCapable(table = "user")
@JsonDeserialize(using = UserDeserializer.class)
public class User implements Serializable, Mergeable<User> {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @JsonSerialize(using = UserIdSerializer.class)
    @Convert(UserIdConverter.class)
    @PrimaryKey
    private UserId id;

    @JsonProperty("passwd")
    @Persistent
    private String passwd;

    @JsonProperty("authorities")
    @Element(types = {Authority.class}, converter = AuthorityConverter.class)
    private HashSet<Authority> authorities;

    @JsonUnwrapped
    @Embedded
    private UserData data;

    public User() {
    }

    public User(String id) {
        this(id, null);
    }

    public User(UserId id) {
        this(id, null);
    }

    public User(String id, String passwd) {
        this(new Credentials(id, passwd));
    }

    public User(UserId id, String passwd) {
        this(new Credentials(id, passwd));
    }

    public User(Credentials creds) {
        this(creds, null);
    }

    public User(Credentials creds, UserData data) {
        if (creds != null) {
            this.id = creds.id();
            this.passwd = creds.passwd();
            this.authorities = new HashSet<Authority>(creds.authorities() == null ?
                    Collections.emptySet() :
                    Objects.requireNonNull(creds.authorities()));
        }

        this.data = data;
    }

    public User(User user) {
        this(user == null ? null : new Credentials(user.id, user.authorities, user.passwd), user == null ? null :
                user.data);
    }

    public Set<Authority> authorities() {
        return this.authorities == null ? null : new HashSet<>(this.authorities);
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

    private void setId(UserId id) {
        this.id = id;
    }

    private void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    private void setAuthorities(Collection<Authority> authorities) {
        this.authorities = new HashSet<>(authorities == null ? Collections.emptySet() : authorities);
    }

    private void setData(UserData data) {
        this.data = data;
    }

    @Override
    public User merge(User override) {
        if (override == null) return this;

        if (override.id != null)
            this.setId(override.id);

        if (override.passwd != null)
            this.setPasswd(override.passwd);

        if (override.authorities != null)
            this.setAuthorities(override.authorities);

        if (this.data == null)
            this.setData(override.data);

        else
            this.setData(this.data.merge(override.data));

        Mergeable.super.merge(this);

        return this;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(User.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
