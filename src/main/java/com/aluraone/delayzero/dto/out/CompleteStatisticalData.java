package com.aluraone.delayzero.dto.out;

import java.util.List;

public record CompleteStatisticalData(
        int totalPredicciones,
        int prediccionesPuntuales,
        int prediccionesRetrasadas,
        int porcentajeRetrasados,
        List<StatisticsByAirline> estadisticasPorAerolinea,
        List<StatisticsByOrigin> estadisticasPorOrigen,
        List<HourlyStatistics> estadisticasPorHora
) {
}
