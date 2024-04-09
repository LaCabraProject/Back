package org.lacabra.store.client.Controller;

import org.lacabra.store.server.api.route.stats.Route;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.User;

import io.jsonwebtoken.lang.Collections;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private static String ServerName = "http://localhost:8080";

    public List<Item> ReceiveItems() {
        List<Item> items = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/items"))).GET().build();

        try {
            HttpResponse<String> respuesta = client.send(request, BodyHandlers.ofString());
            System.out.println(respuesta.body());

            // deserializar un json y devolver array de Items
            items =
                    Collections.arrayToList(new ObjectMapperProvider().getContext(Item[].class).readValue(respuesta.body(), Item[].class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<User> ReceiveUsers() {
        List<User> users = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/users"))).GET().build();
        try {
            HttpResponse<String> respuesta = client.send(request, BodyHandlers.ofString());
            System.out.println(respuesta.body());

            // deserializar un json y devolver array de Usuarios
            users =
                    Collections.arrayToList(new ObjectMapperProvider().getContext(User[].class).readValue(respuesta.body(), User[].class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void PutItem(Item item) {
        // serializar un item
        //

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/items"))).PUT(HttpRequest.BodyPublishers.noBody()).build();
        try {
            HttpResponse<String> respuesta = client.send(request, BodyHandlers.ofString());
            System.out.println(respuesta.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void PutUser(User user) {
        // serializar un usuario
        //

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/users"))).PUT(HttpRequest.BodyPublishers.noBody()).build();
        try {
            HttpResponse<String> respuesta = client.send(request, BodyHandlers.ofString());
            System.out.println(respuesta.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
