package org.lacabra.store.client.controller;


import org.glassfish.jersey.http.ResponseStatus;
import org.lacabra.store.client.dto.ItemAssembler;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.RequestMethod;
import org.lacabra.store.server.api.provider.ObjectMapperProvider;
import org.lacabra.store.server.api.type.item.Item;
import org.lacabra.store.server.api.type.user.Credentials;

import javax.net.ssl.SSLSession;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MainController {
    public final static Pattern URL_REGEX =
            Pattern.compile("^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;" + "]*[-a-zA-Z0-9" + "+&@#/%=~_|]");

    public final static int PORT_MIN = 0;
    public final static int PORT_MAX = 65535;

    public final static String DEFAULT_HOSTNAME = "http://localhost";
    public final static int DEFAULT_PORT = 8080;
    public final static String DEFAULT_ENDPOINT = "/api";

    private String hostname;
    private Integer port;
    private String endpoint;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public final static Function<String, HttpResponse<String>> RequestError =
            body -> new HttpResponse<>() {
                @Override
                public int statusCode() {
                    return 0;
                }

                @Override
                public HttpRequest request() {
                    return null;
                }

                @Override
                public Optional<HttpResponse<String>> previousResponse() {
                    return Optional.empty();
                }

                @Override
                public HttpHeaders headers() {
                    return null;
                }

                @Override
                public String body() {
                    return body;
                }

                @Override
                public Optional<SSLSession> sslSession() {
                    return Optional.empty();
                }

                @Override
                public URI uri() {
                    return null;
                }

                @Override
                public HttpClient.Version version() {
                    return null;
                }
            };

    private String token;

    public MainController() {
        super();

        this.hostname = DEFAULT_HOSTNAME;
        this.port = DEFAULT_PORT;
        this.endpoint = DEFAULT_ENDPOINT;
    }

    public MainController(String hostname) throws MalformedURLException {
        this(hostname, (String) null, null);
    }

    public MainController(String hostname, Integer port) throws MalformedURLException {
        this(hostname, port, null);
    }

    public MainController(String hostname, String endpoint) throws MalformedURLException {
        this(hostname, (String) null, endpoint);
    }

    public MainController(String hostname, Integer port, String endpoint) throws MalformedURLException {
        this(hostname, port.toString(), endpoint);
    }

    public MainController(String hostname, String port, String endpoint) throws IllegalArgumentException,
            MalformedURLException {
        super();

        this.setHostnamePrimitive(hostname);
        this.setPortPrimitive(port);
        this.setEndpointPrimitive(endpoint);
    }

    public MainController(MainController mc) {
        super();

        if (mc == null)
            mc = new MainController();

        try {
            this.setHostnamePrimitive(mc.hostname);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        this.setPortPrimitive(mc.port);
        this.setEndpointPrimitive(mc.endpoint);

        this.token = mc.token;
    }

    public static MainController fromArgs(final String[] args) throws MalformedURLException {
        if (args == null)
            return new MainController();

        return switch (args.length) {
            case 0 -> new MainController();
            case 1 -> new MainController(args[0]);
            case 2 -> new MainController(args[0], args[1]);
            default -> new MainController(args[0], args[1], args[2]);
        };
    }

    public String getHostname() {
        return this.hostname;
    }

    private void setHostnamePrimitive(String hostname) throws IllegalArgumentException, MalformedURLException {
        if (hostname == null) {
            this.hostname = MainController.DEFAULT_HOSTNAME;

            return;
        }

        if (!URL_REGEX.asMatchPredicate().test(hostname)) {
            final String temp = "http://" + hostname;

            if (!URL_REGEX.asMatchPredicate().test(temp))
                throw new MalformedURLException("Invalid hostname: " + hostname);

            hostname = temp;
        }

        try {
            final URL url = URI.create(hostname).toURL();

            if (!url.getPath().isEmpty()) throw new MalformedURLException("Invalid hostname: " + hostname);
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            throw new MalformedURLException("Invalid hostname: " + hostname);
        }

        this.hostname = hostname;
    }

    public void setHostname(String hostname) throws IllegalArgumentException, MalformedURLException {
        this.setHostnamePrimitive(hostname);
    }

    public Integer getPort() {
        return this.port;
    }

    private void setPortPrimitive(final String port) throws NumberFormatException {
        if (port == null) {
            setPortPrimitive((Integer) null);

            return;
        }

        setPortPrimitive(Integer.valueOf(port));
    }

    private void setPortPrimitive(final Integer port) throws IllegalArgumentException {
        if (port == null) {
            this.port = DEFAULT_PORT;

            return;
        }

        if (port < PORT_MIN || port > PORT_MAX)
            throw new IllegalArgumentException(String.format("Invalid port: %d", port));

        this.port = port;
    }

    public void setPort(final String port) throws NumberFormatException {
        this.setPortPrimitive(port);
    }

    public void setPort(final Integer port) throws IllegalArgumentException {
        this.setPortPrimitive(port);
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    private void setEndpointPrimitive(final String endpoint) {
        this.endpoint = endpoint == null ? DEFAULT_ENDPOINT : endpoint.replaceAll("^/", "");
    }

    public void setEndpoint(final String endpoint) {
        this.setEndpointPrimitive(endpoint);
    }

    public URL URL() {
        String ret = String.format("%s:%d/%s", this.hostname, this.port, this.endpoint.replaceAll("^/", ""));

        if (!URL_REGEX.asPredicate().test(ret)) return null;

        try {
            return URI.create(ret).toURL();
        } catch (MalformedURLException e) {
            Logger.getLogger().severe(e);

            return null;
        }
    }

    public void unauth() {
        this.token = null;
    }

    public CompletableFuture<Boolean> auth() {
        return this.authResp().thenApply(r -> this.token != null);
    }

    public CompletableFuture<Boolean> auth(final String id, String passwd) {
        return this.authResp(id, passwd).thenApply(r -> this.token != null);
    }

    public CompletableFuture<Boolean> auth(final Credentials creds) {
        return this.authResp(creds).thenApply(r -> this.token != null);
    }

    public CompletableFuture<HttpResponse<String>> authResp() {
        if (this.token == null) {
            final var body = "Requested token-based authentication without an existing token.";

            Logger.getLogger().warning(body);

            return CompletableFuture.completedFuture(RequestError.apply(body));
        }

        return this.request("/auth/refresh", RequestMethod.POST, null, null, Map.of("Authorization", this.token)).thenApply((r) -> {
            if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode()) {
                this.token = null;
                return r;
            }

            this.token = r.body();
            return r;
        });
    }

    public CompletableFuture<HttpResponse<String>> authResp(final String id, String passwd) {
        if (id == null || id.isBlank()) {
            final var body = "No username was provided";

            Logger.getLogger().warning(body);
            return CompletableFuture.completedFuture(RequestError.apply(body));
        }

        if (passwd == null || passwd.isBlank())
            passwd = "";

        return this.authResp(new Credentials(id, passwd));
    }

    public CompletableFuture<HttpResponse<String>> authResp(final Credentials creds) {
        if (creds == null) {
            final var body = "No credentials were provided.";

            Logger.getLogger().warning(body);
            return CompletableFuture.completedFuture(RequestError.apply(body));
        }

        if (creds.id().get() == null) {
            final var body = "Invalid username.";

            Logger.getLogger().warning(body);
            return CompletableFuture.completedFuture(RequestError.apply(body));
        }

        return this.request("/auth", RequestMethod.POST, null, creds).thenApply(r -> {
            if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode()) {
                this.token = null;
                return r;
            }

            this.token = r.body();
            return r;
        });
    }

    public CompletableFuture<HttpResponse<String>> request(@NotNull final String route) {
        return this.request(route, RequestMethod.GET);
    }

    public CompletableFuture<HttpResponse<String>> request(@NotNull final String route,
                                                           @NotNull final RequestMethod method) {
        return this.request(route, method, null);
    }

    public CompletableFuture<HttpResponse<String>> request(@NotNull final String route,
                                                           @NotNull final RequestMethod method, final Map<String,
            String[]> params) {
        return this.request(route, method, params, null);
    }

    public CompletableFuture<HttpResponse<String>> request(@NotNull final String route,
                                                           @NotNull final RequestMethod method, final Map<String,
            String[]> params, Object body) {
        return this.request(route, method, params, body, null);
    }

    public CompletableFuture<HttpResponse<String>> request(@NotNull final String route,
                                                           @NotNull final RequestMethod method, final Map<String,
            String[]> params, Object body, Map<String, String> headers) {
        if (route == null) throw new IllegalArgumentException("No route provided.");

        if (method == null) throw new IllegalArgumentException("No request method provided.");

        try {
            HttpRequest.BodyPublisher b = switch (body) {
                case null -> HttpRequest.BodyPublishers.noBody();
                case HttpRequest.BodyPublisher bp -> bp;
                default ->
                        HttpRequest.BodyPublishers.ofString(new ObjectMapperProvider().getContext(body.getClass()).writerWithDefaultPrettyPrinter().writeValueAsString(body));
            };

            String url = this.URL() + "/" + route.replaceAll("^/", "");

            if (params != null) {
                url += params.entrySet().stream().flatMap((entry) -> Arrays.stream(entry.getValue() == null ?
                        new String[0] : entry.getValue()).map(x -> String.format("%s=%s", entry.getKey(), x))).reduce(
                        "?", (acc, param) -> acc + param + "&").replaceAll("&$", "");
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));

            if (this.token != null)
                builder = builder.header("Authorization", this.token);

            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder = builder.setHeader(header.getKey(), header.getValue());
                }
            }

            HttpRequest request = switch (method) {
                case GET -> builder.GET().build();
                case HEAD -> builder.HEAD().build();
                case POST -> builder.POST(b).build();
                case PUT -> builder.PUT(b).build();
                case DELETE -> builder.DELETE().build();
                case CONNECT -> throw new UnsupportedOperationException("CONNECT method not supported.");
                case OPTIONS ->
                        builder.method(RequestMethod.OPTIONS.toString(), HttpRequest.BodyPublishers.noBody()).build();
                case TRACE -> throw new UnsupportedOperationException("TRACE method not supported.");
                case PATCH ->
                        builder.method(RequestMethod.PATCH.toString(), HttpRequest.BodyPublishers.noBody()).build();
            };

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply((r) -> {
                Logger.getLogger().info(String.format("%s: %d", route.replaceAll("^([^/])", "/$1"), r.statusCode()));

                if (r.statusCode() != ResponseStatus.ClientError4xx.UNAUTHORIZED_401.getStatusCode())
                    return r;

                if (Pattern.matches("^/?auth(/refresh?)?/?$", route))
                    return r;

                return this.auth().thenApply((auth) -> {
                    if (auth)
                        return this.request(route, method, params, b, headers).getNow(r);

                    return r;
                }).getNow(r);
            });
        } catch (Exception e) {
            Logger.getLogger().warning(e);

            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<List<ItemDTO>> getItems() {
        return this.request("/item/all").thenApply((r) -> {
            if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode())
                return null;

            try {
                var ia = ItemAssembler.getInstance();

                return Stream.of(new ObjectMapperProvider().getContext(Item[].class).readValue(r.body(),
                        Item[].class)).map(ia::ItemToDTO).toList();
            } catch (Exception e) {
                Logger.getLogger().warning(e);

                return null;
            }
        });
    }
}