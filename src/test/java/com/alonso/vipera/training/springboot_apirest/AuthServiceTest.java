package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

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
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.VetRegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.AuthResponseDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.UserRoleJpaRepository;
import com.alonso.vipera.training.springboot_apirest.model.user.UserRole;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.AuthServiceImpl;
import com.alonso.vipera.training.springboot_apirest.service.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    // Constants
    private static final String NAME = "Juan";
    private static final String SURNAMES = "Perez Garcia";
    private static final String EMAIL = "juan@gmail.com";
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String JWT_TOKEN = "jwtToken";
    private static final Long USER_ID = 1L;

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

    @Mock
    private UserRoleJpaRepository userRoleJpaRepository;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private VetRegisterRequestDTO registerRequestDTO;
    private User userEntity;
    private UserOutDTO userOutDTO;
    private AuthResponseDTO authResponseDTO;
    private UserRole userRole;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new VetRegisterRequestDTO();
        registerRequestDTO.setName(NAME);
        registerRequestDTO.setSurnames(SURNAMES);
        registerRequestDTO.setEmail(EMAIL);
        registerRequestDTO.setPassword(PASSWORD);

        userEntity = new User();
        userEntity.setId(USER_ID);
        userEntity.setName(NAME);
        userEntity.setSurnames(SURNAMES);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(ENCODED_PASSWORD);

        userRole = new UserRole();
        userRole.setRole(Role.USER);
        userEntity.setUserRole(userRole);

        registerRequestDTO.setRole(Role.USER);

        userOutDTO = new UserOutDTO(USER_ID, NAME, SURNAMES, EMAIL, Role.USER, LocalDateTime.now());

        authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(JWT_TOKEN);
        authResponseDTO.setUser(userOutDTO);
    }

    @Test
    void testPasswordEncryption_shouldEncryptAndMatchPassword() {
        // Arrange
        PasswordEncoder realEncoder = new BCryptPasswordEncoder();

        // Act
        String encoded = realEncoder.encode(PASSWORD);

        // Assert
        assertTrue(realEncoder.matches(PASSWORD, encoded));
    }

    @Test
    void testRegister_whenValidData_shouldCreateUserSuccessfully() {
        // Arrange
        when(userRepositoryAdapter.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userMapper.toEntity(registerRequestDTO)).thenReturn(userEntity);
        when(userRoleJpaRepository.findByRole(Role.USER)).thenReturn(java.util.Optional.of(userRole));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);
        when(userRepositoryAdapter.save(any(User.class))).thenReturn(userEntity);
        when(jwtService.generateToken(userEntity)).thenReturn(JWT_TOKEN);

        // Act
        AuthResponseDTO result = authServiceImpl.registerVet(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(JWT_TOKEN, result.getToken());
        assertEquals(NAME, result.getUser().getName());
        assertEquals(SURNAMES, result.getUser().getSurnames());

        // Verify
        verify(userRepositoryAdapter, times(1)).existsByEmail(EMAIL);
        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verify(userMapper, times(1)).toEntity(registerRequestDTO);
        verify(userRepositoryAdapter, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(userEntity);
    }


    @Test
    void testRegister_whenEmailTaken_shouldThrowEmailTakenException() {
        // Arrange
        when(userRepositoryAdapter.existsByEmail(EMAIL)).thenReturn(true);

        // Act & Assert
        assertThrows(EmailTakenException.class, () -> authServiceImpl.registerVet(registerRequestDTO));

        // Verify
        verify(userRepositoryAdapter, times(1)).existsByEmail(EMAIL);
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testRegister_whenUserNotSaved_shouldThrowUserCreationException() {
        // Arrange
        when(userRepositoryAdapter.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userMapper.toEntity(registerRequestDTO)).thenReturn(userEntity);
        when(userRoleJpaRepository.findByRole(Role.USER)).thenReturn(java.util.Optional.of(userRole));
        when(userRepositoryAdapter.save(any(User.class))).thenReturn(null);

        // Act & Assert
        assertThrows(UserCreationException.class, () -> authServiceImpl.registerVet(registerRequestDTO));

        // Verify
        verify(userRepositoryAdapter, times(1)).existsByEmail(EMAIL);
        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verify(userMapper, times(1)).toEntity(registerRequestDTO);
        verify(userRepositoryAdapter, times(1)).save(any(User.class));
        verify(jwtService, times(0)).generateToken(any());
    }
}
