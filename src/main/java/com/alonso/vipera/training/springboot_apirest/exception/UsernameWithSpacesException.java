package com.alonso.vipera.training.springboot_apirest.exception;

public class UsernameWithSpacesException extends RuntimeException {
    public UsernameWithSpacesException() {
        super("Username cannot contain spaces");
    }    
}
