package com.aluraone.delayzero.infra.exception;

import org.springframework.http.HttpStatus;

public class PredictionBusinessException extends CustomApiException {
    
    public static final String INVALID_AIRLINE_CODE = "INVALID_AIRLINE_CODE";
    public static final String INVALID_AIRPORT_CODE = "INVALID_AIRPORT_CODE";
    public static final String INVALID_DATE = "INVALID_DATE";
    public static final String INVALID_DISTANCE = "INVALID_DISTANCE";
    public static final String MISSING_REQUIRED_FIELD = "MISSING_REQUIRED_FIELD";

    public PredictionBusinessException(String code, String message) {
        super(message, HttpStatus.BAD_REQUEST, code);
    }
}