package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un Id específico no es encontrado en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * entidades cuando:
 * - Se intenta acceder a una entidad por un Id que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException() {
        super("Id no encontrado");
    }
}
