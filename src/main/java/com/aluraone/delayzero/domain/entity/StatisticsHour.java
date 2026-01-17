package com.aluraone.delayzero.domain.entity;

import com.aluraone.delayzero.dto.out.HourlyStatistics;
import lombok.Getter;

import java.sql.Time;

@Getter
public class StatisticsHour {

    private Double delayedAverage;
    private Time hour;

    public StatisticsHour(Double delayedAverage, Time hour) {

        this.delayedAverage = delayedAverage == null ? 0 : delayedAverage;
        this.hour = hour;
    }

    public HourlyStatistics createDTO() {
        return new HourlyStatistics(hour, delayedAverage);
    }
}
