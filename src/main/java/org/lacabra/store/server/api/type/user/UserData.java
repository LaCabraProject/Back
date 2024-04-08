package org.lacabra.store.server.api.type.user;

import javax.jdo.annotations.EmbeddedOnly;
import java.io.Serial;
import java.io.Serializable;

@EmbeddedOnly
public class UserData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}