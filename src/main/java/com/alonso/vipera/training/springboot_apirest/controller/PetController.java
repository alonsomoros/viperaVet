package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.PetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar las mascotas.
 * Proporciona endpoints para obtener, registrar y borrar mascotas.
 */
@RestController
@RequestMapping("/pets")
@Tag(name = "Pets", description = "API endpoints para gestionar las mascotas")
@RequiredArgsConstructor
public class PetController {

    @Autowired
    private PetService petService;

    // GET calls

    /**
     * Endpoint para obtener las mascotas del usuario autenticado
     *
     * @param userDetails Detalles del usuario autenticado.
     * @return ResponseEntity con la lista de mascotas del usuario.
     */
    @Operation(summary = "Obtener mis mascotas", description = "Devuelve una lista de todas las mascotas que pertenecen al usuario autenticado a través de su token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mascotas encontradas con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PetOutDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @GetMapping("/my-pets")
    public ResponseEntity<List<PetOutDTO>> getMyPets(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(petService.getPetsByOwnerUsername(username));
    }

    /**
     * Endpoint para obtener mascotas según filtros de búsqueda.
     *
     * @param id        ID de la mascota (opcional).
     * @param name      Nombre de la mascota (opcional).
     * @param breed_id  ID de la raza de la mascota (opcional).
     * @param specie_id ID de la especie de la mascota (opcional).
     * @param pageable  Parámetros de paginación.
     * @return ResponseEntity con la lista de mascotas que coinciden con los
     *         filtros.
     */
    @Operation(summary = "Buscar mascotas con filtros", description = "Devuelve una lista de mascotas que coinciden con los parámetros de búsqueda proporcionados. Si no se proporcionan parámetros, devuelve todas las mascotas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascotas encontradas con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = PetOutDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<PetOutDTO>> getPetsByFilters(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long breed_id,
            @RequestParam(required = false) Long specie_id,
            Pageable pageable) {

        if (id != null || name != null || breed_id != null || specie_id != null) {
            return ResponseEntity.ok(petService.getPetByFilters(id, name, breed_id, specie_id, pageable));
        }
        return ResponseEntity.ok(petService.getAll(pageable));
    }

    // POST calls

    /**
     * Endpoint para registrar una nueva mascota.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @param petInDTO    DTO que contiene la información de la mascota a registrar.
     * @return ResponseEntity con los detalles de la mascota registrada.
     */
    @Operation(summary = "Registrar una mascota", description = "Permite registrar una mascota asociada al usuario autenticado a través de su token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota registrada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = PetOutDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PetOutDTO> registerPet(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PetInDTO petInDTO) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(petService.save(petInDTO, username));
    }

    // DELETE calls

    /**
     * Endpoint para borrar una mascota.
     *
     * @param id ID de la mascota a borrar.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Borrar una mascota", description = "Permite borrar una mascota asociada al usuario autenticado a través de su token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota borrada con éxito", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
