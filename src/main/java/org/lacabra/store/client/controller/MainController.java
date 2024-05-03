package org.lacabra.store.client.controller;


import org.glassfish.jersey.http.ResponseStatus;
import org.lacabra.store.client.assembler.ItemAssembler;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.dto.UserDTO;
import org.lacabra.store.internals.json.provider.ObjectMapperProvider;
import org.lacabra.store.internals.logging.Logger;
import org.lacabra.store.internals.type.RequestMethod;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Credentials;

import javax.net.ssl.SSLSession;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MainController implements Serializable {
    public final static Pattern URL_REGEX =
            Pattern.compile("^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;" + "]*[-a-zA-Z0-9" + "+&@#/%=~_|]");
    public final static int PORT_MIN = 0;
    public final static int PORT_MAX = 65535;
    public final static String DEFAULT_HOSTNAME = "http://localhost";
    public final static int DEFAULT_PORT = 8080;
    public final static String DEFAULT_ENDPOINT = "/api";
    public final static Function<String, HttpResponse<String>> RequestError = body -> new HttpResponse<>() {
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
            return HttpHeaders.of(Collections.emptyMap(), (x, y) -> true);
        }

        @Override
        public String body() {
            return body == null ? HttpRequest.BodyPublishers.noBody().toString() : body;
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
    @Serial
    private static final long serialVersionUID = 1L;
    public static final int TIMEOUT = 2500;
    public final GET GET = new GET(this);
    private final transient HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofMillis(TIMEOUT)).build();
    private String hostname;
    private Integer port;
    private String endpoint;
    private UserId user;
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

        if (mc == null) mc = new MainController();

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
        if (args == null) return new MainController();

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

    public void setHostname(String hostname) throws IllegalArgumentException, MalformedURLException {
        this.setHostnamePrimitive(hostname);
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

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final String port) throws NumberFormatException {
        this.setPortPrimitive(port);
    }

    public void setPort(final Integer port) throws IllegalArgumentException {
        this.setPortPrimitive(port);
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

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.setEndpointPrimitive(endpoint);
    }

    private void setEndpointPrimitive(final String endpoint) {
        this.endpoint = endpoint == null ? DEFAULT_ENDPOINT : endpoint.replaceAll("^/", "");
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

    public UserId getUser() {
        return this.user;
    }

    public void unauth() {
        this.user = null;
        this.token = null;
    }

    public boolean authSync() {
        try {
            return this.auth().get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            return false;
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            return false;
        }
    }

    public boolean authSync(final String id, String passwd) {
        try {
            this.unauth();
            return this.auth(id, passwd).get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            return false;
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            return false;
        }
    }

    public boolean authSync(final Credentials creds) {
        try {
            this.unauth();
            return this.auth(creds).get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            return false;
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            return false;
        }
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

        return this.request("/auth/refresh", RequestMethod.POST).thenApply((r) -> {
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

        if (passwd == null || passwd.isBlank()) passwd = "";

        return this.authResp(new Credentials(id, passwd));
    }

    public CompletableFuture<HttpResponse<String>> authResp(final Credentials creds) {
        this.unauth();

        if (creds == null) {
            final var body = "No credentials were provided.";

            Logger.getLogger().warning(body);
            return CompletableFuture.completedFuture(RequestError.apply(body));
        }

        final var id = creds.id();

        if (id.get() == null) {
            final var body = "Invalid username.";

            Logger.getLogger().warning(body);
            return CompletableFuture.completedFuture(RequestError.apply(body));
        }

        this.user = id;

        try {
            return this.request("/auth", RequestMethod.POST, null, creds).thenApply(r -> {
                try {
                    if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode()) {
                        this.token = null;
                        return r;
                    }

                    this.token = r.body();
                    return r;
                } catch (Exception e) {
                    this.token = null;
                    return RequestError.apply(e.getMessage());
                }
            });
        } catch (Exception e) {
            this.token = null;
            return CompletableFuture.completedFuture(RequestError.apply(e.getMessage()));
        }
    }

    public HttpResponse<String> requestSync(@NotNull final String route) {
        return this.requestSync(route, RequestMethod.GET);
    }

    public HttpResponse<String> requestSync(@NotNull final String route, @NotNull final RequestMethod method) {
        return this.requestSync(route, method, null);
    }

    public HttpResponse<String> requestSync(@NotNull final String route, @NotNull final RequestMethod method,
                                            final Map<String, String[]> params) {
        return this.requestSync(route, method, params, null);
    }

    public HttpResponse<String> requestSync(@NotNull final String route, @NotNull final RequestMethod method,
                                            final Map<String, String[]> params, Object body) {
        return this.requestSync(route, method, params, body, null);
    }

    public HttpResponse<String> requestSync(@NotNull final String route, @NotNull final RequestMethod method,
                                            final Map<String, String[]> params, Object body,
                                            Map<String, String> headers) {
        try {
            return this.request(route, method, params, body, headers).get((long) (TIMEOUT * 2.5),
                    TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            return RequestError.apply(e.getMessage());
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            return RequestError.apply(e.getMessage());
        }
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
            String url = this.URL() + "/" + route.replaceAll("^/", "");

            if (params != null) {
                url += params.entrySet().stream().flatMap((entry) -> Arrays.stream(entry.getValue() == null ?
                        new String[0] : entry.getValue()).map(x -> String.format("%s=%s", entry.getKey(), x))).reduce("?", (acc, param) -> acc + param + "&").replaceAll("&$", "");
            }

            var builder = HttpRequest.newBuilder(URI.create(url));

            if (this.token != null) builder = builder.header("Authorization", "Bearer " + this.token);

            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder = builder.setHeader(header.getKey(), header.getValue());
                }
            }

            final HttpRequest.BodyPublisher b = switch (body) {
                case null -> HttpRequest.BodyPublishers.noBody();
                case HttpRequest.BodyPublisher bp -> bp;
                default -> {
                    builder.setHeader("Content-Type", "application/json");
                    yield HttpRequest.BodyPublishers.ofString(new ObjectMapperProvider().getContext(body.getClass()).writerWithDefaultPrettyPrinter().writeValueAsString(body));
                }
            };

            return httpClient.sendAsync(switch (method) {
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
            }, HttpResponse.BodyHandlers.ofString()).thenApply((r) -> {
                try {
                    Logger.getLogger().info(String.format("%s: %d%s", route.replaceAll("^([^/])", "/$1"),
                            r.statusCode(), switch (r.statusCode()) {
                                case 200, 404, 401 -> "";

                                default -> String.format(" (\nBODY\n---\n%s)", r.body().replaceAll("\n", "\n\t"));
                            }));

                    if (r.statusCode() != ResponseStatus.ClientError4xx.UNAUTHORIZED_401.getStatusCode()) return r;

                    if (Pattern.matches("^/?auth.*", r.uri().getPath())) return r;

                    if (this.token != null && this.authSync())
                        return this.requestSync(route, method, params, b, headers);

                    return r;
                } catch (Exception e) {
                    return RequestError.apply(e.getMessage());
                }
            });
        } catch (Exception e) {
            Logger.getLogger().warning(e);

            return CompletableFuture.completedFuture(RequestError.apply(e.getMessage()));
        }
    }

    public boolean aliveSync() {
        try {
            return this.alive().get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            return false;
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            return false;
        }
    }

    public final CompletableFuture<Boolean> alive() {
        try {
            return this.request("alive", RequestMethod.HEAD).thenApply(r -> {
                try {
                    return r.statusCode() == 200;
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            Logger.getLogger().severe(e);

            return CompletableFuture.completedFuture(false);
        }
    }

    public static final class GET implements Serializable {
        @Serial
        private final static long serialVersionUID = 1L;

        public final Item Item;
        public final User User;

        public GET(final MainController controller) {
            super();

            this.Item = new Item(controller);
            this.User = new User(controller);
        }

        public record Item(MainController controller) implements Serializable {
            @Serial
            private final static long serialVersionUID = 1L;

            public CompletableFuture<ItemDTO> id(final Number id) {
                return this.id(ObjectId.from(id));
            }

            public CompletableFuture<ItemDTO> id(final String id) {
                return this.id(ObjectId.from(id));
            }

            public CompletableFuture<ItemDTO> id(final ObjectId id) {
                if (id == null) return CompletableFuture.completedFuture(null);

                return this.controller.request("/item/" + id).thenApply(r -> {
                    if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode()) return null;

                    try {
                        return ItemAssembler.getInstance().ItemToDTO(new ObjectMapperProvider().getContext(org.lacabra.store.server.api.type.item.Item.class).readValue(r.body(), org.lacabra.store.server.api.type.item.Item.class));
                    } catch (Exception e) {
                        Logger.getLogger().warning(e);

                        return null;
                    }
                });
            }

            public ItemDTO idSync(final Number id) {
                return this.idSync(ObjectId.from(id));
            }

            public ItemDTO idSync(final String id) {
                return this.idSync(ObjectId.from(id));
            }

            public ItemDTO idSync(final ObjectId id) {
                try {
                    return this.id(id).get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
                } catch (TimeoutException | ExecutionException | InterruptedException e) {
                    return null;
                } catch (Exception e) {
                    Logger.getLogger().severe(e);

                    return null;
                }
            }

            public CompletableFuture<List<ItemDTO>> all() {
                return this.controller.request("/item/all").thenApply((r) -> {
                    if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode()) return null;

                    try {
                        var ia = ItemAssembler.getInstance();

                        return Stream.of(new ObjectMapperProvider().getContext(org.lacabra.store.server.api.type.item.Item[].class).readValue(r.body(), org.lacabra.store.server.api.type.item.Item[].class)).map(ia::ItemToDTO).toList();
                    } catch (Exception e) {
                        Logger.getLogger().warning(e);

                        return null;
                    }
                });
            }

            public List<ItemDTO> allSync() {
                try {
                    return this.all().get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
                } catch (TimeoutException | ExecutionException | InterruptedException e) {
                    return null;
                } catch (Exception e) {
                    Logger.getLogger().severe(e);

                    return null;
                }
            }
        }

        public record User(MainController controller) implements Serializable {
            @Serial
            private final static long serialVersionUID = 1L;

            public CompletableFuture<UserDTO> id(final String id) {
                return this.id(UserId.from(id));
            }

            public CompletableFuture<UserDTO> id(final UserId id) {
                if (id == null) return CompletableFuture.completedFuture(null);

                return this.controller.request("/user/" + id).thenApply(r -> {
                    if (r.statusCode() != ResponseStatus.Success2xx.OK_200.getStatusCode()) return null;

                    try {
                        return new ObjectMapperProvider().getContext(UserDTO.class).readValue(r.body(), UserDTO.class);
                    } catch (Exception e) {
                        Logger.getLogger().warning(e);

                        return null;
                    }
                });
            }

            public UserDTO idSync(final String id) {
                return this.idSync(UserId.from(id));
            }

            public UserDTO idSync(final UserId id) {
                try {
                    return this.id(id).get((long) (TIMEOUT * 2.5), TimeUnit.MILLISECONDS);
                } catch (TimeoutException | ExecutionException | InterruptedException e) {
                    return null;
                } catch (Exception e) {
                    Logger.getLogger().severe(e);

                    return null;
                }
            }
        }
    }
}