package com.alonso.vipera.training.springboot_apirest.exception;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException() {
        super("Formato de nombre de usuario inválido");
    }    
}
