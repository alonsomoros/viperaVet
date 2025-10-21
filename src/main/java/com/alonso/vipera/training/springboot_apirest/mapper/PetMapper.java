package com.alonso.vipera.training.springboot_apirest.mapper;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.in.PetInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.PetOutDTO;

@Component
public class PetMapper {


    public Pet toEntity(PetInDTO dto) {
        if (dto == null)
            return null;
        return Pet.builder()
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .weight(dto.getWeight())
                .diet_info(dto.getDiet_info())
                .build();
    }

    public PetOutDTO toOutDTO(Pet entity) {
        if (entity == null)
            return null;
        return new PetOutDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthDate(),
                entity.getWeight(),
                entity.getDiet_info(),
                entity.getCreatedAt().toLocalDate());
    }
}
