package com.alonso.vipera.training.springboot_apirest;

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
        registerRequestDTO.setUsername("Juan");
        registerRequestDTO.setEmail("juan@gmail.com");
        registerRequestDTO.setPassword("password123");

        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("Juan");
        userEntity.setEmail("juan@gmail.com");
        userEntity.setPassword("encodedPassword");

        userOutDTO = new UserOutDTO();
        userOutDTO.setId(1L);
        userOutDTO.setUsername("Juan");
        userOutDTO.setEmail("juan@gmail.com");
        userOutDTO.setCreatedAt(LocalDateTime.now());

    }

    @Test
    void testGetAllUsers_whenValidResponse_shouldReturnUserList() {
        // Arrange
        when(userRepositoryAdapter.findAll()).thenReturn(List.of(userEntity));

        // Act
        List<?> users = userServiceImpl.getAll();

        // Assert
        assertNotNull(users);

        // Verify
        verify(userRepositoryAdapter, times(1)).findAll();
    }

    @Test
    void testGetUserById_whenValidResponse_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getById(userEntity.getId());

        // Assert
        assertNotNull(user);

        // Verify
        verify(userRepositoryAdapter, times(1)).findById(userEntity.getId());
    }

    @Test
    void testGetUserById_whenIdNotFound_shouldReturnIdNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findById(userEntity.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> userServiceImpl.getById(userEntity.getId()));

        // Verify
        verify(userRepositoryAdapter, times(1)).findById(userEntity.getId());
    }

    @Test
    void testGetUserByUsername_whenValidResponse_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getByUsername(userEntity.getUsername());

        // Assert
        assertNotNull(user);

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(userEntity.getUsername());
    }

    @Test
    void testGetUserByUsername_whenUsernameNotFound_shouldReturnUsernameNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.getByUsername(userEntity.getUsername()));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(userEntity.getUsername());
    }

    @Test
    void testGetUserByEmail_whenValidResponse_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getByEmail(userEntity.getEmail());

        // Assert
        assertNotNull(user);

        // Verify
        verify(userRepositoryAdapter, times(1)).findByEmail(userEntity.getEmail());
    }

    @Test
    void testGetUserByEmail_whenEmailNotFound_shouldReturnEmailNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmailNotFoundException.class, () -> userServiceImpl.getByEmail(userEntity.getEmail()));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByEmail(userEntity.getEmail());
    }

    @Test
    void testGetUserByAddress_whenValidResponse_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findByAddressContainig(userEntity.getAddress())).thenReturn(List.of());

        // Act
        List<UserOutDTO> users = userServiceImpl.getByAddressContaining(userEntity.getAddress());

        // Assert
        assertNotNull(users);

        // Verify
        verify(userRepositoryAdapter, times(1)).findByAddressContainig(userEntity.getAddress());
    }

    @Test
    void testLoadUserDetails_whenValidResponse_shouldReturnUserDetails() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = userServiceImpl.loadUserByUsername(userEntity.getUsername());

        // Assert
        assertNotNull(userDetails);

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(userEntity.getUsername());
    }

    @Test
    void testLoadUserDetails_whenUsernameNotFound_shouldReturnUsernameNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userServiceImpl.loadUserByUsername(userEntity.getUsername()));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByUsername(userEntity.getUsername());
    }

    @Test
    void testDeleteUser_whenValidResponse_shouldDeleteUserSuccesfully() {
        when(userRepositoryAdapter.existsById(userEntity.getId())).thenReturn(true);
        doNothing().when(userRepositoryAdapter).delete(userEntity.getId());

        userServiceImpl.delete(userEntity.getId());

        verify(userRepositoryAdapter).existsById(userEntity.getId());
        verify(userRepositoryAdapter).delete(userEntity.getId());
    }

    @Test
    void testDeleteUser_whenIdNotFound_shouldThrowIdNotFoundException() {
        when(userRepositoryAdapter.existsById(userEntity.getId())).thenReturn(false);

        assertThrows(IdNotFoundException.class, () -> userServiceImpl.delete(userEntity.getId()));

        verify(userRepositoryAdapter).existsById(userEntity.getId());
        verify(userRepositoryAdapter, times(0)).delete(userEntity.getId());
    }

}