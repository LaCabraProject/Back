package org.lacabra.store.client.main;

import org.lacabra.store.client.graphical.dispatcher.WindowDispatcher;
import org.lacabra.store.client.graphical.window.HomeWindow;
import org.lacabra.store.internals.json.validator.JsonSchemaFactory;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import java.io.File;
import java.net.MalformedURLException;

public final class Main {
    public static void main(final String[] args) {
        try {
            for (Class<?> cls : new Class<?>[]{User.class, UserId.class, ObjectId.class, Item.class}) {
                final String name = cls.getSimpleName().toLowerCase();

                final var res = Main.class.getClassLoader().getResource(String.format("schema/%s.json", name));
                if (res == null) {
                    Logger.getLogger().warning(String.format("Could not find JSON schema: %s.json", name));
                } else
                    JsonSchemaFactory.addPreloadedSchema(cls, new File(res.getFile()));
            }

            WindowDispatcher.fromArgs(args).dispatch(HomeWindow.class);
        } catch (IllegalArgumentException e) {
            Logger.getLogger().severe(e);
        } catch (MalformedURLException e) {
            Logger.getLogger().severe(e);

            System.exit(1);
        }
    }
}
