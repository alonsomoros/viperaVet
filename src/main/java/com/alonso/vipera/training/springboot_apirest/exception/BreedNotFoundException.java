package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando una raza específica no es encontrada en el sistema.
 * 
 * Esta excepción se utiliza en operaciones relacionadas con la gestión de
 * razas de mascotas cuando:
 * - Se intenta acceder a una raza que no existe
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class BreedNotFoundException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public BreedNotFoundException() {
        super("Raza no encontrada");
    }

}
