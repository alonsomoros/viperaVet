package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.exception.UsernameTakenException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameWithSpacesException;
import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.model.dto.in.UserInDTO;
import com.alonso.vipera.training.springboot_apirest.service.UserService;

@RestController
@RequestMapping("/users")
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

    // Esto se usará más como filtro ya que no interesa buscar un usuario por
    // username como identificador.
    @GetMapping("/by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam(required = false) String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Parámetro 'username' es obligatorio");
        }
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    // POST calls

    @PostMapping("/create")
    public ResponseEntity<?> createUsuario(@RequestBody UserInDTO userInDTO) {
        return handleRegisterOperation(() -> userService.create(userInDTO));
    }

    // DELETE calls

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> handleRegisterOperation(Runnable operation) {
        try {
            operation.run();
            return ResponseEntity.ok("Usuario creado exitosamente");
        } catch (UsernameWithSpacesException e) {
            return ResponseEntity.badRequest().body("Nombre de usuario no puede contener espacios");
        } catch (UsernameTakenException e) {
            return ResponseEntity.badRequest().body("Nombre de usuario ya está en uso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}