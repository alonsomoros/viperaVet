package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;

public interface PetService {

    Page<PetOutDTO> getAll(Pageable pageable);

    Page<PetOutDTO> getPetByFilters(Long pet_id, String name, Long breed_id, Long specie_id, Pageable pageable);

    List<PetOutDTO> getPetsByOwnerUsername(String username);

    List<PetOutDTO> getByName(String name);

    List<PetOutDTO> getByBirthDate(Date birthDate);

    List<PetOutDTO> getByBreedName(String breed);

    List<PetOutDTO> getBySpecieName(String specie);

    PetOutDTO save(PetInDTO petInDTO, String username);

    void delete(Long id);

}
