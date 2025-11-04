package com.alonso.vipera.training.springboot_apirest.model.pet.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos necesarios para actualizar una mascota en el sistema.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetUpdateDTO {

    @Schema(description = "Nombre de la mascota", example = "Max")
    @Size(min = 2, max = 50)
    private String name;

    @Schema(description = "Fecha de nacimiento de la mascota en formato YYYY-MM-DD", example = "2020-05-15")
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "200.0")
    private Double weight;

    @Schema(description = "Información sobre la dieta de la mascota", example = "Comida balanceada para perros adultos")
    @Size(max = 500)
    private String dietInfo;

}