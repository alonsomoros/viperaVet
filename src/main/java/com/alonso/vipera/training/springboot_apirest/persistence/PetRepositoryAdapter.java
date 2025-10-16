package com.alonso.vipera.training.springboot_apirest.persistence;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PetRepositoryAdapter {

    private PetRepository petRepository;

    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> findPetsByOwnerUsername(String username) {
        return petRepository.findByUserUsername(username)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Pet> findByName(String name) {
        return petRepository.findByName(name)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Pet> findByBirthDate(java.sql.Date birthDate) {
        return petRepository.findByBirthDate(birthDate)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Pet> findByBreed(String breed) {
        return petRepository.findByBreed(breed)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Pet> findBySpecie(String specie) {
        return petRepository.findBySpecie(specie)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void delete(Pet pet) {
        petRepository.delete(pet);
    }

}
