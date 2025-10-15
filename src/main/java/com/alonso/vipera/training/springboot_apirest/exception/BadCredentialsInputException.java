package com.alonso.vipera.training.springboot_apirest.exception;

public class BadCredentialsInputException extends RuntimeException {
    public BadCredentialsInputException() {
        super("Wrong username or password");
    }
    
}
