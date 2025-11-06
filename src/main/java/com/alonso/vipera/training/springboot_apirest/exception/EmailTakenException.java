package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un email ya está registrado en el sistema.
 * 
 * Esta excepción se utiliza en procesos de registro y actualización de
 * usuarios cuando:
 * - Se intenta registrar un nuevo usuario con un email que ya existe
 * - Se intenta actualizar el email de un usuario a uno que ya está en uso
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class EmailTakenException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public EmailTakenException() {
        super("Email ya está en uso");
    }
}
