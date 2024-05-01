package org.lacabra.store.server.api.type.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.internals.json.deserializer.UserDeserializer;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.json.serializer.UserIdSerializer;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.DTOable;
import org.lacabra.store.server.jdo.converter.AuthorityConverter;
import org.lacabra.store.server.jdo.converter.UserIdConverter;
import org.lacabra.store.server.jdo.dao.Mergeable;

import javax.jdo.annotations.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
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
@JsonDeserialize(using = UserDeserializer.Persistent.class)
public class User implements Serializable, Mergeable<User>, DTOable<User, UserDTO> {
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
    private EnumSet<Authority> authorities;

    @JsonUnwrapped
    @Embedded
    private UserData data;

    public User() {
        super();
    }

    public User(final String id) {
        this(id, null);
    }

    public User(final UserId id) {
        this(id, null);
    }

    public User(final String id, final String passwd) {
        this(new Credentials(id, passwd));
    }

    public User(final UserId id, final String passwd) {
        this(new Credentials(id, passwd));
    }

    public User(final Credentials creds) {
        this(creds, null);
    }

    public User(final Credentials creds, final UserData data) {
        if (creds != null) {
            this.id(creds.id());
            this.authorities(creds.authorities());
            this.passwd(creds.passwd());
        }

        this.data(data);
    }

    public User(final User user) {
        this(user == null ? null : new Credentials(user.id, user.authorities, user.passwd), user == null ? null :
                user.data);
    }

    public Set<Authority> authorities() {
        return this.authorities == null || this.authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(this.authorities);
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

    private void id(final String id) {
        this.id(UserId.from(id));
    }

    private void id(final UserId id) {
        this.id = id;
    }

    private void passwd(final String passwd) {
        this.passwd = passwd;
    }

    private void authorities(final Collection<Authority> authorities) {
        this.authorities = authorities == null || authorities.isEmpty() ?
                EnumSet.noneOf(Authority.class) : EnumSet.copyOf(authorities);
    }

    private void data(final UserData data) {
        this.data = data;
    }

    @Override
    public User merge(final User override) {
        if (override == null) return this;

        if (override.id != null) this.id(override.id);

        if (override.passwd != null) this.passwd(override.passwd);

        if (override.authorities != null) this.authorities(override.authorities);

        if (this.data == null) this.data(override.data);

        else this.data(this.data.merge(override.data));

        Mergeable.super.merge(this);

        return this;
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO(new Credentials(this.id, this.authorities, this.passwd));
    }

    public static User fromDTO(final UserDTO dto) {
        if (dto == null)
            return null;

        return new User(new Credentials(dto.id(), dto.authorities(), dto.passwd()));
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
