package com.alonso.vipera.training.springboot_apirest;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.userDto.in.RegisterRequestDTO;
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

    private RegisterRequestDTO userInDTO;
    private User userEntity;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = new UserMapper();

        userInDTO = new RegisterRequestDTO();
        userInDTO.setUsername("Juan");
        userInDTO.setEmail("juan@gmail.com");
        userInDTO.setPassword("password123");

        userEntity = userMapper.toEntity(userInDTO);
        userEntity.setId(1L);
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