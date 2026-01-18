package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar la autenticación de usuarios.
 * Proporciona endpoints para el registro y login de usuarios.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "API endpoints para gestionar el proceso de autenticación")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param registerRequestDto DTO que contiene la información del usuario a registrar.
     * @return ResponseEntity con el token de autenticación y detalles del usuario registrado.
     */
    @Operation(summary = "Registro de Usuario", description = "Permite un usuario registrarse en el sistema y obtener un token de autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Contraseña débil. La contraseña debe tener al menos 6 caracteres.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Formato de email inválido.", content = @Content),
            @ApiResponse(responseCode = "409", description = "'X' parámetro está ya está en uso", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDto) {
        log.info("Iniciando proceso de registro para el usuario: {}", registerRequestDto.getEmail());
        AuthResponseDTO authResponseDto = authService.register(registerRequestDto);
        return ResponseEntity.ok(authResponseDto);
    }

    /**
     * Endpoint para loggear un usuario existente.
     *
     * @param loginRequestDto DTO que contiene las credenciales del usuario.
     * @return ResponseEntity con el token de autenticación y detalles del usuario loggeado.
     */
    @Operation(summary = "Login de Usuario", description = "Permite un usuario loggearse en el sistema y obtener un token de autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario loggeado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales de entrada inválidas", content = @Content)
    })
    @PostMapping("/login/vet")
    public ResponseEntity<AuthResponseDTO> loginVet(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
        log.info("Iniciando proceso de login para el usuario: {}", loginRequestDto.getUsername());
        AuthResponseDTO authResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(authResponseDto);
    }

    /**
     * Endpoint para loggear un usuario existente.
     *
     * @param loginRequestDto DTO que contiene las credenciales del usuario.
     * @return ResponseEntity con el token de autenticación y detalles del usuario loggeado.
     */
    @Operation(summary = "Login de Usuario", description = "Permite un usuario loggearse en el sistema y obtener un token de autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario loggeado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales de entrada inválidas", content = @Content)
    })
    @PostMapping("/login/owner")
    public ResponseEntity<AuthResponseDTO> loginOwner(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
        log.info("Iniciando proceso de login para el usuario: {}", loginRequestDto.getUsername());
        AuthResponseDTO authResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(authResponseDto);
    }
}
