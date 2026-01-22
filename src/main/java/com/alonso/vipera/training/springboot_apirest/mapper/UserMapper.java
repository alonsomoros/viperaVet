package com.alonso.vipera.training.springboot_apirest.mapper;

import com.alonso.vipera.training.springboot_apirest.model.user.UserRole;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.OwnerCreationRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.VetRegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

/**
 * Mapper para convertir entre entidades User y sus DTOs correspondientes.
 */
@Component
public class UserMapper {

    /**
     * Convierte un DTO de entrada VetRegisterRequestDTO a una entidad User.
     *
     * @param dto DTO de entrada con los datos del usuario.
     * @return Entidad User correspondiente.
     */
    public User toEntity(VetRegisterRequestDTO dto) {
        if (dto == null)
            return null;
        return User.builder()
                .name(dto.getName())
                .surnames(dto.getSurnames())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .userRole(new UserRole(null, Role.VET))
                .build();
    }

        /**
     * Convierte un DTO de entrada OwnerCreateRequestDTO a una entidad User.
     *
     * @param dto DTO de entrada con los datos del usuario.
     * @return Entidad User correspondiente.
     */
    public User toEntity(OwnerCreationRequestDTO dto) {
        if (dto == null)
            return null;
        return User.builder()
                .name(dto.getName())
                .surnames(dto.getSurnames())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dni(dto.getDni())
                .userRole(new UserRole(null, Role.USER))
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
        return new UserOutDTO(entity.getId(), entity.getName(), entity.getSurnames(), entity.getEmail(),
                entity.getUserRole().getRole(), entity.getCreatedAt());
    }

}
