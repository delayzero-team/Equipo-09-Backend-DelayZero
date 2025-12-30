package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.CompleteStatisticalData;
import com.aluraone.delayzero.dto.out.StatisticsByAirline;
import com.aluraone.delayzero.dto.out.HourlyStatistics;
import com.aluraone.delayzero.dto.out.StatisticsByOrigin;
import com.aluraone.delayzero.infra.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class FlightPredictStatisticsController {

    @GetMapping
    public ResponseEntity<CompleteStatisticalData> stats() {
        /*
        DATOS DE PRUEBA SOLO PARA PROBAR EL FUNCIONAMIENTO DEL CONTROLLER Y LA RESPUESTA DEL JSON
        ESPERADO POR EL FRONTEND PARA MOSTRAR AL CLIENTE LAS ESTADISTICAS:
        */
        //Datos ficticios aerolinea:
        var air1 = new StatisticsByAirline("Aerolinea1", 45);
        var air2 = new StatisticsByAirline("Aerolinea2", 5);
        var air3 = new StatisticsByAirline("Aerolinea3", 65);

        List<StatisticsByAirline> airlineData = List.of(air1, air2, air3);

        //Datos ficticios origen:
        var origin1 = new StatisticsByOrigin("Origen1", 56);
        var origin2 = new StatisticsByOrigin("Origen2", 26);
        var origin3 = new StatisticsByOrigin("Origen3", 45);

        List<StatisticsByOrigin> originData = List.of(origin1, origin2, origin3);

        //Datos ficticios hora:
        var hour1 = new HourlyStatistics(10, 0.4);
        var hour2 = new HourlyStatistics(14, 0.23);
        var hour3 = new HourlyStatistics(4, 0.78);

        List<HourlyStatistics> hourData = List.of(hour1, hour2, hour3);

        CompleteStatisticalData statsData = new CompleteStatisticalData(
                45,
                25,
                15,
                46,
                airlineData,
                originData,
                hourData
        );

        return ResponseHandler.buildResponse("Estadisticas compiladas con exito",
                HttpStatus.OK,
                statsData);
    }
}
