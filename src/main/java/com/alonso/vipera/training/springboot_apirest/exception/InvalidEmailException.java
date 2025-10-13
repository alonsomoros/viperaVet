package com.alonso.vipera.training.springboot_apirest.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Invalid email format");
    }
    
}
