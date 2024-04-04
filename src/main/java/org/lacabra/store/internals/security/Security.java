package org.lacabra.store.internals.security;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public final class Security {
    @SuppressWarnings({"removal", "deprecation"})
    public static void init() throws URISyntaxException, NoSuchAlgorithmException {
        java.security.Policy.setPolicy(java.security.Policy.getInstance("JavaPolicy", new java.security.URIParameter(
                Objects.requireNonNull(Security.class.getClassLoader().getResource("java.policy")).toURI())));
        java.security.Security.setProperty("policy.provider", "com.sun.security.provider.PolicyFile");
        java.security.Policy.getPolicy().refresh();

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("sh", "-c", "clear").inheritIO().start().waitFor();
        } catch (Exception e) {
        }

        System.setProperty("stdout.encoding", "UTF-8");
        System.setProperty("file.encoding", "UTF-8");
    }
}