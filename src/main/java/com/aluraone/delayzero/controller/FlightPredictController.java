package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.infra.exception.PredictionException;
import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.service.PredictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/prediction")
public class FlightPredictController {

    @Autowired
    PredictService ps;

    @PostMapping
    public ResponseEntity<PredictionData> makePrediction( @RequestBody @Valid PredictionRequest request, Errors errors) throws PredictionException {

        if (errors.hasErrors()) {
            FieldError fieldError = errors.getFieldError();
            String errorMsg = (fieldError != null)
                ? fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage())
                : PredictionException.INVALID_REQUEST;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        }
        
        return ResponseEntity.ok(ps.callModel(request));
    }

}
