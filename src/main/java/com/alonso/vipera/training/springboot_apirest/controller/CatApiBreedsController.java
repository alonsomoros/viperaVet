package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.catBreedAPI.dto.in.CatApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.CatApiBreedsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Cat Breeds", description = "API endpoints para gestionar la información de razas de gatos de una API externa")
@RequestMapping("/api/cat-breeds")
@RequiredArgsConstructor
public class CatApiBreedsController {

    private final CatApiBreedsService catApiBreedsService;

    @Operation(summary = "Obtener todas las razas de gatos", description = "Consulta la API externa para obtener la lista completa de razas de gatos disponibles con su información detallada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de razas de cat obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CatApiBreedInDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron razas de cat o la API externa no está disponible", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al consultar la API externa", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CatApiBreedInDTO>> getAllBreeds() {
        List<CatApiBreedInDTO> breeds = catApiBreedsService.getAllCatBreeds();
        return ResponseEntity.ok(breeds);
    }

    @Operation(summary = "Guardar todas las razas de gatos", description = "Obtiene todas las razas de gatos de la API externa y las guarda en la base de datos local para uso posterior")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Razas de gatos guardadas exitosamente en la base de datos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BreedOutDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se pudieron obtener las razas de la API externa", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto al guardar - algunas razas ya existen en la base de datos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar o guardar los datos", content = @Content)
    })
    @PostMapping("/save-all")
    public ResponseEntity<List<BreedOutDTO>> saveAllBreeds() {
        List<BreedOutDTO> savedBreeds = catApiBreedsService.saveAllCatsBreeds();
        return ResponseEntity.ok(savedBreeds);
    }
}
