package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.BadCredentialsInputException;
import com.alonso.vipera.training.springboot_apirest.exception.EmailNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.EmailTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.PhoneTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.RoleNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UserCreationException;
import com.alonso.vipera.training.springboot_apirest.exception.WrongRoleActionException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.UserRoleJpaRepository;
import com.alonso.vipera.training.springboot_apirest.model.user.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de autenticación y autorización.
 * Maneja el registro de nuevos usuarios, inicio de sesión y validaciones de
 * credenciales.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        log.info("Iniciando registro para el usuario: {} {}", registerRequestDTO.getName(), registerRequestDTO.getSurnames());

        log.debug("Verificando inputs para el registro...");
        verifyRegisterInputs(registerRequestDTO);
        log.debug("Inputs verificados con éxito.");

        log.debug("Codificando la contraseña del usuario...");
        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        log.debug("Guardando el usuario en la base de datos...");
        User user = userMapper.toEntity(registerRequestDTO);
        
        // Fetch persistent role
        UserRole userRole = userRoleJpaRepository.findByRole(registerRequestDTO.getRole())
            .orElseThrow(() -> new RoleNotFoundException());
        user.setUserRole(userRole);

        user = userRepositoryAdapter.save(user);
        verifyRegisterOutputs(user);

        log.info("Usuario {} registrado con éxito. ID: {}", user.getEmail(), user.getId());

        log.debug("Generando token JWT para el usuario ID: {}", user.getId());
        String token = jwtService.generateToken(user);
        log.debug("Token JWT generado.");

        return new AuthResponseDTO(token, userMapper.toOutDTO(user));
    }

    private void verifyRegisterInputs(RegisterRequestDTO registerRequestDTO) {

        if (existsByEmail(registerRequestDTO.getEmail())) {
            log.warn("El email {} ya está en uso.", registerRequestDTO.getEmail());
            throw new EmailTakenException();
        }

        if (existsByPhone(registerRequestDTO.getPhone())) {
            log.warn("El teléfono {} ya está en uso.", registerRequestDTO.getPhone());
            throw new PhoneTakenException();
        }

    }

    private void verifyRegisterOutputs(User userSaved) {
        if (userSaved == null || userSaved.getId() == null) {
            log.error("Error al crear el usuario en la base de datos.");
            throw new UserCreationException();
        }
    }

    @Override
    public AuthResponseDTO loginWithUser(LoginRequestDTO loginRequestDTO) {
        try {
            log.info("Autenticando al usuario con email: {}...", loginRequestDTO.getEmail());

            log.info("Recuperando los detalles del usuario con email: {}...", loginRequestDTO.getEmail());
            User user = userRepositoryAdapter.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new EmailNotFoundException());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            loginRequestDTO.getPassword()));
            log.info("Usuario {} con email: {} autenticado con éxito.", user.getUsername(), user.getEmail());

            if (user.getUserRole().getRole() != Role.USER) {
                log.warn("El usuario {} con email: {} intentó loguearse como USER pero es {}.", user.getUsername(),
                        user.getEmail(), user.getUserRole());
                throw new WrongRoleActionException();
            }
            log.info("Detalles del usuario {} con email: {} recuperados con éxito.", user.getUsername(),
                    user.getEmail());

            log.info("Generando token de autenticación para el usuario {} con email: {}...", user.getUsername(),
                    user.getEmail());
            String token = jwtService.generateToken(user);
            log.info("Token de autenticación generado con éxito para el usuario {} con email: {}.", user.getUsername(),
                    user.getEmail());

            return new AuthResponseDTO(token, userMapper.toOutDTO(user));
        } catch (BadCredentialsException e) {
            log.warn("Credenciales inválidas para el usuario con email: {}.", loginRequestDTO.getEmail());
            throw new BadCredentialsInputException();
        }
    }

    @Override
    public AuthResponseDTO loginWithVet(LoginRequestDTO loginRequestDTO) {
        try {
            log.info("Autenticando al veterinario con email: {}...", loginRequestDTO.getEmail());

            log.info("Recuperando los detalles del veterinario con email: {}...", loginRequestDTO.getEmail());
            User user = userRepositoryAdapter.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new EmailNotFoundException());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            loginRequestDTO.getPassword()));
            log.info("Veterinario {} con email: {} autenticado con éxito.", user.getUsername(), user.getEmail());

            if (user.getUserRole().getRole() != Role.VET) {
                log.warn("El usuario {} con email: {} intentó loguearse como VET pero es {}.", user.getUsername(),
                        user.getEmail(), user.getUserRole());
                throw new WrongRoleActionException();
            }
            log.info("Detalles del veterinario {} con email: {} recuperados con éxito.", user.getUsername(),
                    user.getEmail());

            log.info("Generando token de autenticación para el veterinario {} con email: {}...", user.getUsername(),
                    user.getEmail());
            String token = jwtService.generateToken(user);
            log.info("Token de autenticación generado con éxito para el veterinario {} con email: {}.",
                    user.getUsername(), user.getEmail());

            return new AuthResponseDTO(token, userMapper.toOutDTO(user));
        } catch (BadCredentialsException e) {
            log.warn("Credenciales inválidas para el veterinario con email: {}.", loginRequestDTO.getEmail());
            throw new BadCredentialsInputException();
        }
    }


    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryAdapter.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepositoryAdapter.existsByPhone(phone);
    }
}
