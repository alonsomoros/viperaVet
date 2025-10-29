package com.alonso.vipera.training.springboot_apirest.model.pet.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Información de una raza de mascota.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreedOutDTO {

    @Schema(description = "ID único de la raza.", example = "1")
    private Long id;

    @Schema(description = "Nombre de la raza.", example = "Golden Retriever")
    private String name;

    @Schema(description = "Descripción de la raza.", example = "Perro de tamaño grande, amigable y activo.")
    private String description;

    @Schema(description = "ID externo de la API de origen (para sincronización).", example = "1")
    private String externalApiId;

    @Schema(description = "Información de la especie a la que pertenece esta raza.")
    private SpecieOutDTO specie;
}