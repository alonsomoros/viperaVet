package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos necesarios para registrar un nuevo usuario en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @Schema(description = "Nombre de usuario único. Debe tener entre 3 y 50 caracteres.", example = "alonso.dev", required = true)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Schema(description = "Email único del usuario. Debe ser un formato de email válido.", example = "alonso@gmail.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Contraseña. Debe tener al menos 6 caracteres.", example = "P4ssw0rd!", required = true)
    @NotBlank
    @Size(min = 6)
    private String password;
}