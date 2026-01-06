package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.dto.in.PredictionRequest;
<<<<<<< HEAD
import com.aluraone.delayzero.infra.response.ResponseHandler;
import com.aluraone.delayzero.service.PredictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
=======
import com.aluraone.delayzero.service.PredictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> bceeafbbcba7cdc3a4c3020f2cdd0d05c1c22740
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;

@RestController
@RequestMapping("/prediction")
public class FlightPredictController {

    @Autowired
<<<<<<< HEAD
    PredictService ps;

    @PostMapping
    public ResponseEntity<PredictionData> makePrediction(@RequestBody @Valid PredictionRequest request) {
       return ResponseEntity.ok(ps.callModel(request));
=======
    PredictService pServ;

    @PostMapping
    public ResponseEntity<PredictionData> makePrediction(@RequestBody @Valid PredictionRequest predictionRequest) {
        /*
        DATOS PARA PROBAR EL CONTROLLER, LA ENTRADA DEL JSON ESPERADO PARA REALIZAR LA PREDICCIÓN
        Y LA RESPUESTA EN JSON PARA EL FRONTEND (RESPUESTA DE LA PREDICCIÓN):
        */


        /*
        System.out.println(predictionRequest);
        PredictionData predData = new PredictionData("Puntual", 0.82);
        return ResponseHandler.buildResponse("Prediccion obtenida con exito", HttpStatus.OK,
                predData);
         */

        pServ.getPrediction(predictionRequest);

        return null;

>>>>>>> bceeafbbcba7cdc3a4c3020f2cdd0d05c1c22740
    }

}
