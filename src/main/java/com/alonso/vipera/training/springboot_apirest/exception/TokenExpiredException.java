package com.alonso.vipera.training.springboot_apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("El token de activación ha expirado");
    }
}
