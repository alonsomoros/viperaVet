package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.out.UserOutDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api")
public class UserRestController {

@GetMapping("/users")
public Map<String, Object> getUsers() {
    Map<String, Object> body = new HashMap<String,Object>();

    User user = new User(1L ,"user1", "password1", "user1@gmail.com");
    UserOutDTO userDTO = UserOutDTO.toDTO(user);

    body.put("username", userDTO);

    return body;
}

@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = new User(id, "user" + id, "password" + id, "user" + id + "@gmail.com");
   return ResponseEntity.ok(user);
}


}