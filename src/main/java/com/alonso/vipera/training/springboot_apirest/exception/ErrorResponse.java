package com.alonso.vipera.training.springboot_apirest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    private int status;
    private String message;
    private long timestamp;

}
