package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

/**
 * Mapper para convertir entre entidades User y sus DTOs correspondientes.
 */
@Component
public class UserMapper {

    /**
     * Convierte un DTO de entrada RegisterRequestDTO a una entidad User.
     *
     * @param dto DTO de entrada con los datos del usuario.
     * @return Entidad User correspondiente.
     */
    public User toEntity(RegisterRequestDTO dto) {
        if (dto == null)
            return null;
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .build();
    }

    /**
     * Convierte una entidad User a su correspondiente DTO de salida UserOutDTO.
     *
     * @param entity Entidad User a convertir.
     * @return DTO de salida UserOutDTO.
     */
    public UserOutDTO toOutDTO(User entity) {
        if (entity == null)
            return null;
        return new UserOutDTO(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getRole(),
                entity.getCreatedAt());
    }

}
