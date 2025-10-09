package com.alonso.vipera.training.springboot_apirest.model.userDto.out;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserOutDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
