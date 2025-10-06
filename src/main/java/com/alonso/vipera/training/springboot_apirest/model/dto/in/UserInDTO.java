package com.alonso.vipera.training.springboot_apirest.model.dto.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInDTO {
    private String username;
    private String password;
    private String email;
    private String createdAt;
}