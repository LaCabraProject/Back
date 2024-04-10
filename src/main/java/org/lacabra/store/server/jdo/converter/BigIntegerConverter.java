package org.lacabra.store.server.jdo.converter;

import jakarta.persistence.Converter;

import javax.jdo.AttributeConverter;
import java.math.BigInteger;

@Converter(autoApply = true)
public final class BigIntegerConverter implements AttributeConverter<BigInteger, Long> {
    @Override
    public Long convertToDatastore(BigInteger bi) {
        return bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 ? Long.MAX_VALUE : bi.longValue();
    }

    @Override
    public BigInteger convertToAttribute(Long l) {
        if (l == null) {
            return null;
        }

        return BigInteger.valueOf(l);
    }
}