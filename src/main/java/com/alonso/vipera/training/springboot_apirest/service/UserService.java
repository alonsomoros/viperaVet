package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

public interface UserService {

    List<UserOutDTO> getAll();

    UserOutDTO getById(Long id);

    UserOutDTO getByEmail(String email);

    UserOutDTO getByUsername(String username);

    List<UserOutDTO> getUserByFilters(Long id, String username, String email, Role role);

    void delete(Long id);
    
}
