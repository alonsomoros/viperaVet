package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un usuario no tiene el rol necesario para realizar una accion.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * entidades cuando:
 * - Se intenta realizar una accion sin tener el rol necesario
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class WrongRoleActionException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public WrongRoleActionException() {
        super("No tienes el rol necesario para realizar esta accion");
    }
}
