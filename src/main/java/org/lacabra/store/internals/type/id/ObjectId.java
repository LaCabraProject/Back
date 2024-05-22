/**
 * @file ObjectId.java
 * @brief Definición de la clase ObjectId.
 *
 * Este archivo contiene la declaración de la clase ObjectId que se usa para representar y manejar identificadores de objetos.
 */

package org.lacabra.store.internals.type.id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.lacabra.store.internals.json.deserializer.ObjectIdDeserializer;
import org.lacabra.store.internals.json.serializer.ObjectIdSerializer;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.jdo.dao.DAO;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;

/**
 * @brief Clase final para representar un identificador de objeto.
 *
 * La clase ObjectId proporciona métodos para validar, normalizar y generar identificadores de objetos.
 */
@JsonSerialize(using = ObjectIdSerializer.class)
@JsonDeserialize(using = ObjectIdDeserializer.class)
public final class ObjectId implements Serializable {
    /**
     * Expresión regular para validar ObjectId.
     */
    public final static Pattern REGEX = Pattern.compile("^[0-9a-f]{1,24}$");
    
    /**
     * Valor mínimo de ObjectId.
     */
    public final static ObjectId MIN = new ObjectId(0);
    
    /**
     * Valor máximo de ObjectId.
     */
    public final static ObjectId MAX;
    
    @Serial
    private static final long serialVersionUID = 1L;

    static {
        final int bytes = 12;
        byte[] arr = new byte[bytes];
        for (int i = 0; i < bytes; arr[i++] = (byte) 0xFF) ;
        MAX = new ObjectId(new BigInteger(1, arr));
    }

    /**
     * Valor interno del ObjectId.
     */
    private final BigInteger value;

    /**
     * Constructor privado que inicializa el ObjectId con un valor numérico.
     *
     * @param value Valor numérico para inicializar el ObjectId.
     */
    private ObjectId(Number value) {
        this.value = NumberToBigInteger(value);
    }

    /**
     * Convierte un número a BigInteger.
     *
     * @param number Número a convertir.
     * @return BigInteger equivalente al número proporcionado.
     */
    private static BigInteger NumberToBigInteger(Number number) {
        return switch (number) {
            case Byte b -> BigInteger.valueOf(b);
            case Short s -> BigInteger.valueOf(s);
            case Integer i -> BigInteger.valueOf(i);
            case AtomicInteger ai -> BigInteger.valueOf(ai.get());
            case Long l -> BigInteger.valueOf(l);
            case AtomicLong al -> BigInteger.valueOf(al.get());
            case LongAccumulator la -> BigInteger.valueOf(la.get());
            case LongAdder la -> BigInteger.valueOf(la.sum());
            case BigInteger bi -> bi;
            case Float f -> NumberToBigInteger(BigDecimal.valueOf((double) f));
            case Double d -> NumberToBigInteger(BigDecimal.valueOf(d));
            case DoubleAccumulator da -> NumberToBigInteger(BigDecimal.valueOf(da.get()));
            case DoubleAdder da -> NumberToBigInteger(BigDecimal.valueOf(da.sum()));
            case BigDecimal dec -> {
                BigInteger i = dec.toBigInteger();
                if (dec.compareTo(new BigDecimal(i)) != 0)
                    yield null;
                yield i;
            }
            case null, default -> null;
        };
    }

    /**
     * Verifica si una cadena es un ObjectId válido.
     *
     * @param id Cadena a verificar.
     * @return true si la cadena es un ObjectId válido, de lo contrario false.
     */
    public static boolean is(String id) {
        if (id == null) return false;
        return ObjectId.REGEX.matcher(id).matches();
    }

    /**
     * Verifica si un número es un ObjectId válido.
     *
     * @param id Número a verificar.
     * @return true si el número es un ObjectId válido, de lo contrario false.
     */
    public static boolean is(Number id) {
        return switch (NumberToBigInteger(id)) {
            case null -> false;
            case BigInteger i when i.compareTo(ObjectId.MIN.value) < 0 -> false;
            case BigInteger i when i.compareTo(ObjectId.MAX.value) > 0 -> false;
            default -> true;
        };
    }

    /**
     * Verifica si un ObjectId es válido.
     *
     * @param id ObjectId a verificar.
     * @return true si el ObjectId es válido, de lo contrario false.
     */
    public static boolean is(ObjectId id) {
        return id != null;
    }

