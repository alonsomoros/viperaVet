package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un Role específico no es encontrado en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * entidades cuando:
 * - Se intenta acceder a una entidad por un Role que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public RoleNotFoundException() {
        super("Role no encontrado");
    }
}
