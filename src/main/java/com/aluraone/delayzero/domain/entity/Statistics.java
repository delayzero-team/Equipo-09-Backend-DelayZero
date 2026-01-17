package com.aluraone.delayzero.domain.entity;

import com.aluraone.delayzero.domain.repository.PredictionRepository;
import com.aluraone.delayzero.dto.out.CompleteStatisticalData;
import com.aluraone.delayzero.dto.out.HourlyStatistics;
import com.aluraone.delayzero.dto.out.StatisticsByAirline;
import com.aluraone.delayzero.dto.out.StatisticsByOrigin;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    public CompleteStatisticalData getStatisticsPredictions(PredictionRepository predictionRepository) {

        List<StatisticsByAirline> statisticsByAirline = new ArrayList<>();
        List<StatisticsByOrigin> statisticsByOrigin = new ArrayList<>();
        List<HourlyStatistics> statisticsByHour = new ArrayList<>();

        int totalPredictions = predictionRepository.totalPredictions();
        int totalPunctualPredictions = predictionRepository.totalPunctualPredictions();
        int totalDelayedPredictions = predictionRepository.totalDelayedPredictions();
        float delayedPercentage = predictionRepository.percentageDelayed();

        predictionRepository.percentageDelayedAirline().forEach(data -> {
            statisticsByAirline.add(data.createDTO());
        });

        predictionRepository.percentageDelayedOrigin().forEach(data -> {
            statisticsByOrigin.add(data.createDTO());
        });

        predictionRepository.delayedAverageHour().forEach(data -> {
            statisticsByHour.add(data.createDTO());
        });

        return new CompleteStatisticalData(
                totalPredictions,
                totalPunctualPredictions,
                totalDelayedPredictions,
                delayedPercentage,
                statisticsByAirline,
                statisticsByOrigin,
                statisticsByHour
                );
    }
}
