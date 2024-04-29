package org.lacabra.store.client.dto;

import org.lacabra.store.server.api.type.id.ObjectId;
import org.lacabra.store.server.api.type.id.UserId;
import org.lacabra.store.server.api.type.item.ItemType;
import org.mockito.Mock;

public class ItemDTOtest {

    @Mock
    private ObjectId id;
    @Mock
    private ItemType type;
    @Mock
    private UserId parent;
}
