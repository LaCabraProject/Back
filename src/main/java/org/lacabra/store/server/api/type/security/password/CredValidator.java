package org.lacabra.store.server.api.type.security.password;

import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.security.exception.AuthenticationException;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.dao.UserDAO;

public class CredValidator {
    public static User validate(Credentials creds) {
        if (creds == null)
            return null;

        return validate(creds.id(), creds.passwd());
    }

    public static User validate(UserId id, String passwd) {
        return validate(id == null ? null : id.get(), passwd);
    }

    public static User validate(String id, String passwd) {
        final var dao = UserDAO.getInstance();
        final var user = dao.findOne(new User(id));

        if (user == null) {
            throw new AuthenticationException("User not found.");
        }

        if (!PasswordHasher.check(passwd, user.passwd())) {
            throw new AuthenticationException("Bad credentials.");
        }

        return user;
    }
}
