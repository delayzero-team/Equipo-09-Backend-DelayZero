package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.CompleteStatisticalData;
import com.aluraone.delayzero.dto.out.HourlyStatistics;
import com.aluraone.delayzero.dto.out.StatisticsByAirline;
import com.aluraone.delayzero.dto.out.StatisticsByOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class FlightPredictStatisticsController {

    @GetMapping
    public ResponseEntity<CompleteStatisticalData> stats() {
        // Datos ficticios (solo para pruebas y demo del frontend)
        // En un futuro real, estos datos vendrían de la base de datos o cálculos agregados

        // Estadísticas por aerolínea (porcentaje de retraso)
        var air1 = new StatisticsByAirline("Aerolinea1", 45);
        var air2 = new StatisticsByAirline("Aerolinea2", 5);
        var air3 = new StatisticsByAirline("Aerolinea3", 65);
        var air4 = new StatisticsByAirline("Aerolinea4", 88);
        List<StatisticsByAirline> airlineData = List.of(air1, air2, air3, air4);

        // Estadísticas por aeropuerto de origen
        var origin1 = new StatisticsByOrigin("Origen1", 56);
        var origin2 = new StatisticsByOrigin("Origen2", 26);
        var origin3 = new StatisticsByOrigin("Origen3", 45);
        var origin4 = new StatisticsByOrigin("Origen4", 90);
        List<StatisticsByOrigin> originData = List.of(origin1, origin2, origin3, origin4);

        // Estadísticas por hora del día (probabilidad promedio de retraso)
        var hour1 = new HourlyStatistics(10, 0.4);
        var hour2 = new HourlyStatistics(14, 0.23);
        var hour3 = new HourlyStatistics(4, 0.78);
        var hour4 = new HourlyStatistics(8, 0.89);
        List<HourlyStatistics> hourData = List.of(hour1, hour2, hour3, hour4);

        // Construcción del objeto completo
        CompleteStatisticalData statsData = new CompleteStatisticalData(
                45,               // totalPredicciones
                25,               // prediccionesPuntuales
                15,               // prediccionesRetrasadas
                46,               // porcentajeRetrasados
                airlineData,
                originData,
                hourData
        );

        // Respuesta directa: 200 OK con el body JSON
        return ResponseEntity.ok(statsData);
    }
}