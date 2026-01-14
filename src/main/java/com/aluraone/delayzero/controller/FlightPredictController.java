package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.service.PredictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediction")
public class FlightPredictController {

    @Autowired
    private PredictService ps;

    @PostMapping
    public ResponseEntity<PredictionData> makePrediction(@RequestBody @Valid PredictionRequest request) {
        PredictionData result = ps.callModel(request);
        return ResponseEntity.ok(result);
    }
}
