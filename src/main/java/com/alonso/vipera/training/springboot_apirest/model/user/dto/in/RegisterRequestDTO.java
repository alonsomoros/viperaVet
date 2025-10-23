package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import com.alonso.vipera.training.springboot_apirest.model.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegisterRequestDTO {
    @NotBlank(message = "Nombre de usuario necesario")
    @Size(min = 3, max = 50, message = "Nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "Contraseña necesaria")
    @Size(min = 6, max = 100, message = "Contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotBlank(message = "Email necesario")
    @Email(message = "Email no válido")
    private String email;

    @NotBlank(message = "Teléfono necesario")
    @Pattern(regexp = "^\\+?[0-9 .()\\-]{7,25}$", message = "Invalid phone number")
    private String phone;

    @Size(min = 3, max = 100, message = "La dirección debe tener entre 3 y 100 caracteres")
    private String address;

    @NotNull(message = "Rol necesario")
    private User.Role role;
}