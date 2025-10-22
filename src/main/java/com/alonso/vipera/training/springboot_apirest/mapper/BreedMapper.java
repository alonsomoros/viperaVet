package com.alonso.vipera.training.springboot_apirest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.dogBreedAPI.dto.in.DogApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.catBreedAPI.dto.in.CatApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;

@Component
public class BreedMapper {
    public List<Breed> fromDogApiToEntity(List<DogApiBreedInDTO> external_dto, Specie specie) {
        if (external_dto == null || external_dto.isEmpty())
            return null;
        else {
            return external_dto.stream()
                    .map(dto -> new Breed(
                            null,
                            dto.getName(),
                            specie,
                            dto.getId().toString()))
                    .toList();
        }
    }

    public List<Breed> fromCatApiToEntity(List<CatApiBreedInDTO> external_dto, Specie specie) {
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

    public BreedOutDTO toDTO(Breed breedEntity) {
        return new BreedOutDTO(breedEntity.getId(), breedEntity.getName(), breedEntity.getSpecie().getId(),
                breedEntity.getExternalApiId());
    }
}
