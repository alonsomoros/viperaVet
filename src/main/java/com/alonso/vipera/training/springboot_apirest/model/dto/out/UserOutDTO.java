package com.alonso.vipera.training.springboot_apirest.model.dto.out;

import com.alonso.vipera.training.springboot_apirest.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOutDTO {
    private String username;
    private String email;

    public UserOutDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static UserOutDTO toDTO(User user) {
        return new UserOutDTO(user.getUsername(), user.getEmail());
    }
}
