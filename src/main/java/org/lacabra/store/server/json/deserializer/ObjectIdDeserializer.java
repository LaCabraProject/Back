package org.lacabra.store.server.json.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.lacabra.store.server.api.type.id.ObjectId;

import java.io.IOException;

public final class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonParseException {
        try {
            return ObjectId.from(jp.getNumberValueExact());
        } catch (JsonParseException e) {
            return ObjectId.from(jp.getValueAsString());
        }
    }
}