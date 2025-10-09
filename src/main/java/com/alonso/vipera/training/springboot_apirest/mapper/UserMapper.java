package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.model.dto.out.UserOutDTO;

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

    public UserOutDTO toOutDTO(User entity) {
        if (entity == null) return null;
        return new UserOutDTO(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getCreatedAt());
    }
}
