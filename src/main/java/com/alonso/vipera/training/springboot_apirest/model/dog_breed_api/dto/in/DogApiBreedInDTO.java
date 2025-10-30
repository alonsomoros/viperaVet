package com.alonso.vipera.training.springboot_apirest.model.dog_breed_api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos de raza de perro obtenidos desde la Dog API externa.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DogApiBreedInDTO {

    @Schema(description = "ID único de la raza en la Dog API.", example = "1")
    private Integer id;

    @Schema(description = "Nombre de la raza de perro.", example = "Golden Retriever")
    private String name;

    @Schema(description = "Descripción detallada de la raza.", example = "Large-sized dog, friendly and active breed.")
    private String description;
}