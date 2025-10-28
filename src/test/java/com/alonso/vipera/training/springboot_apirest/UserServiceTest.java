package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.alonso.vipera.training.springboot_apirest.exception.EmailNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String USERNAME = "Juan";
    private static final String EMAIL = "juan@gmail.com";
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String ADDRESS = "Calle Falsa 123";
    private static final Long USER_ID = 1L;
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    @Mock
    private UserRepositoryAdapter userRepositoryAdapter;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private RegisterRequestDTO registerRequestDTO;
    private User userEntity;
    private UserOutDTO userOutDTO;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername(USERNAME);
        registerRequestDTO.setEmail(EMAIL);
        registerRequestDTO.setPassword(PASSWORD);

        userEntity = new User();
        userEntity.setId(USER_ID);
        userEntity.setUsername(USERNAME);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(ENCODED_PASSWORD);
        userEntity.setAddress(ADDRESS);

        userOutDTO = new UserOutDTO();
        userOutDTO.setId(USER_ID);
        userOutDTO.setUsername(USERNAME);
        userOutDTO.setEmail(EMAIL);
        userOutDTO.setCreatedAt(CREATED_AT);
    }

    @Test
    void testGetAllUsers_whenUsersFound_shouldReturnUserList() {
        // Arrange
        when(userRepositoryAdapter.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        List<UserOutDTO> users = userServiceImpl.getAll();

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());

        // Verify
        verify(userRepositoryAdapter, times(1)).findAll();
        verify(userMapper, times(1)).toOutDTO(userEntity);
    }

    @Test
    void testGetAllUsers_whenNoUsersFound_shouldReturnEmptyList() {
        // Arrange
        when(userRepositoryAdapter.findAll()).thenReturn(List.of());

        // Act
        List<?> users = userServiceImpl.getAll();

        // Assert
        assertNotNull(users);
        assertEquals(0, users.size());

        // Verify
        verify(userRepositoryAdapter, times(1)).findAll();
    }

    @Test
    void testGetUserById_whenUserFound_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getById(USER_ID);

        // Assert
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());

        // Verify
        verify(userRepositoryAdapter, times(1)).findById(USER_ID);
        verify(userMapper, times(1)).toOutDTO(userEntity);
    }

    @Test
    void testGetUserById_whenIdNotFound_shouldThrowIdNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findById(USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> userServiceImpl.getById(USER_ID));

        // Verify
        verify(userRepositoryAdapter, times(1)).findById(USER_ID);
        verify(userMapper, times(0)).toOutDTO(userEntity);
    }

    @Test
    void testGetUserByUsername_whenUserFound_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getByUsername(USERNAME);

        // Assert
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
        verify(userMapper, times(1)).toOutDTO(userEntity);
    }

    @Test
    void testGetUserByUsername_whenUsernameNotFound_shouldThrowUsernameNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.getByUsername(USERNAME));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
        verify(userMapper, times(0)).toOutDTO(userEntity);
    }

    @Test
    void testGetUserByEmail_whenUserFound_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getByEmail(EMAIL);

        // Assert
        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());

        // Verify
        verify(userRepositoryAdapter, times(1)).findByEmail(EMAIL);
        verify(userMapper, times(1)).toOutDTO(userEntity);
    }

    @Test
    void testGetUserByEmail_whenEmailNotFound_shouldThrowEmailNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmailNotFoundException.class, () -> userServiceImpl.getByEmail(EMAIL));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByEmail(EMAIL);
        verify(userMapper, times(0)).toOutDTO(userEntity);
    }

    @Test
    void testLoadUserDetails_whenUserFound_shouldReturnUserDetails() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = userServiceImpl.loadUserByUsername(USERNAME);

        // Assert
        assertNotNull(userDetails);
        assertEquals(USERNAME, userDetails.getUsername());

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testLoadUserDetails_whenUsernameNotFound_shouldThrowUsernameNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.loadUserByUsername(USERNAME));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testDeleteUser_whenUserExists_shouldDeleteUserSuccessfully() {
        // Arrange
        when(userRepositoryAdapter.existsById(USER_ID)).thenReturn(true);
        doNothing().when(userRepositoryAdapter).delete(USER_ID);

        // Act
        userServiceImpl.delete(USER_ID);

        // Verify
        verify(userRepositoryAdapter, times(1)).existsById(USER_ID);
        verify(userRepositoryAdapter, times(1)).delete(USER_ID);
    }

    @Test
    void testDeleteUser_whenIdNotFound_shouldThrowIdNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.existsById(USER_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> userServiceImpl.delete(USER_ID));

        // Verify
        verify(userRepositoryAdapter, times(1)).existsById(USER_ID);
        verify(userRepositoryAdapter, times(0)).delete(USER_ID);
    }
}