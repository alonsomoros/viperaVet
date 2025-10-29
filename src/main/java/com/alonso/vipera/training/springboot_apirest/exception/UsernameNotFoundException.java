package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando un nombre de usuario específico no es encontrado en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * usuarios cuando:
 * - Se intenta acceder a un usuario por un nombre de usuario que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("Username not found");
    }    
}
