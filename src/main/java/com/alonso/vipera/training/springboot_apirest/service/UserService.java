package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

public interface UserService {

    List<UserOutDTO> getAll();

    UserOutDTO getById(Long id);

    UserOutDTO getByEmail(String email);

    UserOutDTO getByUsername(String username);

    List<UserOutDTO> getByAddressContaining(String address);

    void delete(Long id);
    
}
