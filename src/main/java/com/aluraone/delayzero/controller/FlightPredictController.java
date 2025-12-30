package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.infra.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediction")
public class FlightPredictController {

    @PostMapping
    public ResponseEntity<PredictionData> makePrediction(@RequestBody @Valid PredictionRequest predictionRequest) {
        /*
        DATOS PARA PROBAR EL CONTROLLER, LA ENTRADA DEL JSON ESPERADO PARA REALIZAR LA PREDICCIÓN
        Y LA RESPUESTA EN JSON PARA EL FRONTEND (RESPUESTA DE LA PREDICCIÓN):
        */
        System.out.println(predictionRequest);
        PredictionData predData = new PredictionData("Puntual", 0.80);
        return ResponseHandler.buildResponse("Prediccion obtenida con exito", HttpStatus.OK,
                predData);
    }

}
