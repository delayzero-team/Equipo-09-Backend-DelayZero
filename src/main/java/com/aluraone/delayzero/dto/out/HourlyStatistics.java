package com.aluraone.delayzero.dto.out;

import java.sql.Time;

public record HourlyStatistics(
        Time hora,
        double probabilidadPromedioRetraso
) {
}
