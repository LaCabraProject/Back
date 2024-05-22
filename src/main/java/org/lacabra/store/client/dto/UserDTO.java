/**
 * @file UserDTO.java
 * @brief Define la clase UserDTO, un objeto de transferencia de datos (DTO) para transferir información relacionada
 * con usuarios entre el cliente y el servidor.
 */

package org.lacabra.store.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.internals.json.deserializer.UserDeserializer;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.json.serializer.UserIdSerializer;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * @brief Representa un objeto de transferencia de datos (DTO) de usuario utilizado para transferir información
 * relacionada con usuarios entre el cliente y el servidor.
 * Esta clase es serializable y sirve como contenedor de datos para información relacionada con usuarios.
 */
@JsonDeserialize(using = UserDeserializer.DTO.class)
public record UserDTO(
        @JsonProperty("id") @JsonSerialize(using = UserIdSerializer.class) UserId id,
        @JsonProperty("authorities") HashSet<Authority> authorities,
        @JsonProperty("passwd") String passwd
) implements Serializable, DTO<UserDTO, User> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construye un objeto UserDTO vacío.
     */
    public UserDTO() {
        this((UserId) null);
    }

    /**
     * Construye un objeto UserDTO con el ID de usuario especificado.
     *
     * @param id El ID de usuario.
     */
    public UserDTO(final String id) {
        this(id, null);
    }

    /**
     * Construye un objeto UserDTO con el ID de usuario especificado.
     *
     * @param id El ID de usuario.
     */
    public UserDTO(final UserId id) {
        this(id, null);
    }

    /**
     * Construye un objeto UserDTO con el ID de usuario y la contraseña especificados.
     *
     * @param id     El ID de usuario.
     * @param passwd La contraseña del usuario.
     */
    public UserDTO(final String id, final String passwd) {
        this(new Credentials(id, passwd));
    }

    /**
     * Construye un objeto UserDTO con el ID de usuario y la contraseña especificados.
     *
     * @param id     El ID de usuario.
     * @param passwd La contraseña del usuario.
     */
    public UserDTO(final UserId id, final String passwd) {
        this(new Credentials(id, passwd));
    }

    /**
     * Construye un objeto UserDTO con las credenciales especificadas.
     *
     * @param creds Las credenciales del usuario.
     */
    public UserDTO(final Credentials creds) {
        this(creds == null ? null : creds.id(), creds == null ? null : creds.authorities(), creds == null ? null :
                creds.passwd());
    }

    /**
     * Construye un objeto UserDTO con el ID de usuario, las autoridades y la contraseña especificados.
     *
     * @param id          El ID de usuario.
     * @param authorities Las autoridades asociadas con el usuario.
     * @param passwd      La contraseña del usuario.
     */
    public UserDTO(final UserId id, final Collection<Authority> authorities, final String passwd) {
        this(id, new HashSet<>(authorities), passwd);
    }

    /**
     * Construye un objeto UserDTO con el ID de usuario, las autoridades y la contraseña especificados.
     *
     * @param id          El ID de usuario.
     * @param authorities Las autoridades asociadas con el usuario.
     * @param passwd      La contraseña del usuario.
     */
    public UserDTO(final UserId id, final HashSet<Authority> authorities, final String passwd) {
        this.id = id;
        this.authorities = authorities == null ? new HashSet<>() : new HashSet<>(authorities);
        this.passwd = passwd;
    }

    /**
     * Construye un objeto UserDTO con los mismos atributos que el objeto UserDTO especificado.
     *
     * @param user El objeto UserDTO a copiar.
     */
    public UserDTO(final UserDTO user) {
        this(user == null ? null : new Credentials(user.id, user.authorities, user.passwd));
    }

    /**
     * Establece el ID de usuario para este objeto UserDTO.
     *
     * @param id El ID de usuario.
     * @return Un nuevo objeto UserDTO con el ID de usuario actualizado.
     */
    public UserDTO id(final String id) {
        return this.id(UserId.from(id));
    }

    /**
     * Establece el ID de usuario para este objeto UserDTO.
     *
     * @param id El ID de usuario.
     * @return Un nuevo objeto UserDTO con el ID de usuario actualizado.
     */
    public UserDTO id(final UserId id) {
        return new UserDTO(id, this.authorities, this.passwd);
    }

    /**
     * Establece las autoridades para este objeto UserDTO.
     *
     * @param authorities Las autoridades asociadas con el usuario.
     * @return Un nuevo objeto UserDTO con las autoridades actualizadas.
     */
    public UserDTO authorities(final Collection<Authority> authorities) {
        return new UserDTO(this.id, authorities, this.passwd);
    }

    /**
     * Establece la contraseña para este objeto UserDTO.
     *
     * @param passwd La contraseña del usuario.
     * @return Un nuevo objeto UserDTO con la contraseña actualizada.
     */
    public UserDTO passwd(final String passwd) {
        return new UserDTO(this.id, this.authorities, passwd);
    }

    /**
     * Devuelve una representación de cadena JSON de este objeto UserDTO.
     *
     * @return Una representación de cadena JSON de este objeto UserDTO.
     * @throws RuntimeException Si ocurre un error mientras se procesa la cadena JSON.
     */
    @Override
    public String toString() {
        try {
            return new ObjectMapperProvider().getContext(UserDTO.class).writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convierte este objeto UserDTO en un objeto User persistente.
     *
     * @return Un objeto User persistente convertido a partir de este objeto UserDTO.
     */
    @Override
    public User toPersistent() {
        return User.fromDTO(this);
    }
}
