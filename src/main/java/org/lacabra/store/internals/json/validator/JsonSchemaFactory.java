package org.lacabra.store.internals.json.validator;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class JsonSchemaFactory {
    private static final SchemaValidatorsConfig CONFIG = new SchemaValidatorsConfig();

    public static final String PREFIX = "https://github.com/LaCabraProject/";

    private static final Class<?>[] PRELOADED_CLASSES = {User.class, UserId.class, Item.class, ObjectId.class};
    private static final Map<String, String> PRELOADED_SCHEMAS = new HashMap<>();

    static {
        for (Class<?> cls : PRELOADED_CLASSES) {
            final String name = cls.getSimpleName().toLowerCase();

            final var res = JsonSchemaFactory.class.getClassLoader().getResource(String.format("schema/%s.json", name));
            if (res == null) {
                Logger.getLogger().warning(String.format("Could not find schema %s.json", name));

                continue;
            }

            addPreloadedSchema(cls, new File(res.getFile()));
        }
    }

    private static final com.networknt.schema.JsonSchemaFactory jsonSchemaFactory =
            com.networknt.schema.JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012,
                    builder -> builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix(PREFIX, "classpath" +
                            ":schema/")).schemaLoaders(schemaLoaders -> schemaLoaders.schemas(PRELOADED_SCHEMAS)));

    @SuppressWarnings("unchecked")
    private static final Map<Class<?>, JsonSchema> SCHEMAS =
            Map.ofEntries(Arrays.stream(PRELOADED_CLASSES).map((x) -> new AbstractMap.SimpleEntry<>(x,
                    getSchema(x.getSimpleName().toLowerCase()))).toArray(AbstractMap.SimpleEntry[]::new));

    public static JsonSchema getSchema(@NotNull String schema) {
        Objects.requireNonNull(schema);

        return jsonSchemaFactory.getSchema(SchemaLocation.of(PREFIX + (schema.endsWith(".json") ? schema : (schema +
                ".json"))), CONFIG);
    }

    public static JsonSchema getSchema(@NotNull Class<?> cls) {
        return SCHEMAS.get(Objects.requireNonNull(cls));
    }

    public static void addPreloadedSchema(@NotNull Class<?> cls, @NotNull String schema) {
        final String name = cls.getSimpleName().toLowerCase();

        PRELOADED_SCHEMAS.put(String.format("%s%s.json", PREFIX, name), schema);
        PRELOADED_SCHEMAS.put(String.format("classpath:///schema/%s.json", name), schema);
    }

    public static void addPreloadedSchema(@NotNull Class<?> cls, File schema) {
        if (schema == null)
            return;

        try (final BufferedReader br = new BufferedReader(new FileReader(schema))) {
            StringBuilder str = new StringBuilder();

            for (String l = br.readLine(), ls = System.lineSeparator(); l != null; l = br.readLine()) {
                str.append(l);
                str.append(ls);
            }

            addPreloadedSchema(cls, str.toString());
        } catch (IOException e) {
            Logger.getLogger().severe(e);
        }
    }
}
