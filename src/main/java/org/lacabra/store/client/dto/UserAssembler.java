package org.lacabra.store.client.dto;

import org.lacabra.store.server.api.type.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserAssembler {
    private static UserAssembler instance;

    private UserAssembler() {

    }

    public static UserAssembler getInstance() {
        if (instance == null) {
            instance = new UserAssembler();
        }
        return instance;
    }

    public UserDTO UserToDTO(User user) {
        UserDTO userDTO = new UserDTO(user.id().toString(), user.passwd());
        return userDTO;
    }

    public List<UserDTO> UsersToDTO(List<User> users) {
        List<UserDTO> dtos = new ArrayList<>();

        for (User user : users) {
            dtos.add(this.UserToDTO(user));
        }
        return dtos;
    }
}
