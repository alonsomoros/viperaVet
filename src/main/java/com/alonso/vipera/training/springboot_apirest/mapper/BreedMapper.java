package com.alonso.vipera.training.springboot_apirest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.DogApiBreedDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;

@Component
public class BreedMapper {
    public List<Breed> fromApiToEntity(List<DogApiBreedDTO> external_dto, Specie specie) {
        if (external_dto == null || external_dto.isEmpty())
            return null;
        else {
            return external_dto.stream()
                    .map(dto -> new Breed(
                            null,
                            dto.getName(),
                            specie,
                            dto.getId()))
                    .toList();
        }
    }
}
