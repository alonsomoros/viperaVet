package com.alonso.vipera.training.springboot_apirest.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.service.PetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    @Autowired
    private PetService petService;

    // GET calls

    @GetMapping("/my-pets")
    public ResponseEntity<?> getMyPets(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(petService.getPetsByOwnerUsername(username));
    }

    // POST calls

    @PostMapping("/register")
    public ResponseEntity<?> registerPet(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PetInDTO petInDTO) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(petService.save(petInDTO, username));
    }

    // DEETE calls
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
