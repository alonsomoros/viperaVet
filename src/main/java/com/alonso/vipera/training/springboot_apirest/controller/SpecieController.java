package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.SpecieServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/specie")
@Tag(name = "Specie", description = "API endpoints para gestionar especies de mascotas")
@RequiredArgsConstructor
public class SpecieController {

    @Autowired
    private SpecieServiceImpl specieServiceImpl;

    // GET calls - Obtener todas las especies

    @Operation(summary = "Obtener todas las especies", description = "Devuelve una lista completa de todas las especies registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de especies obtenida con éxito", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SpecieOutDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se necesita un token válido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SpecieOutDTO>> getAllSpecies() {
        return ResponseEntity.ok(specieServiceImpl.getAll());
    }
}
