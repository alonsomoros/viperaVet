package com.alonso.vipera.training.springboot_apirest.model.user.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Indica si el usuario existe.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExistOutDTO {
    @Schema(description = "Campo que indica si el usuario existe.", example = "true")
    private boolean exist;
}
