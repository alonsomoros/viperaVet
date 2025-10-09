package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonso.vipera.training.springboot_apirest.exception.EmailTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameWithSpacesException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.userDto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.model.userDto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryAdapter userRepositoryAdapter;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserInDTO userInDTO;
    private User userEntity;
    private UserOutDTO userOutDTO;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = new UserMapper();

        userInDTO = new UserInDTO();
        userInDTO.setUsername("Juan");
        userInDTO.setEmail("juan@gmail.com");
        userInDTO.setPassword("password123");

        userEntity = userMapper.toEntity(userInDTO);
        userEntity.setId(1L);

        userOutDTO = userMapper.toOutDTO(userEntity);
    }

    @Test
    void testCreateUser_whenValidResponse_shouldCreateUserSuccesfully() {
        when(userRepositoryAdapter.existsByUsername(userInDTO.getUsername())).thenReturn(false);
        when(userRepositoryAdapter.existsByEmail(userInDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userInDTO)).thenReturn(userEntity);
        when(userRepositoryAdapter.save(any(User.class))).thenReturn(userEntity);
        when(userMapper.toOutDTO(userEntity)).thenReturn(userOutDTO);

        UserOutDTO result = userService.create(userInDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(userInDTO.getEmail(), result.getEmail());
        assertEquals(userInDTO.getUsername(), result.getUsername());

        verify(userRepositoryAdapter).existsByUsername(userInDTO.getUsername());
        verify(userRepositoryAdapter).existsByEmail(userInDTO.getEmail());
        verify(userRepositoryAdapter).save(any(User.class));
    }

    @Test
    void testCreateUser_whenUsernameWithSpaces_shouldThrowUsernameWithSpacesException() {
        userInDTO.setUsername("Juan ");
        assertThrows(UsernameWithSpacesException.class, () -> userService.create(userInDTO));

        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenUsernameTaken_shouldThrowUsernameTakenException() {
        when(userRepositoryAdapter.existsByUsername(userInDTO.getUsername())).thenReturn(true);

        assertThrows(UsernameTakenException.class, () -> userService.create(userInDTO));

        verify(userRepositoryAdapter).existsByUsername(userInDTO.getUsername());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testCreateUser_whenEmailTaken_shouldThrowEmailTakenException() {
        when(userRepositoryAdapter.existsByEmail(userInDTO.getEmail())).thenReturn(true);

        assertThrows(EmailTakenException.class, () -> userService.create(userInDTO));

        verify(userRepositoryAdapter).existsByEmail(userInDTO.getEmail());
        verify(userRepositoryAdapter, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUser_whenValidResponse_shouldDeleteUserSuccesfully() {
        when(userRepositoryAdapter.existsById(userEntity.getId())).thenReturn(true);
        doNothing().when(userRepositoryAdapter).delete(userEntity.getId());

        userService.delete(userEntity.getId());

        verify(userRepositoryAdapter).existsById(userEntity.getId());
        verify(userRepositoryAdapter).delete(userEntity.getId());
    }

    @Test
    void testDeleteUser_whenIdNotFound_shouldThrowIdNotFoundException() {
        when(userRepositoryAdapter.existsById(userEntity.getId())).thenReturn(false);

        assertThrows(IdNotFoundException.class, () -> userService.delete(userEntity.getId()));

        verify(userRepositoryAdapter).existsById(userEntity.getId());
        verify(userRepositoryAdapter, times(0)).delete(userEntity.getId());
    }

}