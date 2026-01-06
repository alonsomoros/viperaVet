package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un telefono específico no es encontrado en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * usuarios cuando:
 * - Se intenta acceder a un usuario por un telefono que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class PhoneNotFoundException extends RuntimeException {
    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public PhoneNotFoundException(String message) {
        super("Telefono no encontrado");
    }
}
