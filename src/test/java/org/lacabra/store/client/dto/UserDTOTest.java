package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.lacabra.store.server.api.type.user.UserData;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserDTOTest {

    UserDTO dto1;
    UserDTO dto2;
    UserDTO dto3;
    UserDTO dto4;
    UserDTO dto5;
    UserDTO dto6;
    @Mock
    private UserId id;
    private String passwd;
    @Mock
    private Set<Authority> authorities;
    @Mock
    private UserData data;
    @Mock
    private Credentials credentials;
    @Mock
    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dto1 = new UserDTO();
        dto2 = new UserDTO("iker");
        dto3 = new UserDTO("iker", "1234");
        dto4 = new UserDTO(credentials);
        dto5 = new UserDTO(credentials, data);
        dto6 = new UserDTO(user);
        dto1.setId(id);
        dto1.setPasswd(passwd);
        //dto1.setAuthorities(authorities);
        dto1.setData(data);
    }

    @Test
    public void getId() {
        assertEquals(id, dto1.id());
    }

    @Test
    public void getPasswd() {
        assertEquals(passwd, dto1.passwd());
    }

    //@Test
    //public void getAuthorities() {
    //    assertEquals(authorities, dto1.authorities());
    //}
    @Test
    public void getData() {
        assertEquals(data, dto1.data());
    }
}
