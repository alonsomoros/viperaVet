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
import com.alonso.vipera.training.springboot_apirest.model.user.UserRole;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.ActivateAccountRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.OwnerCreationRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.VetRegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.UserRoleJpaRepository;
import com.alonso.vipera.training.springboot_apirest.persistence.repository.ConfirmationTokenRepository;
import com.alonso.vipera.training.springboot_apirest.model.user.ConfirmationToken;
import com.alonso.vipera.training.springboot_apirest.exception.TokenNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.TokenExpiredException;
import com.alonso.vipera.training.springboot_apirest.exception.PasswordsDoNotMatchException;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Override
    public AuthResponseDTO registerVet(VetRegisterRequestDTO registerRequestDTO) {
        log.info("Iniciando registro para el veterinario: {} {}", registerRequestDTO.getName(), registerRequestDTO.getSurnames());

        log.debug("Verificando inputs para el registro...");
        verifyRegisterInputs(registerRequestDTO);
        log.debug("Inputs verificados con éxito.");

        log.debug("Codificando la contraseña del veterinario...");
        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        log.debug("Guardando el veterinario en la base de datos...");
        User user = userMapper.toEntity(registerRequestDTO);
        
        // Fetch persistent role
        UserRole userRole = userRoleJpaRepository.findByRole(registerRequestDTO.getRole())
            .orElseThrow(() -> new RoleNotFoundException());
        user.setUserRole(userRole);
        user.setEnabled(true);

        user = userRepositoryAdapter.save(user);
        verifyRegisterOutputs(user);

        log.info("Veterinario {} registrado con éxito. ID: {}", user.getEmail(), user.getId());

        log.debug("Generando token JWT para el veterinario ID: {}", user.getId());
        String token = jwtService.generateToken(user);
        log.debug("Token JWT generado.");

        return new AuthResponseDTO(token, userMapper.toOutDTO(user));
    }
    
    @Override
    public UserOutDTO ownerCreation(OwnerCreationRequestDTO ownerCreationRequest) {
        log.info("Iniciando creación de propietario (Shadow User): {} {}", ownerCreationRequest.getName(), ownerCreationRequest.getSurnames());

        verifyRegisterInputs(ownerCreationRequest);

        User user = userMapper.toEntity(ownerCreationRequest);
        
        // Fetch persistent role
        UserRole userRole = userRoleJpaRepository.findByRole(ownerCreationRequest.getRole())
            .orElseThrow(() -> new RoleNotFoundException());
        user.setUserRole(userRole);
        
        // Shadow User logic: starting as disabled
        user.setEnabled(false);
        // Password will be set during activation, setting a random UUID as initial password placeholder
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        user = userRepositoryAdapter.save(user);
        verifyRegisterOutputs(user);

        // Generate activation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .user(user)
                .build();
        
        confirmationTokenRepository.save(confirmationToken);

        // Send activation email
        emailService.sendActivationEmail(user.getEmail(), token);

        log.info("Propietario {} creado con éxito en modo inactivo. ID: {}", user.getEmail(), user.getId());

        return userMapper.toOutDTO(user);
    }

    @Override
    public UserOutDTO activateAccount(ActivateAccountRequestDTO request) {
        log.info("Intentando activar cuenta con token...");

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new TokenNotFoundException());

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Esta cuenta ya ha sido activada");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        User user = confirmationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setEnabled(true);

        userRepositoryAdapter.save(user);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        log.info("Cuenta del usuario {} activada con éxito.", user.getEmail());

        return userMapper.toOutDTO(user);
    }

    private void verifyRegisterInputs(VetRegisterRequestDTO registerRequestDTO) {

        if (existsByEmail(registerRequestDTO.getEmail())) {
            log.warn("El email {} ya está en uso.", registerRequestDTO.getEmail());
            throw new EmailTakenException();
        }

        if (existsByPhone(registerRequestDTO.getPhone())) {
            log.warn("El teléfono {} ya está en uso.", registerRequestDTO.getPhone());
            throw new PhoneTakenException();
        }

    }

    private void verifyRegisterInputs(OwnerCreationRequestDTO registerRequestDTO) {

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
    public void verifyToken(String token) {
        log.info("Verificando token de activación...");

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException());

        if (confirmationToken.getConfirmedAt() != null) {
            log.warn("El token ya fue confirmado.");
            throw new IllegalStateException("Esta cuenta ya ha sido activada");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("El token ha expirado.");
            throw new TokenExpiredException();
        }

        log.info("Token válido.");
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
