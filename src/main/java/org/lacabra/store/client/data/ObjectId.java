package org.lacabra.store.client.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;

public final class ObjectId {
    public final static Pattern regex = Pattern.compile("^[0-9a-f]{1,24}$");
    public final static ObjectId MIN = new ObjectId(0);
    public final static ObjectId MAX;

    static {
        final int bytes = 12;

        byte[] arr = new byte[bytes];
        for (int i = 0; i < bytes; arr[i++] = (byte) 0xFF) ;

        MAX = new ObjectId(new BigInteger(1, arr));
    }

    final BigInteger value;

    private ObjectId(Number value) {
        this.value = NumberToBigInteger(value);
    }

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
                BigInteger i = dec.toBigIntegerExact();

                if (dec.compareTo(new BigDecimal(i)) != 0)
                    yield null;

                yield i;
            }

            case null, default -> null;
        };
    }

    public static boolean is(String id) {
        if (id == null) return false;

        return ObjectId.regex.matcher(id).matches();
    }

    public static boolean is(Number id) {
        return switch (NumberToBigInteger(id)) {
            case null -> false;
            case BigInteger i when i.compareTo(ObjectId.MIN.value) < 0 -> false;
            case BigInteger i when i.compareTo(ObjectId.MAX.value) > 0 -> false;
            default -> true;
        };
    }

    public static boolean is(ObjectId id) {
        return id != null;
    }

    public static ObjectId from(Number id) {
        if (!ObjectId.is(id)) {
            return null;
        }

        return new ObjectId(id);
    }

    public static ObjectId from(String id) {
        if (!ObjectId.is(id)) {
            return null;
        }

        return new ObjectId(new BigInteger(id, 16));
    }

    public static ObjectId from(ObjectId id) {
        if (!ObjectId.is(id)) {
            return null;
        }

        return new ObjectId(id.value);
    }

    public static String normalize(Number id) {
        var oid = ObjectId.from(id);
        if (oid == null)
            return null;

        return oid.normalize();
    }

    public static String normalize(String id) {
        var oid = ObjectId.from(id);
        if (oid == null)
            return null;

        return oid.normalize();
    }

    public static String normalize(ObjectId id) {
        if (id == null)
            return null;

        return id.normalize();
    }

    public BigInteger toInteger() {
        return this.value;
    }

    public String normalize() {
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
            case ObjectId id -> this.normalize().equals(id.value);
            case null, default -> false;
        };
    }
}
