package com.alura.forohub.infra.exception.handler;

import com.alura.forohub.infra.exception.CustomApiException;
import com.alura.forohub.infra.response.ResponseHandler;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {CustomApiException.class})
    public ResponseEntity<Object> handleException(CustomApiException e){
        return ResponseHandler.buildResponse(e.getMessage(), e.getReturnStatus(), null);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<DatosErrorValidacion>> handleValidationException(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream()
                .map(DatosErrorValidacion::new).toList();
        return ResponseHandler.buildResponse("Error de validacion, verifique los datos", HttpStatus.BAD_REQUEST,
                errores);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<DatosErrorConstraint> handleDBException(DataIntegrityViolationException e){

        Throwable c = e.getCause();
        String name = "Error desconocido";
        while (c != null) {
            if (c instanceof ConstraintViolationException cve) {
                name = cve.getSQLException().getLocalizedMessage();
                name = name.split("Detail: ")[1];
            }
            c = c.getCause();
        }

        return ResponseHandler.buildResponse("Ocurrio un error de servidor, verifique los datos ingresados", HttpStatus.BAD_REQUEST,
                new DatosErrorConstraint("Error de restricción de BD", name));
    }

    public record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    public record DatosErrorConstraint(String error, String detalle){
    }

}
