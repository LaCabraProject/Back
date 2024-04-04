package org.lacabra.store.server.api.type.security.password;

import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.security.exception.AuthenticationException;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jpa.dao.UserDAO;

import java.util.Optional;

public final class CredValidator {

    private PasswordHasher hasher;

    public User validate(Credentials creds) {
        return validate(creds.id(), creds.passwd());
    }

    public User validate(UserId id, String passwd) {
        return validate(Optional.of(id).get().get(), passwd);
    }

    public User validate(String id, String passwd) {
        User user = UserDAO.getInstance().find(new User(new Credentials(id, passwd)));

        if (user == null) {
            throw new AuthenticationException("Bad credentials.");
        }

        if (!hasher.check(passwd, user.passwd())) {
            throw new AuthenticationException("Bad credentials.");
        }

        return user;
    }
}
