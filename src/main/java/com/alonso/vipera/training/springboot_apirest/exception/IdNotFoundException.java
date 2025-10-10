package com.alonso.vipera.training.springboot_apirest.exception;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException() {
        super("Id not found");
    }
}
