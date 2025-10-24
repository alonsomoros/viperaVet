package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alonso.vipera.training.springboot_apirest.exception.EmailTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UserCreationException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.AuthServiceImpl;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryAdapter userRepositoryAdapter;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private RegisterRequestDTO registerRequestDTO;
    private User userEntity;

    @BeforeEach
    void setUp() {

        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("Juan");
        registerRequestDTO.setEmail("juan@gmail.com");
        registerRequestDTO.setPassword("password123");

        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("Juan");
        userEntity.setEmail("juan@gmail.com");
        userEntity.setPassword("encodedPassword");
    }

    @Test
    void testPasswordEncryption() {
        PasswordEncoder realEncoder = new BCryptPasswordEncoder();
        String encoded = realEncoder.encode("password123");

        assertTrue(realEncoder.matches("password123", encoded));
    }

    @Test
    void testCreateUser_whenValidResponse_shouldCreateUserSuccesfully() {
        // Arrange - Mocks de verificación
        when(userRepositoryAdapter.existsByUsername(registerRequestDTO.getUsername())).thenReturn(false);
        when(userRepositoryAdapter.existsByEmail(registerRequestDTO.getEmail())).thenReturn(false);

        // Arrange - Mock de encriptación
        when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("encodedPassword");

        // Arrange - Mock de mapping y guardado
        when(userMapper.toEntity(registerRequestDTO)).thenReturn(userEntity);
        when(userRepositoryAdapter.save(any(User.class))).thenReturn(userEntity);

        // Arrange - Mock de JWT
        when(jwtService.generateToken(userEntity)).thenReturn("jwtToken");

        // Act
        AuthResponseDTO result = authServiceImpl.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(registerRequestDTO.getUsername(), result.getUsername());

        // Verify interactions
        verify(passwordEncoder, times(1)).encode("password123");
        verify(jwtService, times(1)).generateToken(userEntity);
        verify(userRepositoryAdapter, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenUsernameTaken_shouldThrowUsernameTakenException() {
        // Arrange - Mocks de verificación
        when(userRepositoryAdapter.existsByUsername(registerRequestDTO.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(UsernameTakenException.class, () -> authServiceImpl.register(registerRequestDTO));

        // Verify interactions
        verify(userRepositoryAdapter, times(1)).existsByUsername(registerRequestDTO.getUsername());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenEmailTaken_shouldThrowEmailTakenException() {
        // Arrange - Mocks de verificación
        when(userRepositoryAdapter.existsByUsername(registerRequestDTO.getUsername())).thenReturn(false);
        when(userRepositoryAdapter.existsByEmail(registerRequestDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailTakenException.class, () -> authServiceImpl.register(registerRequestDTO));

        // Verify interactions
        verify(userRepositoryAdapter, times(1)).existsByEmail(registerRequestDTO.getEmail());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenUserNotSaved_shouldThrowUserCreationException() {
        // Arrange - Mocks de verificación
        when(userRepositoryAdapter.existsByUsername(registerRequestDTO.getUsername())).thenReturn(false);
        when(userRepositoryAdapter.existsByEmail(registerRequestDTO.getEmail())).thenReturn(false);

        // Arrange - Mock de encriptación
        when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("encodedPassword");

        // Arrange - Mock de mapping y guardado que falla
        when(userMapper.toEntity(registerRequestDTO)).thenReturn(userEntity);
        when(userRepositoryAdapter.save(any(User.class))).thenReturn(null);

        // Act & Assert
        assertThrows(UserCreationException.class, () -> authServiceImpl.register(registerRequestDTO));

        // Verify interactions
        verify(userRepositoryAdapter, times(1)).save(any(User.class));
    }

}
