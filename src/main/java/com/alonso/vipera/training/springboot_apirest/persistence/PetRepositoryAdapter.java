package com.alonso.vipera.training.springboot_apirest.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

import lombok.AllArgsConstructor;

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

    public List<Pet> findByBreedName(String breed) {
        return petRepository.findByBreedName(breed)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Pet> findBySpecieName(String specie) {
        return petRepository.findBySpecieName(specie)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public Page<Pet> findByFilters(Long pet_id, String name, Long breed_id, Long specie_id, Pageable pageable) {
        return petRepository.findByFilters(pet_id, name, breed_id, specie_id, pageable);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void delete(Pet pet) {
        petRepository.delete(pet);
    }

}
