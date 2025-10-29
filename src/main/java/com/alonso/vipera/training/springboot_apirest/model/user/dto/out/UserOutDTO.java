package com.alonso.vipera.training.springboot_apirest.model.user.dto.out;

import java.time.LocalDateTime;

import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Información pública del usuario.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutDTO {

    @Schema(description = "ID único del usuario.", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario único.", example = "alonso.dev")
    private String username;

    @Schema(description = "Email del usuario.", example = "alonso@gmail.com")
    private String email;

    @Schema(description = "Rol del usuario en el sistema.", example = "OWNER")
    private Role role;

    @Schema(description = "Fecha y hora de creación de la cuenta.", example = "2024-10-29T10:30:00")
    private LocalDateTime createdAt;
}