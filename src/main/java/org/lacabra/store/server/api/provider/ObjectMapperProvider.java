package org.lacabra.store.server.api.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.json.deserializer.ItemArrayDeserializer;
import org.lacabra.store.server.json.deserializer.ItemDeserializer;
import org.lacabra.store.server.json.deserializer.UserArrayDeserializer;
import org.lacabra.store.server.json.deserializer.UserDeserializer;

import javax.validation.constraints.NotNull;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final Map<Class<?>, ObjectMapper> mappers = new HashMap<>();

    public ObjectMapperProvider() {
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule mod = new SimpleModule();
        mod.addDeserializer(User[].class, new UserArrayDeserializer());
        mod.addDeserializer(User.class, new UserDeserializer());
        mod.addDeserializer(Item[].class, new ItemArrayDeserializer());
        mod.addDeserializer(Item.class, new ItemDeserializer());

        mapper.registerModule(mod);
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParameterNamesModule());

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }

    @Override
    public ObjectMapper getContext(@NotNull Class<?> type) {
        if (type == null)
            return null;

        if (mappers.containsKey(type))
            return mappers.get(type);

        final ObjectMapper m = createObjectMapper();
        mappers.put(type, m);

        return m;
    }
}