package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.PetMapper;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetUpdateDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.BreedRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.PetRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.SpecieRepositoryAdapter;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.debug("Buscando mascotas del usuario dueño: {}", username);
        List<PetOutDTO> pets = petRepositoryAdapter.findPetsByOwnerUsername(username)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
        log.debug("Se han encontrado {} mascotas para el usuario: {}", pets.size(), username);
        return pets;
    }

    @Override
    public List<PetOutDTO> getByName(String name) {
        log.debug("Buscando mascotas con el nombre: {}", name);
        List<PetOutDTO> pets = petRepositoryAdapter.findByName(name)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
        log.debug("Se han encontrado {} mascotas con el nombre: {}", pets.size(), name);
        return pets;
    }

    @Override
    public List<PetOutDTO> getByBirthDate(Date birthDate) {
        log.debug("Buscando mascotas con la fecha de nacimiento: {}", birthDate);
        List<PetOutDTO> pets = petRepositoryAdapter.findByBirthDate(birthDate)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
        log.debug("Se han encontrado {} mascotas con la fecha de nacimiento: {}", pets.size(), birthDate);
        return pets;
    }

    @Override
    public List<PetOutDTO> getByBreedName(String breed) {
        log.debug("Buscando mascotas de la raza: {}", breed);
        List<PetOutDTO> pets = petRepositoryAdapter.findByBreedName(breed)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
        log.debug("Se han encontrado {} mascotas de la raza: {}", pets.size(), breed);
        return pets;
    }

    @Override
    public List<PetOutDTO> getBySpecieName(String specie) {
        log.debug("Buscando mascotas de la especie: {}", specie);
        List<PetOutDTO> pets = petRepositoryAdapter.findBySpecieName(specie)
                .stream()
                .map(petMapper::toOutDTO)
                .toList();
        log.debug("Se han encontrado {} mascotas de la especie: {}", pets.size(), specie);
        return pets;
    }

    @Override
    public Page<PetOutDTO> getAll(Pageable pageable) {
        log.debug("Recuperando todas las mascotas de la base de datos...");
        Page<Pet> pets = petRepositoryAdapter.findAll(pageable);
        log.debug("Se han recuperado {} mascotas en total.", pets.getSize());
        return pets.map(petMapper::toOutDTO);
    }

    @Override
    public Page<PetOutDTO> getPetByFilters(Long pet_id, String name, Long breed_id, Long specie_id, Pageable pageable) {
        log.debug("Buscando mascotas con filtros -Id_mascota {}, Nombre: {}, Id_raza: {}, Id_especie: {}", pet_id, name,
                breed_id, specie_id);
        Page<Pet> pets = petRepositoryAdapter.findByFilters(pet_id, name, breed_id, specie_id, pageable);
        log.debug("Se han encontrado {} mascotas con los filtros proporcionados.", pets.getSize());
        return pets.map(petMapper::toOutDTO);
    }

    @Override
    public PetOutDTO save(PetInDTO petInDTO, String username) {
        log.info("Guardando nueva mascota para el usuario: {}", username);

        log.debug("Buscando usuario dueño: {}", username);
        User user = userRepositoryAdapter.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException());
        log.debug("Usuario {} encontrado. ID: {}", username, user.getId());

        log.debug("Buscando especie: {}", petInDTO.getSpecieId());
        Specie specie = specieRepositoryAdapter.findById(petInDTO.getSpecieId())
                .orElseThrow(() -> new IdNotFoundException());
        log.debug("Especie {} encontrada. ID: {}", petInDTO.getSpecieId(), specie.getId());

        log.debug("Buscando raza: {}", petInDTO.getBreedId());
        Breed breed = breedRepositoryAdapter.findById(petInDTO.getBreedId())
                .orElseThrow(() -> new IdNotFoundException());
        log.debug("Raza {} encontrada. ID: {}", petInDTO.getBreedId(), breed.getId());

        Pet pet = petMapper.toEntity(petInDTO);

        pet.setUser(user);
        pet.setSpecie(specie);
        pet.setBreed(breed);

        log.debug("Guardando mascota en la base de datos...");
        Pet petSaved = petRepositoryAdapter.save(pet);
        log.info("Mascota {} guardada con éxito. ID: {}", petSaved.getName(), petSaved.getId());

        return petMapper.toOutDTO(petSaved);
    }

    @Override
    public void delete(Long id) {
        log.debug("Buscando mascota con ID: {} para eliminar...", id);
        Pet pet = petRepositoryAdapter.findById(id).orElseThrow(() -> new IdNotFoundException());
        log.debug("Mascota con ID: {} encontrada. Procediendo a eliminar...", id);
        petRepositoryAdapter.delete(pet);
        log.info("Mascota con ID: {} eliminada con éxito.", id);
    }

    @Override
    public PetOutDTO updatePet(Long petId, PetUpdateDTO petUpdateDTO, String username) {

        log.debug("Buscando mascota con ID: {} para actualizar...", petId);
        Pet pet = petRepositoryAdapter.findById(petId).orElseThrow(() -> new IdNotFoundException());
        log.debug("Mascota con ID: {} encontrada. Verificando permisos del usuario: {}", petId, username);

        if (!username.equals(pet.getUser().getUsername())) {
            throw new SecurityException("No tienes permiso para actualizar esta mascota.");
        }
        log.debug("Permisos verificados. Actualizando información de la mascota...");

        if (petUpdateDTO.getName() != null) {
            pet.setName(petUpdateDTO.getName());
            log.debug("Nombre actualizado a: {}", petUpdateDTO.getName());
        }

        if (petUpdateDTO.getWeight() != null) {
            pet.setWeight(petUpdateDTO.getWeight());
            log.debug("Peso actualizado a: {}", petUpdateDTO.getWeight());
        }

        if (petUpdateDTO.getDietInfo() != null) {
            pet.setDietInfo(petUpdateDTO.getDietInfo());
            log.debug("Información de dieta actualizada a: {}", petUpdateDTO.getDietInfo());
        }

        log.debug("Guardando cambios en la base de datos...");
        Pet updatedPet = petRepositoryAdapter.save(pet);
        log.info("Mascota con ID: {} actualizada con éxito.", petId);

        return petMapper.toOutDTO(updatedPet);
    }

}
