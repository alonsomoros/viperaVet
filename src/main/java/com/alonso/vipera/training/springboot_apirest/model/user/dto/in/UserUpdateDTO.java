package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la actualización de un usuario.
 * Contiene los campos que pueden ser modificados para un usuario existente en
 * el sistema.
 */
@Schema(description = "Datos necesarios para actualizar un usuario en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Schema(description = "Teléfono del usuario", example = "666555444")
    @Pattern(regexp = "^\\+?[0-9 .()\\-]{7,25}$", message = "Invalid phone number")
    private String phone;

    @Schema(description = "Correo electrónico del usuario", example = "juanperez@gmail.com")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Dirección del usuario", example = "Calle Falsa 123, Ciudad, País")
    @Size(min = 3, max = 100, message = "La dirección debe tener entre 3 y 100 caracteres")
    private String address;
}
