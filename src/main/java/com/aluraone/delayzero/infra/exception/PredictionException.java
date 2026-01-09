package com.aluraone.delayzero.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PredictionException extends RuntimeException {

    public static final String INVALID_REQUEST = "Datos inválidos, revise las entradas";
    public static final String ENCODER_NOT_FOUND = "Codificador no encontrado para procesar la solicitud";
    public static final String UNKNOWN_FEATURE_COLUMN = "Columna de característica desconocida";
    public static final String EMPTY_FEATURES_ARRAY = "El arreglo de características está vacío";
    public static final String MODEL_PROCESSING_ERROR = "Error al procesar la predicción con el modelo";
    public static final String INVALID_AIRLINE_CODE = "Código de aerolínea inválido o no soportado";
    public static final String INVALID_AIRPORT_CODE = "Código de aeropuerto inválido o no soportado";
    public static final String MODEL_INITIALIZATION_ERROR = "Error al inicializar el modelo de predicción";
    public static final String FEATURE_BUILDER_INITIALIZATION_ERROR = "Error al inicializar el constructor de características";

    public PredictionException(String message) {
        super(message);
    }

    public PredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
