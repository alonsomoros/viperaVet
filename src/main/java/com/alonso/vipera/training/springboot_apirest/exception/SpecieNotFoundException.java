package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando una especie específica no es encontrada en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * especies de mascotas cuando:
 * - Se intenta acceder a una especie que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class SpecieNotFoundException extends RuntimeException {
    public SpecieNotFoundException() {
        super("Especie no encontrada");
    }
    
}
