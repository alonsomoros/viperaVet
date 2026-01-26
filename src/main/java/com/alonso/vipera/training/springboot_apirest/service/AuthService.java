package com.alonso.vipera.training.springboot_apirest.service;

import com.alonso.vipera.training.springboot_apirest.exception.BadCredentialsInputException;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.ActivateAccountRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.OwnerCreationRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.VetRegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

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
    AuthResponseDTO registerVet(VetRegisterRequestDTO registerRequestDTO);

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida que el username y email no existan previamente.
     *
     * @param registerRequestDTO DTO con los datos del usuario a registrar (username, email, password)
     * @return Respuesta de autenticación con token JWT y datos del usuario
     * @throws IllegalArgumentException Si el username o email ya existen en el sistema
     */
    UserOutDTO ownerCreation(OwnerCreationRequestDTO registerRequestDTO);

    /**
     * Activa la cuenta de un usuario mediante un token y establece su contraseña.
     *
     * @param request DTO con el token y la nueva contraseña
     * @return Datos del usuario activado
     */
    UserOutDTO activateAccount(ActivateAccountRequestDTO request);

    /**
     * Autentica un propietario de mascotas existente en el sistema.
     *
     * @param loginRequestDTO DTO con las credenciales de acceso (email y password)
     * @return Respuesta de autenticación con token JWT y datos del usuario
     * @throws BadCredentialsInputException Si las credenciales proporcionadas son incorrectas
     */
    AuthResponseDTO loginWithUser(LoginRequestDTO loginRequestDTO);

    /**
     * Autentica un veterinario existente en el sistema.
     *
     * @param loginRequestDTO DTO con las credenciales de acceso (email y password)
     * @return Respuesta de autenticación con token JWT y datos del usuario
     * @throws BadCredentialsInputException Si las credenciales proporcionadas son incorrectas
     */
    AuthResponseDTO loginWithVet(LoginRequestDTO loginRequestDTO);


    /**
     * Verifica si existe un veterinario o propietario de mascotas con el email especificado.
     *
     * @param email Dirección de correo electrónico a verificar
     * @return true si existe un veterinario o propietario de mascotas con ese email, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un veterinario o propietario de mascotas con el teléfono especificado.
     *
     * @param phone Teléfono del usuario a verificar
     * @return true si existe un usuario con ese teléfono, false en caso contrario
     */
    boolean existsByPhone(String phone);

    /**
     * Verifica si un token de confirmación es válido.
     *
     * @param token Token de confirmación a verificar
     * @throws IllegalArgumentException Si el token no es válido o ha expirado
     */
    void verifyToken(String token);

}