package com.alonso.vipera.training.springboot_apirest.model.pet.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la salida de datos de una especie de mascota.
 * Contiene la información pública de la especie que puede ser expuesta a través
 * de la API.
 */
@Schema(description = "Información de una especie de mascota.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecieOutDTO {

    /**
     * ID único de la especie.
     */
    @Schema(description = "ID único de la especie.", example = "1")
    private Long id;

    /**
     * Nombre de la especie.
     */
    @Schema(description = "Nombre de la especie.", example = "Perro")
    private String name;

}