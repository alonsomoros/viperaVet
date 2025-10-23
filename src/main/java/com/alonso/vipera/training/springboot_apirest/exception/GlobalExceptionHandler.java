package com.alonso.vipera.training.springboot_apirest.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Email ya existente
    @ExceptionHandler({ EmailTakenException.class })
    public ResponseEntity<ErrorResponse> handleEmailTakenException(EmailTakenException exception) {
        log.warn("Conflicto al registrar email: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Email no encontrado
    @ExceptionHandler({ EmailNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailNotFoundException exception) {
        log.warn("Email no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Username ya existente
    @ExceptionHandler({ UsernameTakenException.class })
    public ResponseEntity<ErrorResponse> handleUsernameTakenException(UsernameTakenException exception) {
        log.warn("Conflicto al registrar username: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Username no encontrado
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        log.warn("Username no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Id no encontrado
    @ExceptionHandler({ IdNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(IdNotFoundException exception) {
        log.warn("ID no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Error en creación de usuario
    @ExceptionHandler({ UserCreationException.class })
    public ResponseEntity<ErrorResponse> handleUserCreationException(UserCreationException exception) {
        log.error("Error al crear usuario: {}", exception.getMessage(), exception);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Credenciales inválidas
    @ExceptionHandler({ BadCredentialsInputException.class })
    public ResponseEntity<ErrorResponse> handleBadCredentialsInputException(BadCredentialsInputException exception) {
        log.warn("Credenciales inválidas: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // Runtime Exception genérica
    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        log.error("Error de runtime no esperado: {}", exception.getMessage(), exception);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Errores de validación de campos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.warn("Errores de validación: {}", errors);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}
