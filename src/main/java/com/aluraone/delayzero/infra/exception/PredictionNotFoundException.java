package com.aluraone.delayzero.infra.exception;

import org.springframework.http.HttpStatus;

public class PredictionNotFoundException extends CustomApiException {

    public static final String PREDICTION_NOT_FOUND = "PREDICTION_NOT_FOUND";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";

    public PredictionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, PREDICTION_NOT_FOUND);
    }

    public PredictionNotFoundException(String resource, Object id) {
        super("No se encontró " + resource + " con ID: " + id,
              HttpStatus.NOT_FOUND,
              RESOURCE_NOT_FOUND);
    }
}