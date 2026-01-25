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
public class OwnerCreationRequestDTO {

    @Schema(description = "Nombre del usuario.", example = "Iñigo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nombre necesario")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Schema(description = "Apellidos del usuario.", example = "Montoya", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Apellidos necesarios")
    @Size(min = 2, max = 50, message = "Los apellidos deben tener entre 2 y 50 caracteres")
    private String surnames;

    @Schema(description = "Email único del usuario. Debe ser un formato de email válido.", example = "iñigo@vet.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email necesario")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Teléfono del usuario.", example = "600112233", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Teléfono necesario")
    @Pattern(regexp = "^\\+?[0-9 .()\\-]{7,25}$", message = "Invalid phone number")
    private String phone;

    @Schema(description = "DNI del usuario.", example = "12345678A", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "DNI necesario")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]", message = "Invalid DNI")
    private String dni;

    @Schema(description = "Rol del usuario en el sistema.", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Rol necesario")
    private Role role;
}