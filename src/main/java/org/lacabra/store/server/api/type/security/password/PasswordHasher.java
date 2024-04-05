package org.lacabra.store.server.api.type.security.password;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordHasher {
    public String hash(String passwd) {
        return BCrypt.hashpw(passwd, BCrypt.gensalt());
    }

    public boolean check(String passwd, String hash) {
        return switch (hash) {
            case String h when h.startsWith("$2a$") -> BCrypt.checkpw(passwd, hash);
            case null, default -> false;
        };
    }
}
