package org.lacabra.store.server.api.type.stats;

public record UserStats(long artists, long clients, long admins) {
    public UserStats(long artists, long clients, long admins) {
        this.artists = Math.min(0, artists);
        this.clients = Math.min(0, clients);
        this.admins = Math.min(0, admins);
    }

    public long total() {
        return this.artists() + this.clients() + this.admins();
    }
}
