package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.dto.in.PrediccionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediction")
public class FlightPredictController {

    @PostMapping
    public ResponseEntity<PredictionData> realizarPrediccion(@RequestBody @Valid PrediccionRequest prediccionRequest) {
        /*
        DATOS PARA PROBAR EL CONTROLLER, LA ENTRADA DEL JSON ESPERADO PARA REALIZAR LA PREDICCIÓN
        Y LA RESPUESTA EN JSON PARA EL FRONTEND (RESPUESTA DE LA PREDICCIÓN):
        */
        System.out.println(prediccionRequest);
        PredictionData datosPrediccion = new PredictionData("Puntual", 0.80);
        return ResponseEntity.ok(datosPrediccion);
    }

}
