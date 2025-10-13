package com.alonso.vipera.training.springboot_apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Email ya existente
    @ExceptionHandler({ EmailTakenException.class })
    public ResponseEntity<ErrorResponse> handleEmailTakenException(EmailTakenException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Email no encontrado
    @ExceptionHandler({ EmailNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Email con formato inválido
    @ExceptionHandler({ InvalidEmailException.class })
    public ResponseEntity<ErrorResponse> handleInvalidEmailException(InvalidEmailException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Username ya existente
    @ExceptionHandler({ UsernameTakenException.class })
    public ResponseEntity<ErrorResponse> handleUsernameTakenException(UsernameTakenException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Username no encontrado
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Username con formato inválido
    @ExceptionHandler({ InvalidUsernameException.class })
    public ResponseEntity<ErrorResponse> handleInvalidUsernameException(InvalidUsernameException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Id no encontrado
    @ExceptionHandler({ IdNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(IdNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Password con formato inválido
    @ExceptionHandler({ WeakPasswordException.class })
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(WeakPasswordException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Error en creación de usuario
    @ExceptionHandler({ UserCreationException.class })
    public ResponseEntity<ErrorResponse> handleUserCreationException(UserCreationException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
