package com.alonso.vipera.training.springboot_apirest.model.pet.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Información de una especie de mascota.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecieOutDTO {

    @Schema(description = "ID único de la especie.", example = "1")
    private Long id;

    @Schema(description = "Nombre de la especie.", example = "Perro")
    private String name;

    @Schema(description = "Descripción de la especie.", example = "Mamífero doméstico de la familia de los cánidos.")
    private String description;
}