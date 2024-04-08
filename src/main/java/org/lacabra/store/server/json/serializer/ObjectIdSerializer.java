package org.lacabra.store.server.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.lacabra.store.server.api.type.id.ObjectId;

import java.io.IOException;

public final class ObjectIdSerializer extends JsonSerializer<ObjectId> {
    @Override
    public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}