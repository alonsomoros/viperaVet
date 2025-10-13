package com.alonso.vipera.training.springboot_apirest.exception;

public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException() {
        super("The password is too weak. It must be at least 6 characters long.");
    }
}
