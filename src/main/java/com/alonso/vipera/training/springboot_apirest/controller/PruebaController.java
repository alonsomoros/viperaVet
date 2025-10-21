package com.alonso.vipera.training.springboot_apirest.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@Tag(name = "Prueba", description = "API endpoints de prueba para obtener razas y especies")
@RestController
@RequestMapping("/prueba")
public class PruebaController {
    
    @Autowired
    private BreedService breedService;

    @Autowired
    private SpecieService specieService;

    @GetMapping("/getBreed")
    public ResponseEntity<BreedOutDTO> getBreed(@RequestParam String breedName){
        return ResponseEntity.status(HttpStatus.OK).body(breedService.findByName(breedName));
    }

    @GetMapping("/getSpecie")
    public ResponseEntity<SpecieOutDTO> getSpecie(@RequestParam String specieName){
        return ResponseEntity.status(HttpStatus.OK).body(specieService.findByName(specieName));
    }
}
