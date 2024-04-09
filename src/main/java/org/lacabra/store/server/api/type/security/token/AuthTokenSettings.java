package org.lacabra.store.server.api.type.security.token;

import jakarta.enterprise.context.Dependent;
import org.lacabra.store.server.api.security.service.token.AuthTokenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Dependent
public class AuthTokenSettings {
    private static String secret;

    private static Long clockSkew;

    private static String audience;

    private static String issuer;

    private static String authoritiesClaimName;

    private static String refreshCountClaimName;

    private static String refreshLimitClaimName;

    static {
        Properties properties = new Properties();
        try (InputStream is = AuthTokenUtils.class.getResourceAsStream("/application.properties")) {
            properties.load(is);

            AuthTokenSettings.secret = String.valueOf(properties.get("authentication.jwt.secret"));
            AuthTokenSettings.clockSkew =
                    Long.valueOf(String.valueOf(properties.get("authentication.jwt.clockSkew")));
            AuthTokenSettings.audience = String.valueOf(properties.get("authentication.jwt.audience"));
            AuthTokenSettings.issuer = String.valueOf(properties.get("authentication.jwt.issuer"));
            AuthTokenSettings.authoritiesClaimName = String.valueOf(properties.get("authentication.jwt.claimNames" +
                    ".authorities"));
            AuthTokenSettings.refreshCountClaimName = String.valueOf(properties.get("authentication.jwt.claimNames" +
                    ".refreshCount"));
            AuthTokenSettings.refreshLimitClaimName = String.valueOf(properties.get("authentication.jwt.claimNames" +
                    ".refreshLimit"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String secret() {
        return AuthTokenSettings.secret;
    }

    public static Long clockSkew() {
        return AuthTokenSettings.clockSkew;
    }

    public static String audience() {
        return AuthTokenSettings.audience;
    }

    public static String issuer() {
        return AuthTokenSettings.issuer;
    }

    public static String authoritiesClaimName() {
        return AuthTokenSettings.authoritiesClaimName;
    }

    public static String refreshCountClaimName() {
        return AuthTokenSettings.refreshCountClaimName;
    }

    public static String refreshLimitClaimName() {
        return AuthTokenSettings.refreshLimitClaimName;
    }
}
