package org.lacabra.store.server.api.type.security.password;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public static String hash(String passwd) {
        if (passwd == null)
            return null;

        return BCrypt.hashpw(passwd, BCrypt.gensalt());
    }

    public static boolean check(String passwd, String hash) {
        return switch (hash) {
            case String h when h.startsWith("$2a$") -> BCrypt.checkpw(passwd, hash);
            case null, default -> false;
        };
    }
}
