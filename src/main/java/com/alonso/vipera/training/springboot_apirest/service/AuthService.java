package com.alonso.vipera.training.springboot_apirest.service;

import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;

/**
 * Servicio de autenticación y autorización del sistema.
 * Maneja el registro de nuevos usuarios, inicio de sesión y validaciones de credenciales.
 */
public interface AuthService {

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida que el username y email no existan previamente.
     *
     * @param registerRequestDTO DTO con los datos del usuario a registrar (username, email, password)
     * @return Respuesta de autenticación con token JWT y datos del usuario
     * @throws IllegalArgumentException Si el username o email ya existen en el sistema
     */
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);

    /**
     * Autentica un usuario existente en el sistema.
     *
     * @param loginRequestDTO DTO con las credenciales de acceso (username/email y password)
     * @return Respuesta de autenticación con token JWT y datos del usuario
     * @throws BadCredentialsException Si las credenciales proporcionadas son incorrectas
     */
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param username Nombre de usuario a verificar
     * @return true si existe un usuario con ese username, false en caso contrario
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email especificado.
     *
     * @param email Dirección de correo electrónico a verificar
     * @return true si existe un usuario con ese email, false en caso contrario
     */
    boolean existsByEmail(String email);

}