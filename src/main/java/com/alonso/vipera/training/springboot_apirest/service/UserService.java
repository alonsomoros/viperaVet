package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

public interface UserService {

    Page<UserOutDTO> getAll(Pageable pageable);

    UserOutDTO getById(Long id);

    UserOutDTO getByEmail(String email);

    UserOutDTO getByUsername(String username);

    Page<UserOutDTO> getUserByFilters(Long id, String username, String email, Role role, Pageable pageable);

    void delete(Long id);
    
}
