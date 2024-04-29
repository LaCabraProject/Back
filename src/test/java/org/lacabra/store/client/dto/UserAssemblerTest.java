package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.server.api.type.user.User;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserAssemblerTest {
    UserAssembler assembler;

    User user;
    List<User> users;

    @Before
    public void setUp() {
        //user = new User(0,"clothing","Camiseta de grupo genérico",25,30,1000,"mikel");
        MockitoAnnotations.openMocks(this);
        assembler = UserAssembler.getInstance();
    }
    @Test
    public void testUserToDTO() {
        UserDTO userDTO = assembler.UserToDTO(user);
        assertEquals(user.id(), userDTO.id());
        assertEquals(user.authorities(), userDTO.authorities());
        assertEquals(user.data(), userDTO.data());
        assertEquals(user.passwd(), userDTO.passwd());
    }
}
