package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.in.DogApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.DogApiBreedsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Dog Breeds", description = "API endpoints para gestionar la información de razas de perros de una API externa")
@RequestMapping("/api/dog-breeds")
@RequiredArgsConstructor
public class DogApiBreedsController {

    private final DogApiBreedsService dogApiBreedsService;

    @Operation(summary = "Obtener todas las razas de perros", description = "Consulta la API externa para obtener la lista completa de razas de perros disponibles con su información detallada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de razas de perros obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DogApiBreedInDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron razas de perros o la API externa no está disponible", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al consultar la API externa", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DogApiBreedInDTO>> getAllBreeds() {
        List<DogApiBreedInDTO> breeds = dogApiBreedsService.getAllDogBreeds();
        return ResponseEntity.ok(breeds);
    }

    @Operation(summary = "Guardar todas las razas de perros", description = "Obtiene todas las razas de perros de la API externa y las guarda en la base de datos local para uso posterior")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Razas de perros guardadas exitosamente en la base de datos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BreedOutDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se pudieron obtener las razas de la API externa", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto al guardar - algunas razas ya existen en la base de datos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar o guardar los datos", content = @Content)
    })
    @PostMapping("/save-all")
    public ResponseEntity<List<BreedOutDTO>> saveAllBreeds() {
        List<BreedOutDTO> savedBreeds = dogApiBreedsService.saveAllDogsBreeds();
        return ResponseEntity.ok(savedBreeds);
    }
}
