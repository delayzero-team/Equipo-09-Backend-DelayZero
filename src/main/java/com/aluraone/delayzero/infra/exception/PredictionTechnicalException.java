package com.aluraone.delayzero.infra.exception;

import org.springframework.http.HttpStatus;

public class PredictionTechnicalException extends CustomApiException {

    // Códigos comunes técnicos
    public static final String MODEL_LOAD_FAILED = "MODEL_LOAD_FAILED";
    public static final String FEATURE_BUILDER_FAILED = "FEATURE_BUILDER_FAILED";
    public static final String ONNX_RUNTIME_ERROR = "ONNX_RUNTIME_ERROR";
    public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    public static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";

    public PredictionTechnicalException(String code, String message, Throwable cause) {
        super(message + " (" + cause.getMessage() + ")", HttpStatus.INTERNAL_SERVER_ERROR, code);
    }

    public PredictionTechnicalException(String code, String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, code);
    }
}