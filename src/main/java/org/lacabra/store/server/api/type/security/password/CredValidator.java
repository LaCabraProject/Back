package org.lacabra.store.server.api.type.security.password;

import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.security.exception.AuthenticationException;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.jdo.dao.UserDAO;

import java.util.Optional;

public class CredValidator {
    public static User validate(Credentials creds) {
        return validate(creds.id(), creds.passwd());
    }

    public static User validate(UserId id, String passwd) {
        return validate(Optional.of(id).get().get(), passwd);
    }

    public static User validate(String id, String passwd) {
        var dao = UserDAO.getInstance();

        User user = dao.findOne(dao.getQuery("FindUser"), new User(new Credentials(id)));

        if (user == null) {
            throw new AuthenticationException("User not found.");
        }

        if (!PasswordHasher.check(passwd, user.passwd())) {
            throw new AuthenticationException("Bad credentials.");
        }

        return user;
    }
}
