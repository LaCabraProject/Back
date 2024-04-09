package data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

public class MainController {
	private static String ServerName = "http://localhost:8080";
	
	public ArrayList<Item> ReceiveItems() {
		ArrayList<Item> items = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/items"))).GET().build();
		
		try {
			HttpResponse<String> respuesta=client.send(request, BodyHandlers.ofString());
	        System.out.println(respuesta.body());
	        
	        // deserializar un json y devolver array de Items
	        //
		} catch (IOException | InterruptedException e) {
	       e.printStackTrace();
		}
		return items;
	}
	public ArrayList<User> ReceiveUsers() {
		ArrayList<User> users = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/users"))).GET().build();
		try {
			HttpResponse<String> respuesta=client.send(request, BodyHandlers.ofString());
	        System.out.println(respuesta.body());
	        
	        // deserializar un json y devolver array de Usuarios
	        //
		} catch (IOException | InterruptedException e) {
	       e.printStackTrace();
		}
		return users;
	}
	public void PutItem(Item item) {
        // serializar un item
		//
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/items"))).PUT(HttpRequest.BodyPublishers.noBody()).build();
		try {
			HttpResponse<String> respuesta=client.send(request, BodyHandlers.ofString());
	        System.out.println(respuesta.body());
	        
		} catch (IOException | InterruptedException e) {
	       e.printStackTrace();
		}
	}
	public void PutUser(User user) {
		// serializar un usuario
		//
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ServerName.concat("/users"))).PUT(HttpRequest.BodyPublishers.noBody()).build();
		try {
			HttpResponse<String> respuesta=client.send(request, BodyHandlers.ofString());
	        System.out.println(respuesta.body());
	        
		} catch (IOException | InterruptedException e) {
	       e.printStackTrace();
		}
	}
}
