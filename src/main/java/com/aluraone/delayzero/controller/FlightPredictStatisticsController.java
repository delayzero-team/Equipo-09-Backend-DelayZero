package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.out.CompleteStatisticalData;
import com.aluraone.delayzero.dto.out.StatisticsByAirline;
import com.aluraone.delayzero.dto.out.HourlyStatistics;
import com.aluraone.delayzero.dto.out.StatisticsByOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class FlightPredictStatisticsController {

    @GetMapping
    public ResponseEntity<CompleteStatisticalData> estadisticas() {
        /*
        DATOS DE PRUEBA SOLO PARA PROBAR EL FUNCIONAMIENTO DEL CONTROLLER Y LA RESPUESTA DEL JSON
        ESPERADO POR EL FRONTEND PARA MOSTRAR AL CLIENTE LAS ESTADISTICAS:
        */
        //Datos ficticios aerolinea:
        var aerolinea1 = new StatisticsByAirline("Aerolinea1", 45);
        var aerolinea2 = new StatisticsByAirline("Aerolinea2", 5);
        var aerolinea3 = new StatisticsByAirline("Aerolinea3", 65);

        List<StatisticsByAirline> datosArolinea = List.of(aerolinea1, aerolinea2, aerolinea3);

        //Datos ficticios origen:
        var origen1 = new StatisticsByOrigin("Origen1", 56);
        var origen2 = new StatisticsByOrigin("Origen2", 26);
        var origen3 = new StatisticsByOrigin("Origen3", 45);

        List<StatisticsByOrigin> datosOrigen = List.of(origen1, origen2, origen3);

        //Datos ficticios hora:
        var hora1 = new HourlyStatistics(10, 0.4);
        var hora2 = new HourlyStatistics(14, 0.23);
        var hora3 = new HourlyStatistics(4, 0.78);

        List<HourlyStatistics> datosHora = List.of(hora1, hora2, hora3);

        CompleteStatisticalData datosEstadisticas = new CompleteStatisticalData(
                45,
                25,
                15,
                46,
                datosArolinea,
                datosOrigen,
                datosHora
        );

        return ResponseEntity.ok(datosEstadisticas);
    }
}
