package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.in.PredictionRequest;

import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.service.PredictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@Tag(name = "Prediction", description = "Make prediction from provided flight data")
@RequestMapping("/prediction")
public class FlightPredictController {

    @Autowired
    private PredictService ps;

    @PostMapping

    public ResponseEntity<PredictionData> makePrediction(@RequestBody @Valid PredictionRequest request) {
        return ResponseEntity.ok(ps.callModel(request));

    }
}
