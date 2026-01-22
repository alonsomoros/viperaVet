package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.UserUpdateDTO;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar usuarios.
 * Proporciona endpoints para obtener y eliminar usuarios.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API endpoints para gestionar usuarios")
@RequiredArgsConstructor
public class UserRestController {

        private final UserService userService;

        // GET calls - Obtener todos los usuarios

         /**
          * Endpoint para obtener usuarios según filtros de búsqueda.
          *
          * @param id       ID del usuario (opcional).
          * @param name     Nombre (opcional).
          * @param surnames Apellidos (opcional).
          * @param email    Dirección de email (opcional).
          * @param role     Rol del usuario (opcional).
          * @param pageable Parámetros de paginación.
          * @return ResponseEntity con la lista de usuarios que coinciden con los
          *         filtros.
          */
         @Operation(summary = "Búsqueda con filtros de los usuarios", description = "Devuelve una lista filtrada por los atributos usados, si no hay atributos devuelve una lista de todos los usuarios registrados en el sistema.")
         @ApiResponses(value = {
                         @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserOutDTO.class)))),
                         @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
                         @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
         })
         @GetMapping
         public ResponseEntity<Page<UserOutDTO>> getUsersWithFilters(
                         @Parameter(description = "ID único del usuario", example = "1") @RequestParam(required = false) Long id,
                         @Parameter(description = "Nombre", example = "alonso") @RequestParam(required = false) String name,
                         @Parameter(description = "Apellidos", example = "moros") @RequestParam(required = false) String surnames,
                         @Parameter(description = "Dirección de email única", example = "alonso@gmail.com") @RequestParam(required = false) String email,
                         @Parameter(description = "Rol de usuario", example = "USER/VET") @RequestParam(required = false) Role role,
                         Pageable pageable) {
                 if (id != null || name != null || surnames != null || email != null || role != null) {
                         return ResponseEntity.ok(userService.getUserByFilters(id, name, surnames, email, role, pageable));
                 } else
                         return ResponseEntity.ok(userService.getAll(pageable));
         }

         /**
          * Endpoint para obtener un usuario por su ID.
          *
          * @param id ID único del usuario a buscar.
          * @return ResponseEntity con el DTO del usuario encontrado.
          */
         @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario en el sistema usando su ID único y devuelve su información detallada.")
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

        // DELETE calls

        /**
         * Endpoint para eliminar un usuario por su ID.
         *
         * @param id ID único del usuario a eliminar.
         * @return ResponseEntity sin contenido si la eliminación es exitosa.
         */
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

        // PATCH calls

        /**
         * Endpoint para actualizar un usuario existente.
         *
         * @param id            ID único del usuario a actualizar.
         * @param userUpdateDTO Datos para actualizar el usuario.
         * @param userDetails   Detalles del usuario autenticado que realiza la
         *                      solicitud.
         * @return ResponseEntity con el DTO del usuario actualizado.
         */
        @PatchMapping("/{id}")
        public ResponseEntity<UserOutDTO> updateUser(
                        @PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO,
                        @AuthenticationPrincipal UserDetails userDetails) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(userService.updateUser(id, userUpdateDTO, userDetails.getUsername()));
        }

}