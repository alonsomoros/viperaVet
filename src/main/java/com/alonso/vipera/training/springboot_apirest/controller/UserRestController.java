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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class UserRestController {
    @Autowired
    private UserService userService;

    // GET calls

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // Esto se usará más como filtro ya que no interesa buscar un usuario por username como identificador.
    @GetMapping("/users")
    public List<User> getUserByUsername(@RequestParam(required = false) String username) {

        if (username == null || username.isEmpty()) {
            return userService.getAll();
        }

        return userService.getAll().stream()
                .filter(user -> user.getUsername().contains(username))
                .collect(Collectors.toList());

    }

    // POST calls

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    // DELETE calls

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}