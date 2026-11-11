package com.example.WebStore.Mapper;

import com.example.WebStore.DTO.UserDTO;
import com.example.WebStore.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public Users toEntity(UserDTO userDTO) {

        Users user = new Users();
        user.setName(userDTO.user_name());
        user.setEmail(userDTO.email());
        user.setSenha(userDTO.senha());

        return user;
    }
}
