package org.lacabra.store.server.jdo.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.type.security.password.PasswordHasher;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public final class UserDAO extends DAO<User> {
    private static UserDAO instance;

    static {
        var dao = UserDAO.getInstance();
        DAO.instances.put(User.class, dao);

        try (InputStream in = UserDAO.class.getClassLoader().getResourceAsStream("data/users.json")) {
            if (in != null) {
                ObjectMapper mapper = new ObjectMapperProvider().getContext(User[].class);
                dao.store(mapper.readValue(in, User[].class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private UserDAO() throws NoSuchMethodException {
        super(User.class, "FindUser");
    }

    public static UserDAO getInstance() {
        if (UserDAO.instance == null) {
            try {
                UserDAO.instance = new UserDAO();
                DAO.instances.put(User.class, UserDAO.instance);
            } catch (NoSuchMethodException e) {
                Logger.getLogger().severe(e);
            }
        }

        return UserDAO.instance;
    }

    @Override
    protected UserDAO instance() {
        return UserDAO.instance;
    }

    @Override
    public boolean store(User user) {
        return super.store(new User(new Credentials(user.id(), user.authorities(), user.passwd().startsWith("$2a$") ?
                user.passwd() : PasswordHasher.hash(user.passwd())),
                user.data()));
    }
}