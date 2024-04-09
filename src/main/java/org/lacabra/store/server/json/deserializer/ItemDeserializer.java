package org.lacabra.store.server.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class ItemDeserializer extends JsonDeserializer<Item> {
    private static final ObjectMapper mapper = new ObjectMapperProvider().getContext(Item.class);

    @Override
    public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();

        if (node.isNull())
            return null;

        JsonNode node2;

        ObjectId id = new ObjectMapperProvider().getContext(ObjectId.class).treeToValue(node.get("id"), ObjectId.class);
        {
            if (node2 == null || node2.isNull())
                throw new RuntimeException("\"id\" is null.");

            if (!node2.isTextual())
                throw new RuntimeException("\"id\" is not a string.");
        }

        String passwd = null;

        node2 = node.get("passwd");
        if (!(node2 == null || node2.isNull())) {
            if (!node2.isTextual())
                throw new RuntimeException("\"passwd\" is not a string.");

            passwd = node2.asText();
        }

        var authorities = new HashSet<Authority>();

        node2 = node.get("authorities");
        if (!(node2 == null || node2.isNull())) {
            List<String> strs = new ArrayList<>();

            if (node2.isTextual())
                strs.add(node2.asText());

            else {
                if (!node2.isArray())
                    throw new RuntimeException("\"authorities\" is not an array.");

                for (final JsonNode n : node2) {
                    if (!n.isTextual())
                        throw new RuntimeException("Authority is not a string.");

                    strs.add(n.asText());
                }
            }

            for (final String str : strs) {
                var auth = Authority.parse(str);
                if (auth == null)
                    throw new RuntimeException("Not a valid authority.");

                authorities.add(auth);
            }
        }

        return new User(new Credentials(id, authorities, passwd));
    }
}