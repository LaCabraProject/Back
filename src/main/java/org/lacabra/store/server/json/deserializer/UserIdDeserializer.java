package org.lacabra.store.server.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.lacabra.store.server.api.type.id.UserId;

import java.io.IOException;

public final class UserIdDeserializer extends JsonDeserializer<UserId> {
    @Override
    public UserId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return UserId.from(jp.getValueAsString());
    }
}