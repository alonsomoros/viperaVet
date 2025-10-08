package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.EmailTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UserCreationException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameWithSpacesException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private UserRepositoryAdapter userRepositoryAdapter;
    private UserMapper userMapper;

    public UserService(UserRepositoryAdapter userRepositoryAdapter, UserMapper userMapper) {
        this.userRepositoryAdapter = userRepositoryAdapter;
        this.userMapper = userMapper;
    }

    public List<User> getAll() {
        return userRepositoryAdapter.findAll();
    }

    public User getById(Long id) {
        return userRepositoryAdapter.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return userRepositoryAdapter.findByEmail(email).orElse(null);
    }

    public User getByUsername(String username) {
        return userRepositoryAdapter.findByUsername(username).orElse(null);
    }

    @Transactional
    public User create(UserInDTO userInDTO) {
        verifyRegisterInputs(userInDTO);

        User userEntity = userMapper.toEntity(userInDTO);
        User userSaved = userRepositoryAdapter.save(userEntity);

        if (userSaved == null || userSaved.getId() == null) {
            throw new UserCreationException();
        }

        return userSaved;
    }

    public void delete(Long id) {
        if (!userRepositoryAdapter.existsById(id)) {
            throw new IdNotFoundException();
        }
        userRepositoryAdapter.delete(id);
    }

    public boolean existsByUsername(String username) {
        return userRepositoryAdapter.existsByUsername(username);
    }

    public void verifyRegisterInputs(UserInDTO userInDTO) {
        if (userInDTO.getUsername().matches(".*\\s.*")) { // Nombre contiene espacios, tabs, saltos de línea...
            throw new UsernameWithSpacesException();
        }
        if (userRepositoryAdapter.existsByUsername(userInDTO.getUsername())) {
            throw new UsernameTakenException();
        }
        if (userRepositoryAdapter.existsByEmail(userInDTO.getEmail())) {
            throw new EmailTakenException();
        }
    }
}
