package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.UserData;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserDTOTest {

    UserDTO dto;
    @Mock
    private UserId id;
    private String passwd;
    @Mock
    private Set<Authority> authorities;
    @Mock
    private UserData data;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dto = new UserDTO();
        dto.setId(id);
        dto.setPasswd(passwd);
        //dto.setAuthorities(authorities);
        dto.setData(data);
    }
    @Test
    public void getId() {
        assertEquals(id, dto.id());
    }
    @Test
    public void getPasswd() {
        assertEquals(passwd, dto.passwd());
    }
    //@Test
    //public void getAuthorities() {
    //    assertEquals(authorities, dto.authorities());
    //}
    @Test
    public void getData() {
        assertEquals(data, dto.data());
    }
}
