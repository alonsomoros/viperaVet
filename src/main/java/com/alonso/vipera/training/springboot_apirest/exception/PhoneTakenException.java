package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un teléfono específico ya está en uso en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * usuarios cuando:
 * - Se intenta registrar un usuario con un teléfono que ya existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class PhoneTakenException extends RuntimeException {
    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public PhoneTakenException() {
        super("Telefono ya está en uso");
    }
}
