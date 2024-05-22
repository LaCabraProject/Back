package org.lacabra.store.internals.type.id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.internals.json.deserializer.UserIdDeserializer;
import org.lacabra.store.internals.json.serializer.UserIdSerializer;
import org.lacabra.store.internals.logging.Logger;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @brief Clase para representar un identificador de usuario.
 *
 * La clase UserId proporciona métodos para validar, crear y comparar identificadores de usuarios.
 */
@JsonSerialize(using = UserIdSerializer.class)
@JsonDeserialize(using = UserIdDeserializer.class)
public final class UserId implements Serializable {
    /**
     * @brief Expresión regular para validar UserId.
     */
    public final static Pattern REGEX;

    /**
     * @brief Lista de identificadores no válidos.
     */
    public static final List<String> Invalid = List.of("all");

    @Serial
    private static final long serialVersionUID = 1L;

    static {
        final String[] chars = {"a-zA-Z0-9", // ascii
                "\\u00c0-\\u00d6\\u00d8-\\u00f6\\u0080-\\u00ff\\u0100-\\u017f\\u0180-\\u024f\\u1e00-\\u1eff", // latin
                "\\u0370-\\u03ff\\u1f00-\\u1fff", // greek
                "\\u0400-\\u04ff\\u0500\\u052f" // cyrillic
        };

        REGEX = Pattern.compile(String.format("^(?!^%s$)(?=.{3,30}$)^([%s]+[-_\\.]?)+$",
                Invalid.stream().map(x -> String.format("(?:%s)", x)).collect(Collectors.joining("|")),
                Arrays.stream(chars).flatMap(x -> {
                    final var m = Pattern.compile("(\\\\u.{1,4}-\\\\u.{1,4})|(.-.)").matcher(x);

                    final var ret = new ArrayList<String>();
                    for (; m.find(); )
                        ret.add(m.group());

                    return ret.stream();
                }).filter(Objects::nonNull).sorted((a, b) -> {
                    String[] match = new String[2];

                    Pattern r = Pattern.compile("\\\\u(.{1,4})\\\\.+");
                    Matcher[] matcher = new Matcher[]{r.matcher(a), r.matcher(b)};

                    for (int i = 0; i < matcher.length; i++) {
                        if (matcher[i].find()) match[i] = matcher[i].group();
                    }

                    return (match[0] == null ? a.codePointAt(0) : Integer.parseInt(match[0], 16)) - (match[1] == null ?
                            b.codePointAt(0) : Integer.parseInt(match[1], 16));
                }).collect(Collectors.joining())));
    }

    /**
     * @brief Valor interno del UserId.
     */
    private final String id;

    /**
     * Constructor privado que inicializa el UserId con una cadena.
     *
     * @param id Cadena para inicializar el UserId.
     */
    private UserId(String id) {
        this.id = id;
    }

    /**
     * Crea un UserId a partir de una cadena.
     *
     * @param id Cadena a partir de la cual crear el UserId.
     * @return Nuevo UserId, o null si la cadena no es válida.
     */
    public static UserId from(String id) {
        if (UserId.is(id))
            return new UserId(id.trim());

        return null;
    }

    /**
     * Crea un UserId a partir de otro UserId.
     *
     * @param id UserId a partir del cual crear el nuevo UserId.
     * @return Nuevo UserId.
     */
    public static UserId from(UserId id) {
        if (UserId.is(id))
            return new UserId(id.id);

        return null;
    }

    /**
     * Verifica si un UserId es válido.
     *
     * @param id UserId a verificar.
     * @return true si el UserId es válido, de lo contrario false.
     */
    public static boolean is(String id) {
        if (id == null)
            return false;

        return REGEX.matcher(id.trim()).matches();
    }

    /**
     * Obtiene la representación de cadena del UserId.
     *
     * @return Cadena que representa el UserId.
     */
    public static boolean is(UserId id) {
        return id != null;
    }

    public String get() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        return switch (o) {
            case UserId i -> this.id.equals(i.id);
            case String s -> this.id.equals(s);
            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
