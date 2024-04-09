package org.lacabra.store.server.json.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.lacabra.store.server.api.type.id.ObjectId;

import java.io.IOException;

public final class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonParseException {
        JsonNode node = jp.readValueAsTree();

        if (node.isNumber())
            return ObjectId.from(jp.getNumberValueExact());

        if (node.isTextual())
            return ObjectId.from(jp.getValueAsString());

        return null;
    }
}