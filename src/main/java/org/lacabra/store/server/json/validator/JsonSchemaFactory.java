package org.lacabra.store.server.json.validator;

import com.networknt.schema.*;
import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

public final class JsonSchemaFactory {
    public static final String PREFIX = "https://github.com/LaCabraProject/";
    private static final SchemaValidatorsConfig CONFIG = new SchemaValidatorsConfig();
    private static final com.networknt.schema.JsonSchemaFactory jsonSchemaFactory =
            com.networknt.schema.JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
                    builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix(PREFIX, "classpath:schema/"))
            );
    private static final Map<Class<?>, JsonSchema> SCHEMAS = Map.of(User.class, getSchema("user"), UserId.class,
            getSchema("userid"), Item.class, getSchema("item"), ObjectId.class, getSchema("objectid"));

    static {
        CONFIG.setPathType(PathType.JSON_POINTER);
        CONFIG.setEcma262Validator(true);
    }

    public static JsonSchema getSchema(@NotNull String schema) {
        Objects.requireNonNull(schema);

        return jsonSchemaFactory.getSchema(SchemaLocation.of(PREFIX + (schema.endsWith(".json") ? schema :
                        (schema + ".json"))),
                CONFIG);
    }

    public static JsonSchema getSchema(@NotNull Class<?> cls) {
        return SCHEMAS.get(Objects.requireNonNull(cls));
    }
}
