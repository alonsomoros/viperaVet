package com.alonso.vipera.training.springboot_apirest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.DogApiBreedDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;
import com.alonso.vipera.training.springboot_apirest.service.DogApiBreedsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Dog Breeds", description = "External API endpoints for retrieving dog breed information")
@RequestMapping("/api/dog-breeds")
@RequiredArgsConstructor
public class DogApiBreedsController {

    private final DogApiBreedsService dogApiBreedsService;

    @GetMapping
    public ResponseEntity<List<DogApiBreedDTO>> getAllBreeds() {
        List<DogApiBreedDTO> breeds = dogApiBreedsService.getAllDogBreeds();
        return ResponseEntity.ok(breeds);
    }

    @PostMapping("/save-all")
    public ResponseEntity<List<BreedOutDTO>> saveAllBreeds() {
        List<BreedOutDTO> savedBreeds = dogApiBreedsService.saveAllDogsBreeds();
        return ResponseEntity.ok(savedBreeds);
    }
}
