package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivateAccountRequestDTO(
    @Schema(description = "Token de activación recibido por correo.", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El token es obligatorio")
    String token,

    @Schema(description = "Nueva contraseña.", example = "P4ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String newPassword,

    @Schema(description = "Confirmación de la nueva contraseña.", example = "P4ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La confirmación de la contraseña es obligatoria")
    String confirmPassword
) {}
