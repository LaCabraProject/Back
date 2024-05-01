package org.lacabra.store.internals.maven;

import java.io.IOException;
import java.util.Arrays;

public final class ClassRunner {
    public static void run(final Class<?> cls) throws IOException, NoSuchMethodException, InterruptedException {
        if (Arrays.stream(cls.getDeclaredMethods()).noneMatch(x -> x.getName().equals("main")))
            throw new NoSuchMethodException("No main method in class " + cls.getName());

        final var b = new ProcessBuilder().command("mvn", "clean:clean", "resources:resources", "compiler:compile",
                "exec:java", String.format("-Dexec.mainClass=\"%s\"", cls.getName()), "-Dexec.args=\"run\"", "-f",
                "pom.xml").inheritIO();

        b.environment().put("JAVA_HOME",
                System.getProperties().getProperty("java.home"));

        final var p = b.start();
        System.err.println(new String(p.getErrorStream().readAllBytes()));
        System.out.println(new String(p.getInputStream().readAllBytes()));

        System.exit(b.start().waitFor());
    }
}
