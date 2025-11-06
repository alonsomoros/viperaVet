package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un email específico no es encontrado en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * usuarios cuando:
 * - Se intenta acceder a un usuario por un email que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class EmailNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public EmailNotFoundException() {
        super("Email no encontrado");
    }
}
