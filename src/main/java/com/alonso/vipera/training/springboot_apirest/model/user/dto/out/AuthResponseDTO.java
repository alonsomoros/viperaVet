package com.alonso.vipera.training.springboot_apirest.model.user.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Respuesta de autenticación exitosa.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    @Schema(description = "Token JWT para autenticación de requests posteriores.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Información del usuario autenticado.")
    private UserOutDTO user;
}