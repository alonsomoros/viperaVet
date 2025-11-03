package com.alonso.vipera.training.springboot_apirest.persistence.adapter;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.PetJpaRepository;
import com.alonso.vipera.training.springboot_apirest.persistence.repository.PetRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PetRepositoryAdapter implements PetRepository {

    private PetJpaRepository petRepository;

    @Override
    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    @Override
    public List<Pet> findPetsByOwnerUsername(String username) {
        return petRepository.findByUserUsername(username)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<Pet> findByName(String name) {
        return petRepository.findByName(name)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<Pet> findByBirthDate(Date birthDate) {
        return petRepository.findByBirthDate(birthDate)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<Pet> findByBreedName(String breed) {
        return petRepository.findByBreedName(breed)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<Pet> findBySpecieName(String specie) {
        return petRepository.findBySpecieName(specie)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    @Override
    public Page<Pet> findByFilters(Long pet_id, String name, Long breed_id, Long specie_id, Pageable pageable) {
        return petRepository.findByFilters(pet_id, name, breed_id, specie_id, pageable);
    }

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public void delete(Pet pet) {
        petRepository.delete(pet);
    }

}
