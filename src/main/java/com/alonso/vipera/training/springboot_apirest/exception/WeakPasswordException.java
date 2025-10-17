package com.alonso.vipera.training.springboot_apirest.exception;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException() {
        super("Contraseña débil. La contraseña debe tener al menos 6 caracteres.");
    }
}
