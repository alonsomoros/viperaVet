package com.alonso.vipera.training.springboot_apirest.service;

import java.sql.Date;
import java.util.List;

import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;

public interface PetService {

    List<PetOutDTO> getPetsByOwnerUsername(String username);

    List<PetOutDTO> getByName(String name);

    List<PetOutDTO> getByBirthDate(Date birthDate);

    List<PetOutDTO> getByBreed(String breed);

    List<PetOutDTO> getBySpecie(String specie);

    List<PetOutDTO> getAll();

    PetOutDTO save(PetInDTO petInDTO, String username);

    void delete(Long id);

}
