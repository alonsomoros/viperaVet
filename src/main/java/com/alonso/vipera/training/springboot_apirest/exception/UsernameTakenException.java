package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un nombre de usuario ya está registrado en el sistema.
 * 
 * Esta excepción se utiliza en procesos de registro y actualización de
 * usuarios cuando:
 * - Se intenta registrar un nuevo usuario con un nombre de usuario que ya existe
 * - Se intenta actualizar el nombre de usuario de un usuario a uno que ya está en uso
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class UsernameTakenException extends RuntimeException {
    
    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public UsernameTakenException() {
        super("Nombre de usuario ya está en uso");
    }
}
