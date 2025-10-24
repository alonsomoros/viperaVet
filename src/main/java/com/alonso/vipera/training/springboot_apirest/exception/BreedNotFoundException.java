package com.alonso.vipera.training.springboot_apirest.exception;

public class BreedNotFoundException extends RuntimeException {
    public BreedNotFoundException() {
        super("Raza no encontrada");
    }
    
}
