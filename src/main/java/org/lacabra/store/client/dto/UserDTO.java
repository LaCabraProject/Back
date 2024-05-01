package org.lacabra.store.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.lacabra.store.internals.json.deserializer.UserDeserializer;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;

@JsonDeserialize(using = UserDeserializer.DTO.class)
public record UserDTO(UserId id, EnumSet<Authority> authorities, String passwd) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserDTO() {
        this((UserId) null);
    }

    public UserDTO(final String id) {
        this(id, null);
    }

    public UserDTO(final UserId id) {
        this(id, null);
    }

    public UserDTO(final String id, final String passwd) {
        this(new Credentials(id, passwd));
    }

    public UserDTO(final UserId id, final String passwd) {
        this(new Credentials(id, passwd));
    }

    public UserDTO(final UserId id, final Collection<Authority> authorities, final String passwd) {
        this(id, authorities == null || authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(authorities), passwd);
    }

    public UserDTO(final Credentials creds) {
        this(creds == null ? null : creds.id(), creds == null ? null : creds.authorities(), creds == null ? null :
                creds.passwd());
    }

    public UserDTO(final UserId id, final EnumSet<Authority> authorities, final String passwd) {
        this.id = id;
        this.authorities = authorities == null || authorities.isEmpty() ? EnumSet.noneOf(Authority.class) :
                EnumSet.copyOf(authorities);
        this.passwd = passwd;
    }

    public UserDTO(final UserDTO user) {
        this(user == null ? null : new Credentials(user.id, user.authorities, user.passwd));
    }

    public UserDTO id(final String id) {
        return this.id(UserId.from(id));
    }

    public UserDTO id(final UserId id) {
        return new UserDTO(id, this.authorities, this.passwd);
    }

    public UserDTO authorities(final Collection<Authority> authorities) {
        return new UserDTO(this.id, authorities, this.passwd);
    }

    public UserDTO passwd(final String passwd) {
        return new UserDTO(this.id, this.authorities, passwd);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(UserDTO.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}