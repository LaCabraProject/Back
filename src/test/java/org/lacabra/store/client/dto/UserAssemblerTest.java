package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.api.type.user.UserData;
import org.lacabra.store.server.api.type.user.Credentials;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserAssemblerTest {
    UserAssembler assembler;

    User user1;
    User user2;
    List<User> users;
    
    @Before
    public void setUp() {
        user1 = new User("iker", "Mikel");
        assembler = UserAssembler.getInstance();
    }
    @Test
    public void testUserToDTO() {
        //UserDTO userDTO1 = assembler.UserToDTO(user1);

        //assertEquals(user1.id(), userDTO1.id());
        //assertEquals(user1.passwd(), userDTO1.passwd());
    }
}
