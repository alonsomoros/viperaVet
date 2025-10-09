package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.exception.EmailTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameWithSpacesException;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.userDto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.model.userDto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.UserService;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    // GET calls

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getById(id));
    }

    // Esto se usará más como filtro ya que no interesa buscar un usuario por
    // username como identificador.
    @GetMapping("/by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam(required = false) String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetro 'username' es obligatorio");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByUsername(username));
    }

    // POST calls

    @PostMapping("/create")
    public ResponseEntity<?> createUsuario(@RequestBody UserInDTO userInDTO) {
        return handleRegisterOperation(() -> userService.create(userInDTO));
    }

    // DELETE calls

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID no encontrado");
        }
    }

    private ResponseEntity<?> handleRegisterOperation(Supplier<UserOutDTO> operation) {
        try {
            UserOutDTO createdUser = operation.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UsernameWithSpacesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nombre de usuario no puede contener espacios");
        } catch (UsernameTakenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nombre de usuario ya está en uso");
        } catch (EmailTakenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correo electrónico ya usado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}