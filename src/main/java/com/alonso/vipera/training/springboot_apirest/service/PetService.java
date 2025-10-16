package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.PetMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.persistence.PetRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepositoryAdapter petRepositoryAdapter;
    private final UserRepositoryAdapter userRepositoryAdapter;
    private final PetMapper petMapper;

    public List<PetOutDTO> getPetsByOwnerUsername(String username) {
        return petRepositoryAdapter.findPetsByOwnerUsername(username)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    public List<PetOutDTO> getByName(String name) {
        return petRepositoryAdapter.findByName(name)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    public List<PetOutDTO> getByBirthDate(Date birthDate) {
        return petRepositoryAdapter.findByBirthDate(birthDate)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    public List<PetOutDTO> getByBreed(String breed) {
        return petRepositoryAdapter.findByBreed(breed)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    public List<PetOutDTO> getBySpecie(String specie) {
        return petRepositoryAdapter.findBySpecie(specie)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    public List<PetOutDTO> getAll() {
        return petRepositoryAdapter.findAll()
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    public PetOutDTO save(PetInDTO petInDTO, String username) {
        // Buscar Usuario Dueño
        User user = userRepositoryAdapter.findByUsername(username).get();

        // Convertir DTO a Entity
        Pet pet = petMapper.toEntity(petInDTO);

        // Asignar Usuario Dueño
        pet.setUser(user);

        // Guardar Entity
        Pet petSaved = petRepositoryAdapter.save(pet);

        return petMapper.toOutDTO(petSaved);
    }

    public void delete(Long id){
        Pet pet = petRepositoryAdapter.findById(id).orElseThrow(() -> new IdNotFoundException());
        petRepositoryAdapter.delete(pet);
    }

}
