package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API endpoints para gestionar usuarios")
@RequiredArgsConstructor
public class UserRestController {

        @Autowired
        private UserService userService;

        // GET calls - Obtener todos los usuarios

        @Operation(summary = "Búsqueda con filtros de los usuarios", description = "Devuelve una lista filtrada por los atributos usados, si no hay atributos devuelve una lista de todos los usuarios registrados en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserOutDTO.class)))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @GetMapping
        public ResponseEntity<Page<UserOutDTO>> getUsersWithFilters(
                        @Parameter(description = "ID único del usuario", example = "1") @RequestParam(required = false) Long id,
                        @Parameter(description = "Nombre de usuario único", example = "alonso") @RequestParam(required = false) String username,
                        @Parameter(description = "Dirección de email única", example = "alonso@gmail.com") @RequestParam(required = false) String email,
                        @Parameter(description = "Rol de usuario", example = "OWNER/VET") @RequestParam(required = false) Role role,
                        Pageable pageable) {
                if (id != null || username != null || email != null || role != null) {
                        return ResponseEntity.ok(userService.getUserByFilters(id, username, email, role, pageable));
                } else
                        return ResponseEntity.ok(userService.getAll(pageable));
        }

        // DELETE calls

        @Operation(summary = "Eliminar usuario", description = "Elimina permanentemente un usuario del sistema usando su ID único.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(
                        @Parameter(description = "ID único del usuario a eliminar", example = "1") @PathVariable Long id) {
                userService.delete(id);
                return ResponseEntity.noContent().build();
        }
}