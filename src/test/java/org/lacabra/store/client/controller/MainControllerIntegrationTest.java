package org.lacabra.store.client.controller;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.experimental.categories.Category;

import io.jsonwebtoken.lang.Collections;
import org.lacabra.store.client.dto.ItemAssembler;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.dto.UserAssembler;
import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@Category(IntegrationTest.class)
public class MainControllerIntegrationTest {

    HttpClient client;
    @BeforeClass
    public void launchAPI() {
        Runtime.getRuntime().exec(new String [] { "mvn", "jetty:run", "-f pom.xml" });
    }

    @Before
    public void setUp() {
        client = HttpClient.newHttpClient();
    }
    @Test
    public void testGetItems() {
        List<Item> items = new ArrayList<>();
        List<ItemDTO> itemDTOs = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/item/all"))).GET().build();
        try {
            HttpResponse<String> respuesta = client.send(request, BodyHandlers.ofString());
            Logger.getLogger().info(respuesta.body());
            assertTrue(items[0].id() == 0);
            assertTrue(items[0].type() == ItemType.Clothing);
            assertTrue(items[0].name() == "Camiseta de grupo genérico");
            assertTrue(items[0].price() == 25);
            assertTrue(items[0].discount() == 30);
            assertTrue(items[0].stock() == 1000);
            assertTrue(items[0].parent().id().toString() == "mikel");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
