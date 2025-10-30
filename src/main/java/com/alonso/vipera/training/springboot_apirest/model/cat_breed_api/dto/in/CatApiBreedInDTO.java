package com.alonso.vipera.training.springboot_apirest.model.cat_breed_api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos de raza de gato obtenidos desde la Cat API externa.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatApiBreedInDTO {

    @Schema(description = "ID único de la raza en la Cat API.",
            example = "abys")
    private String id;

    @Schema(description = "Nombre de la raza de gato.",
            example = "Abyssinian")
    private String name;

    @Schema(description = "Descripción detallada de la raza.",
            example = "Active, energetic and playful cat breed.")
    private String description;
}