package com.alonso.vipera.training.springboot_apirest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase que representa la estructura de una respuesta de error en la API REST.
 * Contiene información sobre el estado del error, el mensaje asociado y la marca de tiempo.
 */
@AllArgsConstructor
@Data
public class ErrorResponse {

    private int status;
    private String message;
    private long timestamp;

}
