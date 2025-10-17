package com.alonso.vipera.training.springboot_apirest.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
        super("Dirección no encontrada");
    }
    
}
