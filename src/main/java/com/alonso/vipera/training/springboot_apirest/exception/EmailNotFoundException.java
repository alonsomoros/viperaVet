package com.alonso.vipera.training.springboot_apirest.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("Email not found");
    }
}
