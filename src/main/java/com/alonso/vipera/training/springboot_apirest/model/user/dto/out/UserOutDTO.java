package com.alonso.vipera.training.springboot_apirest.model.user.dto.out;

import java.time.LocalDateTime;

import com.alonso.vipera.training.springboot_apirest.model.user.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la salida de datos de un usuario.
 * Contiene la información pública del usuario que puede ser expuesta a través
 * de la API.
 */
@Schema(description = "Información pública del usuario.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutDTO {

    /**
     * ID único del usuario.
     */
    @Schema(description = "ID único del usuario.", example = "1")
    private Long id;

    /**
     * Nombre del usuario.
     */
    @Schema(description = "Nombre del usuario.", example = "Alonso")
    private String name;

    /**
     * Apellidos del usuario.
     */
    @Schema(description = "Apellidos del usuario.", example = "Moros")
    private String surnames;

    /**
     * Email del usuario.
     */
    @Schema(description = "Email del usuario.", example = "alonso@gmail.com")
    private String email;

    /**
     * Rol del usuario en el sistema.
     */
    @Schema(description = "Rol del usuario en el sistema.", example = "USER")
    private Role role;

    /**
     * Fecha y hora de creación de la cuenta.
     */
    @Schema(description = "Fecha y hora de creación de la cuenta.", example = "2024-10-29T10:30:00")
    private LocalDateTime createdAt;
}