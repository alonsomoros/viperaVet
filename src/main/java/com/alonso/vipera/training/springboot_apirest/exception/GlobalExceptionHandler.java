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

/**
 * Manejador global de excepciones para toda la aplicación.
 * 
 * Esta clase centraliza el manejo de errores utilizando {@code @RestControllerAdvice},
 * proporcionando respuestas HTTP consistentes y logging adecuado para diferentes
 * tipos de excepciones que pueden ocurrir en la aplicación.
 * 
 * Cada método handler se encarga de:
 * 1. Registrar el error en los logs con el nivel apropiado
 * 2. Crear una respuesta de error estandarizada
 * 3. Devolver el código de estado HTTP correspondiente
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando se intenta registrar un email que ya existe.
     * 
     * @param exception La excepción de email duplicado
     * @return ResponseEntity con error HTTP 409 (Conflict) y detalles del error
     */
    @ExceptionHandler({ EmailTakenException.class })
    public ResponseEntity<ErrorResponse> handleEmailTakenException(EmailTakenException exception) {
        log.warn("Conflicto al registrar email: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones cuando no se encuentra un email específico.
     * 
     * @param exception La excepción de email no encontrado
     * @return ResponseEntity con error HTTP 404 (Not Found) y detalles del error
     */
    @ExceptionHandler({ EmailNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailNotFoundException exception) {
        log.warn("Email no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando se intenta registrar un username que ya existe.
     * 
     * @param exception La excepción de username duplicado
     * @return ResponseEntity con error HTTP 409 (Conflict) y detalles del error
     */
    @ExceptionHandler({ UsernameTakenException.class })
    public ResponseEntity<ErrorResponse> handleUsernameTakenException(UsernameTakenException exception) {
        log.warn("Conflicto al registrar username: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones cuando no se encuentra un usuario específico por username.
     * 
     * @param exception La excepción de username no encontrado
     * @return ResponseEntity con error HTTP 404 (Not Found) y detalles del error
     */
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        log.warn("Username no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando se intenta registrar un username que ya existe.
     * 
     * @param exception La excepción de username duplicado
     * @return ResponseEntity con error HTTP 409 (Conflict) y detalles del error
     */
    @ExceptionHandler({ PhoneTakenException.class })
    public ResponseEntity<ErrorResponse> handlePhoneTakenException(PhoneTakenException exception) {
        log.warn("Conflicto al registrar telefono: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones cuando no se encuentra un usuario específico por username.
     * 
     * @param exception La excepción de username no encontrado
     * @return ResponseEntity con error HTTP 404 (Not Found) y detalles del error
     */
    @ExceptionHandler({ PhoneNotFoundException.class })
    public ResponseEntity<ErrorResponse> handlePhoneNotFoundException(PhoneNotFoundException exception) {
        log.warn("Telefono no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando no se encuentra una entidad por su ID.
     * 
     * @param exception La excepción de ID no encontrado
     * @return ResponseEntity con error HTTP 404 (Not Found) y detalles del error
     */
    @ExceptionHandler({ IdNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleIdNotFoundException(IdNotFoundException exception) {
        log.warn("ID no encontrado: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando no se encuentra una raza específica.
     * 
     * @param exception La excepción de raza no encontrada
     * @return ResponseEntity con error HTTP 404 (Not Found) y detalles del error
     */
    @ExceptionHandler({ BreedNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleBreedNotFoundException(BreedNotFoundException exception) {
        log.warn("Raza no encontrada: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando no se encuentra una especie específica.
     * 
     * @param exception La excepción de especie no encontrada
     * @return ResponseEntity con error HTTP 404 (Not Found) y detalles del error
     */
    @ExceptionHandler({ SpecieNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleSpecieNotFoundException(SpecieNotFoundException exception) {
        log.warn("Especie no encontrada: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones durante el proceso de creación de usuarios.
     * 
     * @param exception La excepción de error en creación de usuario
     * @return ResponseEntity con error HTTP 500 (Internal Server Error) y detalles del error
     */
    @ExceptionHandler({ UserCreationException.class })
    public ResponseEntity<ErrorResponse> handleUserCreationException(UserCreationException exception) {
        log.error("Error al crear usuario: {}", exception.getMessage(), exception);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja excepciones por credenciales inválidas o mal formateadas.
     * 
     * @param exception La excepción de credenciales inválidas
     * @return ResponseEntity con error HTTP 401 (Unauthorized) y detalles del error
     */
    @ExceptionHandler({ BadCredentialsInputException.class })
    public ResponseEntity<ErrorResponse> handleBadCredentialsInputException(BadCredentialsInputException exception) {
        log.warn("Credenciales inválidas: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja excepciones de runtime no capturadas por otros handlers específicos.
     * 
     * Este handler actúa como un "catch-all" para errores inesperados que no tienen
     * un manejo específico definido.
     * 
     * @param exception La excepción de runtime no manejada
     * @return ResponseEntity con error HTTP 500 (Internal Server Error) y detalles del error
     */
    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        log.error("Error de runtime no esperado: {}", exception.getMessage(), exception);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores de validación de campos anotados con {@code @Valid}.
     * 
     * Este método procesa las violaciones de validación de Bean Validation
     * y devuelve una lista detallada de todos los errores encontrados.
     * 
     * @param ex La excepción de argumentos no válidos que contiene los errores de validación
     * @return ResponseEntity con error HTTP 400 (Bad Request) y lista de errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.warn("Errores de validación: {}", errors);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Método de utilidad para crear un mapa con la lista de errores de validación.
     * 
     * @param errors Lista de mensajes de error de validación
     * @return Mapa con clave "errors" y la lista de errores como valor
     */
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}