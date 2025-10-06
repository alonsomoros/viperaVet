package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class UserRestController {

    // Simulación de la "base de datos"
    private final List<User> allUsers = List.of(
            new User(1L, "user1", "password1", "user1@gmail.com"),
            new User(2L, "user2", "password2", "user2@gmail.com"),
            new User(3L, "user3", "password3", "user3@gmail.com"));

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Map<String, Object> getUsers() {
        Map<String, Object> body = new HashMap<String, Object>();

        UserOutDTO userDTO1 = UserOutDTO.toDTO(allUsers.get(0));
        UserOutDTO userDTO2 = UserOutDTO.toDTO(allUsers.get(1));
        UserOutDTO userDTO3 = UserOutDTO.toDTO(allUsers.get(2));

        body.put("user1", userDTO1);
        body.put("user2", userDTO2);
        body.put("user3", userDTO3);

        return body;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = new User(id, "user" + id, "password" + id, "user" + id + "@gmail.com");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public List<User> getUserByUsername(@RequestParam(required = false) String username) {

        if (username == null || username.isEmpty()) {
            return allUsers;
        }

        return allUsers.stream()
                .filter(user -> user.getUsername().contains(username))
                .collect(Collectors.toList());

    }

    @PostMapping("create")
    public User createUser(@RequestBody User user) {
        return user;
    }

}