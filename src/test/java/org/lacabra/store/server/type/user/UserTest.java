package org.lacabra.store.server.type.user;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.api.type.user.UserData;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class UserTest {

    User user1;
    @Mock
    UserData data;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User(new Credentials("0", "iker"), data);
    }
    @Test
    public void testGetId() {
        assertEquals(user1.id().toString(), "0");
    }
    @Test
    public void testGetPasswd() {
        assertEquals(user1.passwd(), "iker");
    }
    @Test
    public void testGetData() {
        assertEquals(user1.data(), data);
    }
}
