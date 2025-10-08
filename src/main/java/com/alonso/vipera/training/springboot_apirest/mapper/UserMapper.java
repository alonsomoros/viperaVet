package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.in.UserInDTO;

@Component
public class UserMapper {
    public User toEntity(UserInDTO dto) {
        if (dto == null) return null;
        return User.builder()
                   .username(dto.getUsername())
                   .email(dto.getEmail())
                   .password(dto.getPassword())
                   .build();
    }
}
