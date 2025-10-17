package com.alonso.vipera.training.springboot_apirest.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException() {
        super("Nombre de usuario ya está en uso");
    }
}
