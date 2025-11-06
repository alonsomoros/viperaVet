package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando ocurre un error durante la creación de un usuario en
 * el sistema.
 * 
 * Esta excepción se utiliza en procesos de registro de usuarios cuando:
 * - Hay un fallo al guardar el usuario en la base de datos
 * - No se cumplen ciertas condiciones necesarias para la creación del usuario
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class UserCreationException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public UserCreationException() {
        super("Error creando el usuario en la BBDD");
    }
}
