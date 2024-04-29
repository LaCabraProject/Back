package org.lacabra.store.client.controller;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.mock;

public class MainControllerTest {

    @Test
    public void testGetItems() {
        HttpClient mockedClient = mock(HttpClient.class);
        String jsonResponse = "{\n" +
                "  \"id\": 0,\n" +
                "  \"type\": \"clothing\",\n" +
                "  \"name\": \"Camiseta de grupo genérico\",\n" +
                "  \"price\": 25,\n" +
                "  \"discount\": 30,\n" +
                "  \"stock\": 1000,\n" +
                "  \"parent\": \"mikel\"\n" +
                "}";

    }
}
