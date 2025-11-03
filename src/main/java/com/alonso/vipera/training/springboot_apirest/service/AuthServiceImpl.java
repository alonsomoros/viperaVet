package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.BadCredentialsInputException;
import com.alonso.vipera.training.springboot_apirest.exception.EmailTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UserCreationException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.LoginRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        log.info("Iniciando registro para el usuario: {}", registerRequestDTO.getUsername());

        log.debug("Verificando inputs para el registro...");
        verifyRegisterInputs(registerRequestDTO);
        log.debug("Inputs verificados con éxito.");

        log.debug("Codificando la contraseña del usuario...");
        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        log.debug("Guardando el usuario en la base de datos...");
        User user = userRepositoryAdapter.save(userMapper.toEntity(registerRequestDTO));
        verifyRegisterOutputs(user);

        log.info("Usuario {} registrado con éxito. ID: {}", user.getUsername(), user.getId());

        log.debug("Generando token JWT para el usuario ID: {}", user.getId());
        String token = jwtService.generateToken(user);
        log.debug("Token JWT generado.");

        return new AuthResponseDTO(token, userMapper.toOutDTO(user));
    }

    private void verifyRegisterInputs(RegisterRequestDTO registerRequestDTO) {
        if (existsByUsername(registerRequestDTO.getUsername())) {
            log.warn("El nombre de usuario {} ya está en uso.", registerRequestDTO.getUsername());
            throw new UsernameTakenException();
        }
        if (existsByEmail(registerRequestDTO.getEmail())) {
            log.warn("El email {} ya está en uso.", registerRequestDTO.getEmail());
            throw new EmailTakenException();
        }

    }

    private void verifyRegisterOutputs(User userSaved) {
        if (userSaved == null || userSaved.getId() == null) {
            log.error("Error al crear el usuario en la base de datos.");
            throw new UserCreationException();
        }
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            log.info("Autenticando al usuario {}...", loginRequestDTO.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()));
            log.info("Usuario {} autenticado con éxito.", loginRequestDTO.getUsername());

            log.info("Recuperando los detalles del usuario {}...", loginRequestDTO.getUsername());
            User user = userRepositoryAdapter.findByUsername(loginRequestDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException());
            log.info("Detalles del usuario {} recuperados con éxito.", loginRequestDTO.getUsername());

            log.info("Generando token de autenticación para el usuario {}...", loginRequestDTO.getUsername());
            String token = jwtService.generateToken(user);
            log.info("Token de autenticación generado con éxito para el usuario {}.", loginRequestDTO.getUsername());

            return new AuthResponseDTO(token, userMapper.toOutDTO(user));
        } catch (BadCredentialsException e) {
            log.warn("Credenciales inválidas para el usuario {}.", loginRequestDTO.getUsername());
            throw new BadCredentialsInputException();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepositoryAdapter.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryAdapter.existsByEmail(email);
    }
}
