package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import com.alonso.vipera.training.springboot_apirest.model.user.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "Username necesario")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @Schema(description = "Email único del usuario. Debe ser un formato de email válido.", example = "alonso@gmail.com", required = true)
    @NotBlank(message = "Email necesario")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Contraseña. Debe tener al menos 6 caracteres.", example = "P4ssw0rd!", required = true)
    @NotBlank(message = "Password necesario")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Contraseña. Debe tener al menos 6 caracteres.", example = "P4ssw0rd!", required = true)
    @NotBlank(message = "Teléfono necesario")
    @Pattern(regexp = "^\\+?[0-9 .()\\-]{7,25}$", message = "Invalid phone number")
    private String phone;

    @Schema(description = "Dirección del usuario. Debe tener entre 3 y 100 caracteres.", example = "Calle Falsa 123, Ciudad, País", required = true)
    @Size(min = 3, max = 100, message = "La dirección debe tener entre 3 y 100 caracteres")
    private String address;

    @Schema(description = "Rol del usuario en el sistema.", example = "OWNER", required = true)
    @NotNull(message = "Rol necesario")
    private User.Role role;
}