package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Credenciales necesarias para autenticarse en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @Schema(description = "Nombre de usuario o email del usuario.", example = "alonso.dev", required = true)
    @NotBlank(message = "Username necesario")
    private String username;

    @Schema(description = "Contraseña del usuario.", example = "P4ssw0rd!", required = true)
    @NotBlank(message = "Password necesario")
    private String password;
}