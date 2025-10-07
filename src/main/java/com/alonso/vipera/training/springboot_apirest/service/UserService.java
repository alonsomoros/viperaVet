package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameWithSpacesException;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private UserRepositoryAdapter userRepositoryAdapter;

    public UserService(UserRepositoryAdapter userRepositoryAdapter) {
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    public List<User> getAll(){
        return userRepositoryAdapter.findAll();
    }

    public User getById(Long id){
        return userRepositoryAdapter.findById(id).orElse(null);
    }

    public User getByEmail(String email){
        return userRepositoryAdapter.findByEmail(email).orElse(null);
    }

    public User getByUsername(String username){
        return userRepositoryAdapter.findByUsername(username).orElse(null);
    }

    @Transactional
    public void create(UserInDTO userInDTO){
        
        verifyRegisterInputs(userInDTO);
        User user = User.builder()
                .username(userInDTO.getUsername())
                .email(userInDTO.getEmail())
                .password(userInDTO.getPassword())
                .build();

         userRepositoryAdapter.save(user);

        if (user == null) {
            throw new RuntimeException("Error creando el usuario en la base de datos");
        }
    }

    public void delete(Long id){
        if (!userRepositoryAdapter.existsById(id)) {
            throw new UsernameNotFoundException();
        }
        userRepositoryAdapter.delete(id);
    }

    public boolean existsByUsername(String username){
        return userRepositoryAdapter.existsByUsername(username);
    }

    public void verifyRegisterInputs(UserInDTO userInDTO){
        if (userInDTO.getUsername().matches(".*\\s.*")) { // Nombre contiene espacios, tabs, saltos de línea...
            throw new UsernameWithSpacesException();
        }
        if (userRepositoryAdapter.existsByUsername(userInDTO.getUsername())) {
            throw new UsernameTakenException();
        }
    }
}
