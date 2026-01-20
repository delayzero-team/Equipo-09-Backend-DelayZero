package com.aluraone.delayzero.infra.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    protected CustomApiException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
