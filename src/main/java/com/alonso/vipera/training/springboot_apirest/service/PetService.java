package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;

public interface PetService {

    List<PetOutDTO> getAll();

    List<PetOutDTO> getPetByFilters(Long pet_id, String name, Long breed_id, Long specie_id);

    List<PetOutDTO> getPetsByOwnerUsername(String username);

    List<PetOutDTO> getByName(String name);

    List<PetOutDTO> getByBirthDate(Date birthDate);

    List<PetOutDTO> getByBreedName(String breed);

    List<PetOutDTO> getBySpecieName(String specie);

    PetOutDTO save(PetInDTO petInDTO, String username);

    void delete(Long id);

}
