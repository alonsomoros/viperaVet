package com.alonso.vipera.training.springboot_apirest.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("Username not found");
    }    
}
