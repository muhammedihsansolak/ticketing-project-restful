package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import javax.ws.rs.core.Response;

public interface KeycloakService {
    Response userCreate(UserDTO userDTO);
    void delete(String username);
}
