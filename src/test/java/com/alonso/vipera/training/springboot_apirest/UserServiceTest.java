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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import com.alonso.vipera.training.springboot_apirest.exception.EmailNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.RegisterRequestDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String NAME = "Juan";
    private static final String SURNAMES = "Perez Garcia";
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

    private Pageable testPageable;

    private RegisterRequestDTO registerRequestDTO;
    private User userEntity;
    private UserOutDTO userOutDTO;

    @BeforeEach
    void setUp() {
        testPageable = PageRequest.of(0, 10);

        registerRequestDTO = new RegisterRequestDTO();
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
        userEntity.setAddress(ADDRESS);

        userOutDTO = new UserOutDTO();
        userOutDTO.setId(USER_ID);
        userOutDTO.setName(NAME);
        userOutDTO.setSurnames(SURNAMES);
        userOutDTO.setEmail(EMAIL);
        userOutDTO.setCreatedAt(CREATED_AT);
    }

    @Test
    void testGetAllUsers_whenUsersFound_shouldReturnUserPage() {
        List<User> userList = List.of(userEntity);
        Page<User> userPage = new PageImpl<>(userList, (org.springframework.data.domain.Pageable) testPageable, userList.size());

        // Arrange
        when(userRepositoryAdapter.findAll(testPageable)).thenReturn(userPage);
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        Page<UserOutDTO> users = userServiceImpl.getAll(testPageable);

        // Assert
        assertNotNull(users);
        assertEquals(1, users.getContent().size());

        // Verify
        verify(userRepositoryAdapter, times(1)).findAll(testPageable);
        verify(userMapper, times(1)).toOutDTO(userEntity);
    }

    @Test
    void testGetAllUsers_whenNoUsersFound_shouldReturnEmptyPage() {
        // Arrange
        when(userRepositoryAdapter.findAll(testPageable)).thenReturn(Page.empty());

        // Act
        Page<UserOutDTO> users = userServiceImpl.getAll(testPageable);

        // Assert
        assertNotNull(users);
        assertEquals(0, users.getSize());

        // Verify
        verify(userRepositoryAdapter, times(1)).findAll(testPageable);
    }

    @Test
    void testGetUserById_whenUserFound_shouldReturnUser() {
        // Arrange
        when(userRepositoryAdapter.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        // Act
        UserOutDTO user = userServiceImpl.getById(USER_ID);

        // Assert
        assertEquals(NAME, user.getName());
        assertEquals(SURNAMES, user.getSurnames());

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
        when(userRepositoryAdapter.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = userServiceImpl.loadUserByUsername(EMAIL);

        // Assert
        assertNotNull(userDetails);
        assertEquals(EMAIL, userDetails.getUsername());

        // Verify
        verify(userRepositoryAdapter, times(1)).findByEmail(EMAIL);
    }

    @Test
    void testLoadUserDetails_whenEmailNotFound_shouldThrowEmailNotFoundException() {
        // Arrange
        when(userRepositoryAdapter.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmailNotFoundException.class, () -> userServiceImpl.loadUserByUsername(EMAIL));

        // Verify
        verify(userRepositoryAdapter, times(1)).findByEmail(EMAIL);
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