    /**
     * Crea un ObjectId a partir de un número.
     *
     * @param id Número a partir del cual crear el ObjectId.
     * @return Nuevo ObjectId, o null si el número no es válido.
     */
    public static ObjectId from(Number id) {
        if (!ObjectId.is(id)) {
            return null;
        }
        return new ObjectId(id);
    }

    /**
     * Crea un ObjectId a partir de una cadena.
     *
     * @param id Cadena a partir de la cual crear el ObjectId.
     * @return Nuevo ObjectId, o null si la cadena no es válida.
     */
    public static ObjectId from(String id) {
        if (!ObjectId.is(id)) {
            return null;
        }
        return new ObjectId(new BigInteger(id, 16));
    }

    /**
     * Crea un ObjectId a partir de otro ObjectId.
     *
     * @param id ObjectId a partir del cual crear el nuevo ObjectId.
     * @return Nuevo ObjectId, o null si el ObjectId proporcionado no es válido.
     */
    public static ObjectId from(ObjectId id) {
        if (!ObjectId.is(id)) {
            return null;
        }
        return new ObjectId(id.value);
    }

    /**
     * Normaliza un número como cadena de ObjectId.
     *
     * @param id Número a normalizar.
     * @return Cadena normalizada del ObjectId, o null si el número no es válido.
     */
    public static String normalize(Number id) {
        var oid = ObjectId.from(id);
        if (oid == null)
            return null;
        return oid.normalize();
    }

    /**
     * Normaliza una cadena como cadena de ObjectId.
     *
     * @param id Cadena a normalizar.
     * @return Cadena normalizada del ObjectId, o null si la cadena no es válida.
     */
    public static String normalize(String id) {
        var oid = ObjectId.from(id);
        if (oid == null)
            return null;
        return oid.normalize();
    }

    /**
     * Normaliza un ObjectId como cadena.
     *
     * @param id ObjectId a normalizar.
     * @return Cadena normalizada del ObjectId, o null si el ObjectId es null.
     */
    public static String normalize(ObjectId id) {
        if (id == null)
            return null;
        return id.normalize();
    }

    /**
     * Genera un ObjectId aleatorio para una clase específica.
     *
     * @param cls Clase para la cual generar un ObjectId aleatorio.
     * @return Nuevo ObjectId aleatorio, o null si no se puede generar.
     */
    public static ObjectId random(Class<?> cls) {
        var dao = DAO.getInstance(cls);
        if (dao == null)
            return null;

        try {
            var constructor = cls.getConstructor(ObjectId.class);
            final int MAX_ITERS = 1_000_000;
            final String hex = "0123456789abcdef";
            final Set<ObjectId> tried = new HashSet<>();

            for (int i = 0; i < MAX_ITERS; ) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < 24; j++) {
                    builder.append(hex.charAt((int) (Math.random() * 100) % hex.length()));
                }

                String str = builder.toString();
                ObjectId id = ObjectId.from(str);
                if (id == null || tried.contains(id)) {
                    continue;
                }

                tried.add(id);
                i++;

                Object o = constructor.newInstance(id);
                if (dao.findOne(o) == null)
                    return id;
            }
        } catch (NoSuchMethodException e) {
            Logger.getLogger().warning("No ObjectId based constructor found for class " + cls.getName());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Logger.getLogger().severe(e);
        }
        return null;
    }

    /**
     * Convierte el ObjectId a un valor entero.
     *
     * @return Valor entero del ObjectId.
     */
    public BigInteger toInteger() {
        return this.value;
    }

    /**
     * Normaliza el ObjectId a una cadena de longitud 24.
     *
     * @return Cadena normalizada del ObjectId.
     */
    public String normalize() {
        if (this.value == null)
            return null;

        var str = this.toString();
        return "0".repeat(24 - str.length()) + str;
    }

    @Override
    public String toString() {
        return this.value.toString(16);
    }

    @Override
    public boolean equals(Object o) {
        return switch (o) {
            case String s -> this.equals(ObjectId.from(s));
            case Number n -> this.equals(ObjectId.from(n));
            case ObjectId id -> Objects.equals(this.normalize(), id.normalize());
            case null, default -> false;
        };
    }

    @Override
    public int hashCode() {
        return this.value == null ? 0 : this.value.hashCode();
    }
}
