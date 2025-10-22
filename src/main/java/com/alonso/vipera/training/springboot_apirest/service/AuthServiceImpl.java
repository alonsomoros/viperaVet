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
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;

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
        verifyRegisterInputs(registerRequestDTO);

        registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        User user = userRepositoryAdapter.save(userMapper.toEntity(registerRequestDTO));
        verifyRegisterOutputs(user);

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, user.getUsername());
    }

    private void verifyRegisterInputs(RegisterRequestDTO registerRequestDTO) {
        if (existsByUsername(registerRequestDTO.getUsername())) {
            throw new UsernameTakenException();
        }
        if (existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailTakenException();
        }

    }

    private void verifyRegisterOutputs(User userSaved) {
        if (userSaved == null || userSaved.getId() == null) {
            throw new UserCreationException();
        }
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()));

        User user = userRepositoryAdapter.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException());

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, user.getUsername());
        } catch (BadCredentialsException e) {
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
