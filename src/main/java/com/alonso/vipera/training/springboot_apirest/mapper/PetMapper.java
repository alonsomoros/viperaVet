package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;

import lombok.AllArgsConstructor;

/**
 * Mapper para convertir entre entidades Pet y sus DTOs correspondientes.
 */
@Component
@AllArgsConstructor
public class PetMapper {

    private BreedMapper breedMapper;
    private SpecieMapper specieMapper;

    /**
     * Convierte un DTO de entrada PetInDTO a una entidad Pet.
     *
     * @param dto DTO de entrada con los datos de la mascota.
     * @return Entidad Pet correspondiente.
     */
    public Pet toEntity(PetInDTO dto) {
        if (dto == null)
            return null;
        return Pet.builder()
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .weight(dto.getWeight())
                .dietInfo(dto.getDietInfo())
                .build();
    }

    /**
     * Convierte una entidad Pet a su correspondiente DTO de salida PetOutDTO.
     *
     * @param entity Entidad Pet a convertir.
     * @return DTO de salida PetOutDTO.
     */
    public PetOutDTO toOutDTO(Pet entity) {
        if (entity == null)
            return null;
        return new PetOutDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthDate(),
                entity.getWeight(),
                entity.getDietInfo(),
                specieMapper.toDTO(entity.getSpecie()),
                breedMapper.toDTO(entity.getBreed()),
                entity.getCreatedAt());
    }
}
