package org.lacabra.store.internals.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.lacabra.store.internals.type.id.UserId;

import java.io.IOException;

public final class UserIdSerializer extends JsonSerializer<UserId> {
    @Override
    public void serialize(UserId value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}