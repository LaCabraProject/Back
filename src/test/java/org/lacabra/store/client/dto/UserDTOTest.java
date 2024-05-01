package org.lacabra.store.client.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lacabra.store.internals.type.id.UserId;
import org.lacabra.store.server.api.type.user.Authority;
import org.lacabra.store.server.api.type.user.Credentials;
import org.lacabra.store.server.api.type.user.User;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    private Credentials credentials;
    @Mock
    private User user;

    @Before
    public void setUp() {
        when(authorities.stream()).thenAnswer(x -> Stream.of(Authority.Client));
        when(authorities.iterator()).thenAnswer(x -> Stream.of(Authority.Client).iterator());

        dto1 = new UserDTO();
        dto1 = dto1.id(id);
        dto1 = dto1.passwd(passwd);
        dto1 = dto1.authorities(authorities);

        dto2 = new UserDTO("iker");
        dto3 = new UserDTO("iker", "1234");
        dto4 = new UserDTO(credentials);
        dto5 = new UserDTO(credentials);
        dto6 = user.toDTO();
    }

    @Test
    public void getId() {
        assertEquals(dto1.id(), id);
    }

    @Test
    public void getPasswd() {
        assertEquals(dto1.passwd(), passwd);
    }

    @Test
    public void getAuthorities() {
        assertEquals(dto1.authorities(), authorities.stream().collect(Collectors.toSet()));
    }
}
