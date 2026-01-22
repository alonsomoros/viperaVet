package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import com.alonso.vipera.training.springboot_apirest.model.user.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de registro de un nuevo usuario.
 */
@Schema(description = "Datos necesarios para registrar un nuevo usuario en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @Schema(description = "Nombre del usuario.", example = "Iñigo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nombre necesario")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Schema(description = "Apellidos del usuario.", example = "Montoya", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Apellidos necesarios")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String surnames;

    @Schema(description = "Email único del usuario. Debe ser un formato de email válido.", example = "alonso@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email necesario")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Contraseña. Debe tener al menos 6 caracteres.", example = "P4ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password necesario")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Contraseña. Debe tener al menos 6 caracteres.", example = "P4ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Teléfono necesario")
    @Pattern(regexp = "^\\+?[0-9 .()\\-]{7,25}$", message = "Invalid phone number")
    private String phone;

    @Schema(description = "Dirección del usuario. Debe tener entre 3 y 100 caracteres.", example = "Calle Falsa 123, Ciudad, País", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 100, message = "La dirección debe tener entre 3 y 100 caracteres")
    private String address;

    @Schema(description = "Rol del usuario en el sistema.", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Rol necesario")
    private Role role;
}