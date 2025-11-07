package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.BreedService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar las razas de mascotas.
 * Proporciona endpoints para obtener información sobre las razas.
 */
@RestController
@RequestMapping("/breeds")
@Tag(name = "Breeds", description = "API endpoints para gestionar razas de mascotas")
@RequiredArgsConstructor
public class BreedController {

    private final BreedService breedService;

    // GET calls - Obtener todas las razas

    /**
     * Endpoint para obtener todas las razas de mascotas.
     *
     * @return ResponseEntity con la lista de todas las razas.
     */
    @Operation(summary = "Obtener todas las razas", description = "Devuelve una lista completa de todas las razas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de razas obtenida con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BreedOutDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BreedOutDTO>> getAllBreeds() {
        return ResponseEntity.ok(breedService.getAllBreeds());
    }
}
