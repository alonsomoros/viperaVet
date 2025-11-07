package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.SpecieOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.BreedService;
import com.alonso.vipera.training.springboot_apirest.service.SpecieService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST de prueba para obtener razas y especies.
 */
@Tag(name = "Prueba", description = "API endpoints de prueba para obtener razas y especies")
@RestController
@RequestMapping("/prueba")
@RequiredArgsConstructor
public class PruebaController {

    private final BreedService breedService;

    private final SpecieService specieService;

    /**
     * Endpoint para obtener una raza por su nombre.
     *
     * @param breedName Nombre de la raza.
     * @return ResponseEntity con los detalles de la raza.
     */
    @GetMapping("/getBreed")
    public ResponseEntity<BreedOutDTO> getBreed(@RequestParam String breedName) {
        return ResponseEntity.status(HttpStatus.OK).body(breedService.findByName(breedName));
    }

    /**
     * Endpoint para obtener una especie por su nombre.
     *
     * @param specieName Nombre de la especie.
     * @return ResponseEntity con los detalles de la especie.
     */
    @GetMapping("/getSpecie")
    public ResponseEntity<SpecieOutDTO> getSpecie(@RequestParam String specieName) {
        return ResponseEntity.status(HttpStatus.OK).body(specieService.findByName(specieName));
    }
}
