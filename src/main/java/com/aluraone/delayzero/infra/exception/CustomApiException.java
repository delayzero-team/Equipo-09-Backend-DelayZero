package com.alura.forohub.infra.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomApiException extends RuntimeException {

    final HttpStatus returnStatus;

    public CustomApiException(String message, HttpStatus returnStatus) {
        super(message);
        this.returnStatus = returnStatus;
    }


}
