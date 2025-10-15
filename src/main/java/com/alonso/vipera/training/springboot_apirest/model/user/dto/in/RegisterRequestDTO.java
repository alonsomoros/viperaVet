package com.alonso.vipera.training.springboot_apirest.model.user.dto.in;

import com.alonso.vipera.training.springboot_apirest.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private User.Role role;
}