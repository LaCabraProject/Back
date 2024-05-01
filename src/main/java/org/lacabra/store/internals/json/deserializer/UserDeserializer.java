package org.lacabra.store.internals.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.lacabra.store.internals.json.exception.JsonSchemaComplianceException;
import org.lacabra.store.internals.json.validator.JsonSchemaValidator;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();

        try {
            JsonSchemaValidator.USER.validate(node);
        } catch (JsonSchemaComplianceException e) {
            Logger.getLogger().warning(e);
            return null;
        }

        JsonNode node2;

        UserId id = new ObjectMapperProvider().getContext(UserId.class).treeToValue(node.get("id"), UserId.class);

        String passwd = null;
        node2 = node.get("passwd");
        if (!(node2 == null || node2.isNull())) {
            passwd = node2.asText();
        }

        var authorities = new HashSet<Authority>();

        node2 = node.get("authorities");
        if (!(node2 == null || node2.isNull())) {
            List<String> strs = new ArrayList<>();

            if (node2.isArray())
                for (final JsonNode n : node2) {
                    strs.add(n.asText());
                }
            else
                strs.add(node2.asText());

            authorities.addAll(strs.stream().map(Authority::parse).toList());
        }

        return new User(new Credentials(id, authorities, passwd));
    }
}