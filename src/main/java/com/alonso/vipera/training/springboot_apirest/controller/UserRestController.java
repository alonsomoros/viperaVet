package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista completa de todos los usuarios registrados en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserOutDTO.class)))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @GetMapping
        public ResponseEntity<List<UserOutDTO>> getAllUsers() {
                return ResponseEntity.ok(userService.getAll());
        }

        // GET calls - Buscar por ID único

        @Operation(summary = "Obtener usuario por ID", description = "Busca y devuelve un usuario específico usando su identificador único.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOutDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<UserOutDTO> getUserById(
                        @Parameter(description = "ID único del usuario", example = "1") @PathVariable Long id) {
                return ResponseEntity.ok(userService.getById(id));
        }

        // GET calls - Buscar por username único

        @Operation(summary = "Obtener usuario por nombre de usuario", description = "Busca y devuelve un usuario específico usando su nombre de usuario único.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOutDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @GetMapping("/username/{username}")
        public ResponseEntity<UserOutDTO> getUserByUsername(
                        @Parameter(description = "Nombre de usuario único", example = "john_doe") @PathVariable String username) {
                return ResponseEntity.ok(userService.getByUsername(username));
        }

        // GET calls - Buscar por email único

        @Operation(summary = "Obtener usuario por email", description = "Busca y devuelve un usuario específico usando su dirección de email única.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOutDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @GetMapping("/email/{email}")
        public ResponseEntity<UserOutDTO> getUserByEmail(
                        @Parameter(description = "Dirección de email única", example = "john@example.com") @PathVariable String email) {
                return ResponseEntity.ok(userService.getByEmail(email));
        }

        // GET calls - Búsquedas múltiples por dirección

        @Operation(summary = "Buscar usuarios por filtros", description = "Busca usuarios que coincidan con los filtros proporcionados. "
                        +
                        "Puede buscar por dirección. Si no se proporcionan filtros, devuelve todos los usuarios.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Búsqueda realizada con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserOutDTO.class)))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
        })
        @GetMapping("/search")
        public ResponseEntity<List<UserOutDTO>> searchUsers(
                        @Parameter(description = "Búsqueda de la dirección", example = "Madrid") @RequestParam(required = false) String address) {

                if (address != null && !address.isBlank()) {
                        return ResponseEntity.ok(userService.getByAddressContaining(address));
                }

                // Si no hay parámetros, devolver todos
                return ResponseEntity.ok(userService.getAll());
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