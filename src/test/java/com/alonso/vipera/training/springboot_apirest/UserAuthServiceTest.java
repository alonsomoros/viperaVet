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
import com.alonso.vipera.training.springboot_apirest.exception.InvalidEmailException;
import com.alonso.vipera.training.springboot_apirest.exception.InvalidUsernameException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.WeakPasswordException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.userDto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.userDto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.AuthService;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

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
    private AuthService authService;

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
        AuthResponseDTO result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(registerRequestDTO.getUsername(), result.getUsername());

        // Verify interactions
        verify(passwordEncoder).encode("password123");
        verify(userRepositoryAdapter).save(any(User.class));
    }

    @Test
    void testCreateUser_whenUsernameWithSpaces_shouldThrowUsernameWithSpacesException() {
        registerRequestDTO.setUsername("Juan ");
        assertThrows(InvalidUsernameException.class, () -> authService.register(registerRequestDTO));

        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenUsernameTaken_shouldThrowUsernameTakenException() {
        when(userRepositoryAdapter.existsByUsername(registerRequestDTO.getUsername())).thenReturn(true);

        assertThrows(UsernameTakenException.class, () -> authService.register(registerRequestDTO));

        verify(userRepositoryAdapter).existsByUsername(registerRequestDTO.getUsername());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenEmailTaken_shouldThrowEmailTakenException() {
        when(userRepositoryAdapter.existsByEmail(registerRequestDTO.getEmail())).thenReturn(true);

        assertThrows(EmailTakenException.class, () -> authService.register(registerRequestDTO));

        verify(userRepositoryAdapter).existsByEmail(registerRequestDTO.getEmail());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenPasswordTooShort_shouldThrowWeakPasswordException() {
        registerRequestDTO.setPassword("123"); // Menos de 6 caracteres

        assertThrows(WeakPasswordException.class, () -> authService.register(registerRequestDTO));

        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenInvalidEmail_shouldThrowInvalidEmailException() {
        registerRequestDTO.setEmail("invalid-email");

        assertThrows(InvalidEmailException.class, () -> authService.register(registerRequestDTO));

        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

}
