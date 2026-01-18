package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de inicio de sesión.
 * Contiene las credenciales necesarias para que un usuario se autentique en el sistema.
 */
@Schema(description = "Credenciales necesarias para autenticarse en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @Schema(description = "Email único del usuario. Debe ser un formato de email válido.", example = "alonso@gmail.com", required = true)
    @NotBlank(message = "Email necesario")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Contraseña del usuario.", example = "P4ssw0rd!", required = true)
    @NotBlank(message = "Password necesario")
    private String password;
}