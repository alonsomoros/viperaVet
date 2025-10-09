package com.alonso.vipera.training.springboot_apirest.model.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserInDTO {
    private String username;
    private String password;
    private String email;
}