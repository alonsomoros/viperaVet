package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.PetMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.persistence.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.PetRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.SpecieRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepositoryAdapter petRepositoryAdapter;
    private final UserRepositoryAdapter userRepositoryAdapter;
    private final SpecieRepositoryAdapter specieRepositoryAdapter;
    private final BreedRepositoryAdapter breedRepositoryAdapter;
    private final PetMapper petMapper;

    @Override
    public List<PetOutDTO> getPetsByOwnerUsername(String username) {
        return petRepositoryAdapter.findPetsByOwnerUsername(username)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    @Override
    public List<PetOutDTO> getByName(String name) {
        return petRepositoryAdapter.findByName(name)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    @Override
    public List<PetOutDTO> getByBirthDate(Date birthDate) {
        return petRepositoryAdapter.findByBirthDate(birthDate)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    @Override
    public List<PetOutDTO> getByBreedName(String breed) {
        return petRepositoryAdapter.findByBreedName(breed)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    @Override
    public List<PetOutDTO> getBySpecieName(String specie) {
        return petRepositoryAdapter.findBySpecieName(specie)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    @Override
    public List<PetOutDTO> getAll() {
        return petRepositoryAdapter.findAll()
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
    }

    @Override
    public PetOutDTO save(PetInDTO petInDTO, String username) {
        // Buscar Usuario Dueño
        User user = userRepositoryAdapter.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException());
        // Buscar la Especie
        Specie specie = specieRepositoryAdapter.findByName(petInDTO.getSpecie()).orElseThrow(() -> new IdNotFoundException());
        // Buscar la Raza
        Breed breed = breedRepositoryAdapter.findByName(petInDTO.getBreed()).orElseThrow(() -> new IdNotFoundException());
        
        // Convertir DTO a Entity
        Pet pet = petMapper.toEntity(petInDTO);

        // Asignar Usuario Dueño
        pet.setUser(user);
        // Asignar Especie
        pet.setSpecie(specie);
        // Asignar Raza
        pet.setBreed(breed);

        // Guardar Entity
        Pet petSaved = petRepositoryAdapter.save(pet);

        return petMapper.toOutDTO(petSaved);
    }

    @Override
    public void delete(Long id) {
        Pet pet = petRepositoryAdapter.findById(id).orElseThrow(() -> new IdNotFoundException());
        petRepositoryAdapter.delete(pet);
    }

}
