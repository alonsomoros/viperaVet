package com.alonso.vipera.training.springboot_apirest.exception;

public class SpecieNotFoundException extends RuntimeException {
    public SpecieNotFoundException() {
        super("Especie no encontrada");
    }
    
}
