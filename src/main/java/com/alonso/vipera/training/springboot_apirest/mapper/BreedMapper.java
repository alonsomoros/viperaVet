package com.alonso.vipera.training.springboot_apirest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.cat_breed_api.dto.in.CatApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.dog_breed_api.dto.in.DogApiBreedInDTO;
import com.alonso.vipera.training.springboot_apirest.model.pet.Breed;
import com.alonso.vipera.training.springboot_apirest.model.pet.Specie;
import com.alonso.vipera.training.springboot_apirest.model.pet.dto.out.BreedOutDTO;

import lombok.AllArgsConstructor;

/**
 * Mapper para convertir entre entidades Breed y sus DTOs correspondientes,
 * así como para mapear datos de APIs externas a entidades internas.
 */
@Component
@AllArgsConstructor
public class BreedMapper {

    private final SpecieMapper specieMapper;

    /**
     * Convierte una lista de DTOs de breeds de perros obtenidos de una API externa
     * a una lista de entidades Breed.
     *
     * @param external_dto Lista de DTOs de razas de perros de la API externa.
     * @param specie       Especie asociada a las razas.
     * @return Lista de entidades Breed.
     */
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

    /**
     * Convierte una lista de DTOs de breeds de gatos obtenidos de una API externa
     * a una lista de entidades Breed.
     *
     * @param external_dto Lista de DTOs de razas de gatos de la API externa.
     * @param specie       Especie asociada a las razas.
     * @return Lista de entidades Breed.
     */
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

    /**
     * Convierte una entidad Breed a su correspondiente DTO de salida.
     *
     * @param breedEntity Entidad Breed a convertir.
     * @return DTO de salida BreedOutDTO.
     */
    public BreedOutDTO toDTO(Breed breedEntity) {
        return new BreedOutDTO(breedEntity.getId(), breedEntity.getName(),
                breedEntity.getExternalApiId(), specieMapper.toDTO(breedEntity.getSpecie()));
    }
}
