package com.alonso.vipera.training.springboot_apirest.model.dto.out;

import com.alonso.vipera.training.springboot_apirest.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserOutDTO {
    private String username;
    private String email;

    public static UserOutDTO toDTO(User user) {
        return new UserOutDTO(user.getUsername(), user.getEmail());
    }
}
