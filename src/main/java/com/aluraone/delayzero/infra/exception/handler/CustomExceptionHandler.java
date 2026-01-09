package com.aluraone.delayzero.infra.exception.handler;

import com.aluraone.delayzero.infra.exception.CustomApiException;
import com.aluraone.delayzero.infra.exception.PredictionException;
import com.aluraone.delayzero.infra.response.ResponseHandler;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = {CustomApiException.class})
    public ResponseEntity<Object> handleException(CustomApiException e) {
        return ResponseHandler.buildResponse(e.getMessage(), e.getReturnStatus(), null);
    }

    @ExceptionHandler(value = {PredictionException.class})
    public ResponseEntity<Object> handlePredictionException(PredictionException e) {
        log.error("Error en predicción: {}", e.getMessage(), e);
        return ResponseHandler.buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<DatosErrorValidacion>> handleValidationException(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream()
                .map(DatosErrorValidacion::new).toList();
        return ResponseHandler.buildResponse(
            "Error de validación, verifique los datos", 
            HttpStatus.BAD_REQUEST,
            errores
        );
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<DatosErrorConstraint> handleDBException(DataIntegrityViolationException e) {
        Throwable c = e.getCause();
        String name = "Error desconocido";
        
        while (c != null) {
            if (c instanceof ConstraintViolationException cve) {
                name = cve.getSQLException().getLocalizedMessage();
                if (name.contains("Detail: ")) {
                    name = name.split("Detail: ")[1];
                }
            }
            c = c.getCause();
        }

        return ResponseHandler.buildResponse(
            "Ocurrió un error de servidor, verifique los datos ingresados", 
            HttpStatus.BAD_REQUEST,
            new DatosErrorConstraint("Error de restricción de BD", name)
        );
    }

    public record DatosErrorValidacion(String campo, String error) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    public record DatosErrorConstraint(String error, String detalle) {
    }
}