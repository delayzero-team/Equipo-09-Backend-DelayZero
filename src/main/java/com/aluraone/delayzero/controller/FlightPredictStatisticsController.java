package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.domain.entity.Statistics;
import com.aluraone.delayzero.domain.repository.PredictionRepository;
import com.aluraone.delayzero.dto.out.CompleteStatisticalData;
import com.aluraone.delayzero.dto.out.StatisticsByAirline;
import com.aluraone.delayzero.dto.out.HourlyStatistics;
import com.aluraone.delayzero.dto.out.StatisticsByOrigin;
import com.aluraone.delayzero.infra.response.ResponseHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Statistics", description = "Retrieve flight delay statistics")
@RequestMapping("/statistics")
public class FlightPredictStatisticsController {

    private Statistics statistics;

    @Autowired
    private PredictionRepository predictionRepository;

    @GetMapping
    public ResponseEntity<CompleteStatisticalData> stats() {

        statistics = new Statistics();
        CompleteStatisticalData statsData = statistics.getStatisticsPredictions(predictionRepository);

        return ResponseHandler.buildResponse("Estadisticas compiladas con exito",
                HttpStatus.OK,
                statsData);
    }
}
