package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pets")
@Tag(name = "Pets", description = "API endpoints for managing pets")
@RequiredArgsConstructor
public class PetController {

    @Autowired
    private PetService petService;

    // GET calls

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

    @Operation(summary = "Buscar mascotas con filtros", description = "Devuelve una lista de mascotas que coinciden con los parámetros de búsqueda proporcionados. Si no se proporcionan parámetros, devuelve todas las mascotas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascotas encontradas con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = PetOutDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<PetOutDTO>> searchPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String specie) {

        if (name != null) {
            return ResponseEntity.ok(petService.getByName(name));
        }
        if (breed != null) {
            return ResponseEntity.ok(petService.getByBreed(breed));
        }
        if (specie != null) {
            return ResponseEntity.ok(petService.getBySpecie(specie));
        }

        // Si no hay parámetros, devolver todos
        return ResponseEntity.ok(petService.getAll());
    }

    // POST calls

    @Operation(summary = "Registrar una mascota", description = "Permite registrar una mascota asociada al usuario autenticado a través de su token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota registrada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = PetOutDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<PetOutDTO> registerPet(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PetInDTO petInDTO) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(petService.save(petInDTO, username));
    }

    // DELETE calls

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
