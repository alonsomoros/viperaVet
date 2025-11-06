package com.alonso.vipera.training.springboot_apirest.exception;

/**
 * Excepción lanzada cuando las credenciales proporcionadas son inválidas o
 * están mal formateadas.
 * 
 * Esta excepción se utiliza en procesos de autenticación cuando:
 * - Las credenciales no cumplen con el formato esperado
 * - Los datos de entrada son nulos o vacíos
 * - El formato de username/email es incorrecto
 * 
 * Extiende RuntimeException para ser una excepción no verificada.
 */
public class BadCredentialsInputException extends RuntimeException {

    /**
     * Constructor por defecto que inicializa la excepción con un mensaje
     */
    public BadCredentialsInputException() {
        super("Credenciales de entrada inválidas");
    }

}
