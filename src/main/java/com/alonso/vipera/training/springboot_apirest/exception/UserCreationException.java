package com.alonso.vipera.training.springboot_apirest.exception;

public class UserCreationException extends RuntimeException{
    public UserCreationException() {
        super("Error creating user");
    }    
}
