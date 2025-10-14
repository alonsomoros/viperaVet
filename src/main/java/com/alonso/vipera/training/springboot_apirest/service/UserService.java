package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.EmailNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserMapper userMapper;

    public List<UserOutDTO> getAll() {
        return userRepositoryAdapter.findAll()
                .stream()
                .map(userMapper::toOutDTO)
                .toList();
    }

    public UserOutDTO getById(Long id) {
        User userSaved = userRepositoryAdapter.findById(id).orElseThrow(() -> new IdNotFoundException());
        return userMapper.toOutDTO(userSaved);
    }

    public UserOutDTO getByEmail(String email) {
        User userSaved = userRepositoryAdapter.findByEmail(email).orElseThrow(() -> new EmailNotFoundException());
        return userMapper.toOutDTO(userSaved);
    }

    public UserOutDTO getByUsername(String username) {
        User userSaved = userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException());
        return userMapper.toOutDTO(userSaved);
    }

    // Hacer un update en el futuro
    // public User update(Long id, UpdateUserDTO updateDTO) {
    // User existingUser = getById(id);
    // // Lógica de actualización sin cambiar password
    // return userRepositoryAdapter.save(updatedUser);
    // }

    public void delete(Long id) {
        if (!userRepositoryAdapter.existsById(id)) {
            throw new IdNotFoundException();
        }
        userRepositoryAdapter.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException());
    }

}
