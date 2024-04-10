package org.lacabra.store.server.api.type.user;

import org.lacabra.store.server.jdo.dao.Mergeable;

import javax.jdo.annotations.EmbeddedOnly;
import java.io.Serial;
import java.io.Serializable;

@EmbeddedOnly
public class UserData implements Serializable, Mergeable<UserData> {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserData() {}

    public UserData(UserData data) {}

    @Override
    public UserData merge (UserData override) {
        if (override == null)
            return this;

        Mergeable.super.merge(this);

        return this;
    }
}