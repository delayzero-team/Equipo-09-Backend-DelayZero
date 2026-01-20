package com.aluraone.delayzero.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PredictionBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(
            PredictionBusinessException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus().value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("code", ex.getCode());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(PredictionTechnicalException.class)
    public ResponseEntity<Map<String, Object>> handleTechnicalException(
            PredictionTechnicalException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus().value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocurrió un error interno. Contacta al equipo técnico.");
        body.put("code", ex.getCode());
        // En producción: no exponer detalles técnicos
        // body.put("detail", ex.getMessage());

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("error", "Validation Failed");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    // Captura cualquier otra excepción no manejada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", "Error inesperado");

        return ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler(PredictionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            PredictionNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("code", ex.getCode());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}