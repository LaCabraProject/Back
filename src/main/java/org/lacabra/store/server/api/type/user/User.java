package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.jdo.converter.UserIdConverter;
import org.lacabra.store.server.json.deserializer.UserDeserializer;
import org.lacabra.store.server.json.serializer.UserIdSerializer;

import javax.jdo.annotations.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
public class User implements Serializable {
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
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Persistent
    private Set<Authority> authorities;

    @JsonUnwrapped
    @Embedded
    private UserData data;

    public User() {
    }

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
        if (creds != null) {
            this.id = creds.id;
            this.passwd = creds.passwd;
            this.authorities = creds.authorities;
        }

        this.data = data;
    }

    public User(User user) {
        this(user == null ? null : new Credentials(user.id, user.authorities, user.passwd), user == null ? null :
                user.data);
    }

    public Set<Authority> authorities() {
        return new HashSet<>(this.authorities);
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

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(User.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
