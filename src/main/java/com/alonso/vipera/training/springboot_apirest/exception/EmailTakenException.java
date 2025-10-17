package com.alonso.vipera.training.springboot_apirest.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException() {
        super("Email ya está en uso");
    }
}
